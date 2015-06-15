package com.pokemaps.pokemaps.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pokemaps.pokemaps.R;
import com.pokemaps.pokemaps.Utilities;
import com.pokemaps.pokemaps.data.Pokemon.PokemonRarity;
import com.pokemaps.pokemaps.data.Pokemon.PokemonType;
import com.pokemaps.pokemaps.data.Zone.ZoneType;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private Context context;

	private static final String DATABASE_NAME = "pokemaps.db";
	private static final int DATABASE_VERSION = 3;

	private Dao<User, Integer> userDao = null;
	private Dao<Pokemon, Integer> pokemonDao = null;
	private Dao<UserPokemon, Integer> userPokemonDao = null;

	private Dao<Zone, Integer> zoneDao = null;
	private Dao<ZonePokemon, Integer> zonePokemonDao = null;

	private Dao<Item, Integer> itemDao = null;
	private Dao<UserItem, Integer> userItemDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	// DAOs
	public Dao<User, Integer> getUserDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(User.class);
		}
		return userDao;
	}

	public Dao<Pokemon, Integer> getPokemonDao() throws SQLException {
		if (pokemonDao == null) {
			pokemonDao = getDao(Pokemon.class);
		}
		return pokemonDao;
	}

	public Dao<UserPokemon, Integer> getUserPokemonDao() throws SQLException {
		if (userPokemonDao == null) {
			userPokemonDao = getDao(UserPokemon.class);
		}
		return userPokemonDao;
	}

	public Dao<Zone, Integer> getZoneDao() throws SQLException {
		if (zoneDao == null) {
			zoneDao = getDao(Zone.class);
		}
		return zoneDao;
	}

	public Dao<ZonePokemon, Integer> getZonePokemonDao() throws SQLException {
		if (zonePokemonDao == null) {
			zonePokemonDao = getDao(ZonePokemon.class);
		}
		return zonePokemonDao;
	}

	public Dao<Item, Integer> getItemDao() throws SQLException {
		if (itemDao == null) {
			itemDao = getDao(Item.class);
		}
		return itemDao;
	}

	public Dao<UserItem, Integer> getUserItemDao() throws SQLException {
		if (userItemDao == null) {
			userItemDao = getDao(UserItem.class);
		}
		return userItemDao;
	}

	private PreparedQuery<Pokemon> pokemonForUserQuery = null;
	private PreparedQuery<Item> itemsForUserQuery = null;
	private PreparedQuery<Pokemon> pokemonForZoneQuery = null;

	public List<Pokemon> lookupPokemonForUser(User user) throws SQLException {
		if (pokemonForUserQuery == null) {
			pokemonForUserQuery = makePokemonForUserQuery();
		}
		pokemonForUserQuery.setArgumentHolderValue(0, user);
		return pokemonDao.query(pokemonForUserQuery);
	}

	public List<Item> lookupItemsForUser(User user) throws SQLException {
		if (itemsForUserQuery == null) {
			itemsForUserQuery = makeItemForUserQuery();
		}
		itemsForUserQuery.setArgumentHolderValue(0, user);
		return itemDao.query(itemsForUserQuery);
	}

	public List<Pokemon> lookupPokemonForZone(Zone zone) throws SQLException {
		if (pokemonForZoneQuery == null) {
			pokemonForZoneQuery = makePokemonForZoneQuery();
		}
		pokemonForZoneQuery.setArgumentHolderValue(0, zone);
		return pokemonDao.query(pokemonForZoneQuery);
	}

	private PreparedQuery<Pokemon> makePokemonForUserQuery()
			throws SQLException {
		// build our inner query for UserPost objects
		QueryBuilder<UserPokemon, Integer> userPokeQb = getUserPokemonDao()
				.queryBuilder();
		// just select the post-id field
		userPokeQb.selectColumns(UserPokemon.POKEMON_ID_FIELD_NAME);
		SelectArg userSelectArg = new SelectArg();
		// you could also just pass in user1 here
		userPokeQb.where().eq(UserPokemon.USER_ID_FIELD_NAME, userSelectArg);

		// build our outer query for Post objects
		QueryBuilder<Pokemon, Integer> postQb = pokemonDao.queryBuilder();
		// where the id matches in the post-id from the inner query
		postQb.where().in(Pokemon.POKEMON_ID_FIELD_NAME, userPokeQb);
		return postQb.prepare();
	}

	private PreparedQuery<Item> makeItemForUserQuery() throws SQLException {
		// build our inner query for UserPost objects
		QueryBuilder<UserItem, Integer> userItemQb = getUserItemDao()
				.queryBuilder();
		// just select the post-id field
		userItemQb.selectColumns(UserItem.ITEM_ID_FIELD_NAME);
		SelectArg userSelectArg = new SelectArg();
		// you could also just pass in user here
		userItemQb.where().eq(UserItem.USER_ID_FIELD_NAME, userSelectArg);

		// build our outer query for Post objects
		QueryBuilder<Item, Integer> postQb = itemDao.queryBuilder();
		// where the id matches in the post-id from the inner query
		postQb.where().in(Item.ITEM_ID_FIELD_NAME, userItemQb);
		return postQb.prepare();
	}

	private PreparedQuery<Pokemon> makePokemonForZoneQuery()
			throws SQLException {
		// build our inner query for UserPost objects
		QueryBuilder<ZonePokemon, Integer> zonePokeQb = getZonePokemonDao()
				.queryBuilder();
		// just select the post-id field
		zonePokeQb.selectColumns(ZonePokemon.POKEMON_ID_FIELD_NAME);
		SelectArg zoneSelectArg = new SelectArg();
		// you could also just pass in user1 here
		zonePokeQb.where().eq(ZonePokemon.ZONE_ID_FIELD_NAME, zoneSelectArg);

		// build our outer query for Post objects
		QueryBuilder<Pokemon, Integer> postQb = pokemonDao.queryBuilder();
		// where the id matches in the post-id from the inner query
		postQb.where().in(Pokemon.POKEMON_ID_FIELD_NAME, zonePokeQb);
		return postQb.prepare();
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, Pokemon.class);
			TableUtils.createTable(connectionSource, UserPokemon.class);
			TableUtils.createTable(connectionSource, Zone.class);
			TableUtils.createTable(connectionSource, ZonePokemon.class);
			TableUtils.createTable(connectionSource, Item.class);
			TableUtils.createTable(connectionSource, UserItem.class);

			InstantiatePokemonDB();
			InstantiateItemDB();
			InstantiateZoneDB();

			Log.i(DatabaseHelper.class.getName(), "onCreate");
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases",
					e);
		}
	}

	private void InstantiateZoneDB() throws SQLException {
		List<Pokemon> pokemonCollection = pokemonDao.queryForAll();
		HashMap<String, Pokemon> pokemonBN = new HashMap<String, Pokemon>();
		for (Pokemon p : pokemonCollection)
			pokemonBN.put(p.getName(), p);

		HashMap<Integer, Zone> zonesBN = new HashMap<Integer, Zone>();

		try {
			AssetManager am = context.getAssets();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					am.open("ZoneConfig.txt")));
			String line = reader.readLine();

			int zone_id = 1;
			while (line != null) {
				if (line.equals("ZONE")) {
					line = reader.readLine(); // read zone line
					String[] tokens = line.split(",");
					String name = tokens[0];
					ZoneType type = Utilities.textToZoneType(tokens[1]);
					double lat = Double.parseDouble(tokens[2]);
					double lng = Double.parseDouble(tokens[3]);
					int radius = Integer.parseInt(tokens[4]);
					// Create the zone in the DB
					Zone zone = new Zone(name, type, lat, lng, radius);
					zonesBN.put(zone_id, zone);
					getZoneDao().create(zone);

					line = reader.readLine(); // read null or first pokemon line
					while (line != null && !line.equals("ZONE")) {
						String pokeName = line;
						getZonePokemonDao().create(
								new ZonePokemon(zonesBN.get(zone_id), pokemonBN
										.get(pokeName)));
						line = reader.readLine();
					}
					zone_id++;
				}
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		Log.i(DatabaseHelper.class.getName(), "ZONES: "
				+ getZoneDao().countOf());
		Log.i(DatabaseHelper.class.getName(), "ZONE POKEMON: "
				+ getZonePokemonDao().countOf());
	}

	private void InstantiateItemDB() throws SQLException {
		Item i = new Item("Pokeball", "Used for catching pokemon",
				R.drawable.pokeball);
		i.setExtraValue(255);
		getItemDao().create(i);
	}

	private void InstantiatePokemonDB() throws SQLException {
		try {
			AssetManager am = context.getAssets();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					am.open("PokemonConfig.txt")));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("\\|");
				int number = Integer.parseInt(tokens[0]);
				String name = tokens[1];
				String description = tokens[2];
				PokemonRarity rarity = Utilities.textToRarity(tokens[3]);
				int catchrate = Integer.parseInt(tokens[4]);
				PokemonType type1 = Utilities.textToPokemonType(tokens[5]);
				PokemonType type2 = null;
				if (tokens.length == 7) {
					type2 = Utilities.textToPokemonType(tokens[6]);
				}
				// Create the zone in the DB
				Pokemon poke = new Pokemon(number, name, description, rarity, catchrate,
						Utilities.IMAGES[number - 1],
						Utilities.GREY_IMAGES[number - 1]);
				poke.setType1(type1);
				poke.setType2(type2);
				getPokemonDao().create(poke);
			}

		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		// TODO: More Pokemon
		Log.i(DatabaseHelper.class.getName(), "POKEMONS:"
				+ getPokemonDao().countOf());

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.dropTable(connectionSource, Pokemon.class, true);
			TableUtils.dropTable(connectionSource, UserPokemon.class, true);
			TableUtils.dropTable(connectionSource, Zone.class, true);
			TableUtils.dropTable(connectionSource, ZonePokemon.class, true);
			TableUtils.dropTable(connectionSource, Item.class, true);
			TableUtils.dropTable(connectionSource, UserItem.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}

	}

}