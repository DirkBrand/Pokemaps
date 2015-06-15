package com.pokemaps.pokemaps;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.pokemaps.pokemaps.NewGameDialog.NewGameDialogListener;
import com.pokemaps.pokemaps.StarterPokemonDialog.StarterPokemonDialogListener;
import com.pokemaps.pokemaps.data.DatabaseHelper;
import com.pokemaps.pokemaps.data.Item;
import com.pokemaps.pokemaps.data.Pokemon;
import com.pokemaps.pokemaps.data.User;
import com.pokemaps.pokemaps.data.UserItem;
import com.pokemaps.pokemaps.data.UserPokemon;
import com.pokemaps.pokemaps.data.Zone;
import com.pokemaps.pokemaps.drawer.ItemListFragment;
import com.pokemaps.pokemaps.drawer.PokedexFragment;

public class MainMapActivity extends Activity implements OnMapReadyCallback,
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		NewGameDialogListener, StarterPokemonDialogListener {

	public static final String PREFS_NAME = "MyPrefsFile";

	private boolean mIsBound = false;
	private BackgroundSoundService mServ;

	// MENU STUFF
	RelativeLayout mDrawerPane;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private TextView mDrawerUserName;
	private ImageView mDrawerAvatar;
	ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

	// DATEBASE STUFF
	private DatabaseHelper databaseHelper = null;
	private Dao<User, Integer> userDao;
	private Dao<Pokemon, Integer> pokemonDao;
	private Dao<UserPokemon, Integer> userPokeDao;
	private Dao<Zone, Integer> zoneDao;
	private Dao<UserItem, Integer> userItemDao;
	private Dao<Item, Integer> itemDao;

	// ZONECHECK STUFF
	public ZoneCheckResultReciever zoneCheckReceiver;

	// Request code to use when launching new game dialog
	private static final int NEW_GAME_DIALOG = 1;
	// Request code to use when launching starter pokemon dialog
	private static final int STARTER_POKEMON_DIALOG = 2;
	// Request code to use when launching the resolution activity
	private static final int REQUEST_RESOLVE_ERROR = 1001;
	// Unique tag for the error dialog fragment
	private static final String DIALOG_ERROR = "dialog_error";
	// Bool to track whether the app is already resolving an error
	private boolean mResolvingError = false;

	private static final String STATE_RESOLVING_ERROR = "resolving_error";
	private static final String STATE_REQUESTING_LOCATION_UPDATES = "requesting_location";
	private static final String LOCATION_KEY = "last_known_location";

	GoogleApiClient mGoogleApiClient;

	MapFragment mapFragment;

	LocationRequest mLocationRequest;
	private boolean mRequestingLocationUpdates = false;

	Location mLastLocation;
	LatLng userLatLng;

	List<Zone> pokemonZones = new ArrayList<Zone>();

	// USER STUFF
	User currentUser;

	private boolean isInFront;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateValuesFromBundle(savedInstanceState);
		setContentView(R.layout.main_map_layout);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent intent = getIntent();
		boolean new_game = intent.getBooleanExtra("new_game", false);

		// Start music service
		doBindService();

		// Instantiate menu drawer
		mNavItems.add(new NavItem("Pokedex", "View Your Pokemon",
				R.drawable.ic_action_tiles_small));
		mNavItems.add(new NavItem("Backpack", "View Your Items",
				R.drawable.ic_action_box));
		mNavItems.add(new NavItem("Option3", "Blah Blah", R.drawable.ic_broom));

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerPane = (RelativeLayout) findViewById(R.id.leftDrawer);
		mDrawerList = (ListView) findViewById(R.id.navList);
		mDrawerUserName = (TextView) findViewById(R.id.drawerUserName);
		mDrawerAvatar = (ImageView) findViewById(R.id.drawerAvatar);
		// Set the adapter for the list view
		DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
		mDrawerList.setAdapter(adapter);
		// Set the list's click listener
		mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						selectItemFromDrawer(position);
					}
				});

		// Initiate DAOs
		try {
			userDao = getHelper().getUserDao();
			userPokeDao = getHelper().getUserPokemonDao();
			pokemonDao = getHelper().getPokemonDao();
			zoneDao = getHelper().getZoneDao();
			itemDao = getHelper().getItemDao();
			userItemDao = getHelper().getUserItemDao();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// Start a new game
		if (new_game) {
			NewGameDialog newGameFragment = new NewGameDialog();
			newGameFragment.setCancelable(false);
			newGameFragment
					.getDialog()
					.getWindow()
					.setBackgroundDrawable(
							new ColorDrawable(
									android.graphics.Color.TRANSPARENT));
			newGameFragment.show(getFragmentManager(), "dialog");
		} else {
			SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
			int user_id = sharedPref.getInt("user_id", -1);

			try {
				currentUser = userDao.queryForId(user_id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mDrawerUserName.setText(currentUser.getName());
			if (currentUser.getSex() == User.MALE) {
				mDrawerAvatar.setImageResource(R.drawable.trainer_male);
			} else {
				mDrawerAvatar.setImageResource(R.drawable.trainer_female);
			}
		}

		// Start Map stuff
		try {
			createLocationRequest();
			createZones();
			buildGoogleApiClient();

			// Loading map
			mapFragment = (MapFragment) getFragmentManager().findFragmentById(
					R.id.map);
			mapFragment.getMapAsync(this);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Start Services
		// Start ZoneChecker Service
		setupServiceReceiver();
		Intent i = new Intent(this, ZoneCheckService.class);
		i.putExtra("receiver", zoneCheckReceiver);
		startService(i);
		scheduleServiceAlarm();

	}

	private void setupServiceReceiver() {
		zoneCheckReceiver = new ZoneCheckResultReciever(new Handler());
		// This is where we specify what happens when data is received from the
		// service
		zoneCheckReceiver.setReceiver(new ZoneCheckResultReciever.Receiver() {
			@Override
			public void onReceiveResult(int resultCode, Bundle resultData) {
				if (resultCode == RESULT_OK) {
					if (userLatLng != null && isInFront) {
						PokemonEncounter();
					}

				}
			}
		});

	}

	protected void PokemonEncounter() {

		try {
			boolean found = false;
			for (Zone zone : pokemonZones) {
				LatLng zoneLatLng = new LatLng(zone.getLat(), zone.getLong());
				if (Utilities.CalculationByDistance(userLatLng, zoneLatLng) < zone
						.getRadius()) {
					// In a zone
					List<Pokemon> pokemonByZone = getHelper()
							.lookupPokemonForZone(zone);
					double[] cumSum = new double[pokemonByZone.size() + 1];
					cumSum[0] = pokemonByZone.get(0).getRarity()
							.getProbability();
					for (int i = 1; i < cumSum.length - 1; i++)
						cumSum[i] = cumSum[i - 1]
								+ pokemonByZone.get(i).getRarity()
										.getProbability();

					cumSum[cumSum.length - 1] = 1.0;

					// Search Through cumulative density function
					Random rand = new Random();
					double key = rand.nextDouble();
					int size = cumSum.length;
					int index = -1;
					for (int i = 0; i < cumSum.length; i++) {
						if (key < cumSum[i]) {
							index = i;
							break;
						}
					}

					if (index < pokemonByZone.size()) { // ENCOUNTERED
						Pokemon pokemon = pokemonByZone.get(index);
						String message = "Wild pokemon " + pokemon.getName()
								+ " is attacking!";

						Fragment prev = getFragmentManager().findFragmentByTag(
								"dialog");
						if (prev == null) {// Only one dialog at a time
							// Create and show the dialog.
							PokemonAttackedDialog newFragment = PokemonAttackedDialog
									.newInstance(pokemon, currentUser);
							newFragment.setCancelable(false);
							newFragment.show(getFragmentManager(), "dialog");

						}

						Log.i("MainMapActivity", message);
					}
					Log.i("MainMapActivity", "In zone " + zone.getName());
					found = true;
					break;
				}
			}
			if (!found) {
				Log.i("MainMapActivity", "Not in zone");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void scheduleServiceAlarm() {
		Intent intent = new Intent(getApplicationContext(),
				ZoneCheckAlarm.class);
		intent.putExtra("receiver", zoneCheckReceiver);

		// Create a PendingIntent to be triggered when the alarm goes off
		final PendingIntent pIntent = PendingIntent.getBroadcast(this,
				ZoneCheckAlarm.REQUEST_CODE, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// Setup periodic alarm every 5 seconds
		long firstMillis = System.currentTimeMillis(); // first run of alarm is
														// immediate
		int intervalMillis = 10000; // 5 seconds
		AlarmManager alarm = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
				intervalMillis, pIntent);
	}

	private void updateValuesFromBundle(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			// Update the value of mRequestingLocationUpdates from the Bundle,
			// and
			// make sure that the Start Updates and Stop Updates buttons are
			// correctly enabled or disabled.
			if (savedInstanceState.keySet().contains(
					STATE_REQUESTING_LOCATION_UPDATES)) {
				mRequestingLocationUpdates = savedInstanceState
						.getBoolean(STATE_REQUESTING_LOCATION_UPDATES);
			}

			if (savedInstanceState.keySet().contains(STATE_RESOLVING_ERROR)) {
				mResolvingError = savedInstanceState
						.getBoolean(STATE_RESOLVING_ERROR);
			}

			// Update the value of mCurrentLocation from the Bundle and update
			// the
			// UI to show the correct latitude and longitude.
			if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
				// Since LOCATION_KEY was found in the Bundle, we can be sure
				// that
				// mCurrentLocationis not null.
				mLastLocation = savedInstanceState.getParcelable(LOCATION_KEY);
			}

		}
	}

	private ServiceConnection Scon = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder binder) {
			mServ = ((BackgroundSoundService.ServiceBinder) binder)
					.getService();
		}

		public void onServiceDisconnected(ComponentName name) {
			mServ = null;
		}
	};

	void doBindService() {
		bindService(new Intent(this, BackgroundSoundService.class), Scon,
				Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	void doUnbindService() {
		if (mIsBound) {
			unbindService(Scon);
			mIsBound = false;
		}
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	@Override
	public void onMapReady(final GoogleMap map) {

		map.setMyLocationEnabled(true);

		// Add Zones
		for (Zone zone : pokemonZones) {
			map.addCircle(new CircleOptions()
					.center(new LatLng(zone.getLat(), zone.getLong()))
					.radius(zone.getRadius())
					.strokeWidth(5)
					.strokeColor(
							getResources().getColor(
									zone.getType().getColourOutline()))
					.fillColor(
							getResources().getColor(
									zone.getType().getColourResource())));
		}

		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
			userLatLng = new LatLng(mLastLocation.getLatitude(),
					mLastLocation.getLongitude());

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(userLatLng).zoom(15).build();

			CameraUpdate update = CameraUpdateFactory
					.newCameraPosition(cameraPosition);
			map.animateCamera(update);

		}
		if (mRequestingLocationUpdates) {
			startLocationUpdates();
		}

	}

	public void createZones() {
		try {
			List<Zone> zones = zoneDao.queryForAll();
			for (Zone zone : zones) {
				pokemonZones.add(zone);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// PERIODIC UPDATES
	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(5000);
		mLocationRequest.setFastestInterval(3000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	@Override
	public void onLocationChanged(Location location) {
		mLastLocation = location;
		userLatLng = new LatLng(mLastLocation.getLatitude(),
				mLastLocation.getLongitude());

	}

	protected void startLocationUpdates() {
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	protected void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, this);
	}

	// CONNECTION FAILED
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mResolvingError) {
			// Already attempting to resolve an error.
			return;
		} else if (result.hasResolution()) {
			try {
				mResolvingError = true;
				result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
			} catch (SendIntentException e) {
				// There was an error with the resolution intent. Try again.
				mGoogleApiClient.connect();
			}
		} else {
			// Show dialog using GooglePlayServicesUtil.getErrorDialog()
			showErrorDialog(result.getErrorCode());
			mResolvingError = true;
		}

	}

	/* Creates a dialog for an error message */
	private void showErrorDialog(int errorCode) {
		// Create a fragment for the error dialog
		ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
		// Pass the error that should be displayed
		Bundle args = new Bundle();
		args.putInt(DIALOG_ERROR, errorCode);
		dialogFragment.setArguments(args);
		dialogFragment.show(getFragmentManager(), "errordialog");
	}

	/* Called from ErrorDialogFragment when the dialog is dismissed. */
	public void onDialogDismissed() {
		mResolvingError = false;
	}

	/* A fragment to display an error dialog */
	public static class ErrorDialogFragment extends DialogFragment {
		public ErrorDialogFragment() {
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Get the error code and retrieve the appropriate dialog
			int errorCode = this.getArguments().getInt(DIALOG_ERROR);
			return GooglePlayServicesUtil.getErrorDialog(errorCode,
					this.getActivity(), REQUEST_RESOLVE_ERROR);
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			((MainMapActivity) getActivity()).onDialogDismissed();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_RESOLVE_ERROR) {
			mResolvingError = false;
			if (resultCode == RESULT_OK) {
				// Make sure the app is not already connected or attempting to
				// connect
				if (!mGoogleApiClient.isConnecting()
						&& !mGoogleApiClient.isConnected()) {
					mGoogleApiClient.connect();
				}
			}
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mapFragment.getMapAsync(this);

	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

	private void setCurrentUser(User user) {
		currentUser = user;
		mDrawerUserName.setText(currentUser.getName());
		if (currentUser.getSex() == User.MALE) {
			mDrawerAvatar.setImageResource(R.drawable.trainer_male);
		} else {
			mDrawerAvatar.setImageResource(R.drawable.trainer_female);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
		outState.putBoolean(STATE_REQUESTING_LOCATION_UPDATES,
				mRequestingLocationUpdates);
		outState.putParcelable(LOCATION_KEY, mLastLocation);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!mResolvingError && mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
		if (mServ != null)
			mServ.resumeMusic();
	}

	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		if (mServ != null)
			mServ.pauseMusic();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/*
		 * You'll need this in your class to release the helper when done.
		 */
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
		if (mServ != null)
			mServ.stopMusic();
		doUnbindService();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isInFront = false;
		if (mGoogleApiClient.isConnected())
			stopLocationUpdates();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isInFront = true;
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()
				&& !mRequestingLocationUpdates) {
			startLocationUpdates();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {

			View drawerView = findViewById(R.id.leftDrawer); // child drawer
																// view

			if (!mDrawerLayout.isDrawerOpen(drawerView)) {
				mDrawerLayout.openDrawer(drawerView);
			} else if (mDrawerLayout.isDrawerOpen(drawerView)) {
				mDrawerLayout.closeDrawer(drawerView);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getFragmentManager();
		fm.popBackStack();
	}

	/******************************************************************************************************/
	// DRAWER

	/** Swaps fragments in the main content view */
	private void selectItemFromDrawer(int position) {

		if (position == 0) { // Collection
			List<Pokemon> userPokemon = null;
			try {
				userPokemon = getHelper().lookupPokemonForUser(currentUser);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			FragmentManager fragmentManager = getFragmentManager();
			Fragment prev = fragmentManager.findFragmentById(R.id.gridview);
			if (prev != null) {// Only one dialog at a time
				fragmentManager
						.beginTransaction()
						.replace(R.id.gridview,
								new PokedexFragment(userPokemon)).commit();
			} else {
				fragmentManager
						.beginTransaction()
						.add(R.id.mainMapLayout,
								new PokedexFragment(userPokemon))
						.addToBackStack("navfrag").commit();
			}

			setTitle(mNavItems.get(position).mTitle);

		} else if (position == 1) { // Items
			List<Item> userItems = null;
			try {
				userItems = getHelper().lookupItemsForUser(currentUser);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			ItemListFragment itemFrag = new ItemListFragment(userItems);
			FragmentManager fragmentManager = getFragmentManager();
			Fragment prev = fragmentManager.findFragmentById(R.id.gridview);
			if (prev != null) {// Only one dialog at a time
				fragmentManager.beginTransaction()
						.replace(R.id.gridview, itemFrag).commit();
			} else {
				fragmentManager.beginTransaction()
						.add(R.id.mainMapLayout, itemFrag)
						.addToBackStack("navfrag").commit();
			}

			setTitle(mNavItems.get(position).mTitle);

		}
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerPane);
	}

	class NavItem {
		String mTitle;
		String mSubtitle;
		int mIcon;

		public NavItem(String title, String subtitle, int icon) {
			mTitle = title;
			mSubtitle = subtitle;
			mIcon = icon;
		}
	}

	/*******************************************************************************************************/
	// Database

	// This is how, DatabaseHelper can be initialized for future use
	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}

	/*******************************************************************************************************/
	// New Game Dialog Stuff
	@Override
	public void onFinishNewGameDialog(String username, int sex) {
		User user = new User(username, sex);
		try {
			// This is the way to insert data into a database table
			user = userDao.createIfNotExists(user);

			// Get pokeball
			Item pokeball = itemDao
					.queryForEq(Item.NAME_FIELD_NAME, "Pokeball").get(0);

			// Check if user has pokeballs
			QueryBuilder<UserItem, Integer> queryBuilder = userItemDao
					.queryBuilder();
			Where<UserItem, Integer> where = queryBuilder.where();
			where.eq(UserItem.USER_ID_FIELD_NAME, user.getId());
			where.eq(UserItem.ITEM_ID_FIELD_NAME, pokeball.getId());
			where.and(2);
			UserItem ui = queryBuilder.queryForFirst();

			// Create if it does not exist
			if (ui == null) {
				// Automatically get 5 pokeballs
				for (int i = 0; i < 5; i++) {
					userItemDao.create(new UserItem(user, pokeball));
				}
			}

			userItemDao.update(ui);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Persist user id
		SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("user_id", user.getId());
		editor.commit();
		setCurrentUser(user);

		StarterPokemonDialog frag = new StarterPokemonDialog();
		frag.getDialog()
				.getWindow()
				.setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));

		frag.setCancelable(false);
		frag.show(getFragmentManager(), "dialog");
	}

	@Override
	public void onFinishStarterDialog(String name) {
		if (currentUser == null) {
			Log.e("pokemaps", "Current user is null?");
		}
		try {
			Log.i("pokemaps", name);
			QueryBuilder<Pokemon, Integer> queryBuilder = pokemonDao
					.queryBuilder();
			queryBuilder.where().eq("name", name);
			List<Pokemon> list = queryBuilder.query();
			UserPokemon userPoke = new UserPokemon(currentUser, list.get(0));

			// This is the way to insert data into a database table
			userPokeDao.create(userPoke);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
