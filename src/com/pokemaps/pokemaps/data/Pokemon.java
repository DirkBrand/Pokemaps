package com.pokemaps.pokemaps.data;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.pokemaps.pokemaps.R;

public class Pokemon implements Serializable {
	
	/**
	 * Model Class for Pokemon database table
	 */
	private static final long serialVersionUID = -8755734209725555655L;
	public final static String POKEMON_ID_FIELD_NAME = "pokemon_id";
	public final static String NAME_FIELD_NAME = "name";
	public final static String DESCRIPTION_FIELD_NAME = "description";
	public final static String NUMBER_FIELD_NAME = "number";
	public final static String RARITY_FIELD_NAME = "rarity";
	public final static String CATCH_RATE_FIELD_NAME = "catch_rate";
	public final static String TYPE_1_FIELD_NAME = "type1";	
	public final static String TYPE_2_FIELD_NAME = "type2";	
	public final static String IMAGE_ID_FIELD_NAME = "image_id";
	public final static String GREY_IMAGE_ID_FIELD_NAME = "grey_image_id";

	@DatabaseField(generatedId = true, columnName = POKEMON_ID_FIELD_NAME)
	private long id;
	
	@DatabaseField(columnName = NAME_FIELD_NAME)
	private String name;	
	

	@DatabaseField(columnName = DESCRIPTION_FIELD_NAME)
	private String description;	
	
	@DatabaseField(columnName = NUMBER_FIELD_NAME)
	private int number;
	
	@DatabaseField(columnName = RARITY_FIELD_NAME)
	private PokemonRarity rarity;

	@DatabaseField(columnName = CATCH_RATE_FIELD_NAME)
	private int catch_rate;
	
	@DatabaseField(columnName = TYPE_1_FIELD_NAME)
	private PokemonType type1;
	
	@DatabaseField(columnName = TYPE_2_FIELD_NAME)
	private PokemonType type2;

	@DatabaseField(columnName = IMAGE_ID_FIELD_NAME)
	private int image_id;

	@DatabaseField(columnName = GREY_IMAGE_ID_FIELD_NAME)
	private int grey_image_id;
	

	public enum PokemonType{
		GRASS(1), POISON(2), FIRE(3), WATER(4), BUG(5), FLYING(6), NORMAL(7), GROUND(8), PSYCIC(9), ELECTRIC(10),FIGHTING(11),STEEL(12),GHOST(13),ICE(14),DRAGON(15);
		int value;
		private PokemonType(int val) {
			this.value = val;
		}
		public int getDrawable() {
			switch(this) {
			case GRASS:
				return R.drawable.grass_shape;
			case POISON:
				return R.drawable.poison_shape;
			case FIRE:
				return R.drawable.fire_shape;
			case WATER:
				return R.drawable.water_shape;
			case BUG:
				return R.drawable.bug_shape;
			case FLYING:
				return R.drawable.flying_shape;
			case NORMAL:
				return R.drawable.normal_shape;
			case GROUND:
				return R.drawable.ground_shape;
			case PSYCIC:
				return R.drawable.psychic_shape;
			case ELECTRIC:
				return R.drawable.electric_shape;
			case FIGHTING:
				return R.drawable.fighting_shape;
			case STEEL:
				return R.drawable.steel_shape;
			case GHOST:
				return R.drawable.ghost_shape;
			case ICE:
				return R.drawable.ice_shape;
			case DRAGON:
				return R.drawable.dragon_shape;
			}
			return 0;
		}

	}
	
	public enum PokemonRarity{
		VERYCOMMON(1), COMMON(2), UNCOMMON(3), RARE(4), VERYRARE(5);
		int value;
		private PokemonRarity(int val) {
			this.value = val;
		}
		public double getProbability() {
			switch(this) {
			case VERYCOMMON:
				return 10/187.0;
			case COMMON:
				return 8.5/187.0;
			case UNCOMMON:
				return 6.75/187.0;
			case RARE:
				return 3.33/187.0;
			case VERYRARE:
				return 1.25/187.0;
			default:
				return 0.0;
			}
		}
	}
	
	public Pokemon() {}
	
	public Pokemon (int number, String name, String description, PokemonRarity rarity, int catch_rate, int imageId, int grey_image_id) {
		this.number = number;
		this.name = name;
		this.description = description;
		this.catch_rate = catch_rate;
		this.rarity = rarity;
		this.image_id = imageId;
		this.grey_image_id = grey_image_id;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getImageId() {
		return image_id;
	}

	public void setImageId(int id) {
		this.image_id = id;
	}
	
	public int getGreyImageId() {
		return grey_image_id;
	}

	public void setGreyImageId(int id) {
		this.grey_image_id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getCatchRate() {
		return catch_rate;
	}

	public PokemonType getType1() {
		return type1;
	}

	public PokemonType getType2() {
		return type2;
	}
	public void setCatchRate(int catch_rate) {
		this.catch_rate = catch_rate;
	}
	
	public PokemonRarity getRarity() {
		return rarity;
	}
	
	public void setType1(PokemonType type) {
		this.type1 = type;
	}

	public void setType2(PokemonType type) {
		this.type2 = type;
	}
	

	public void setName(PokemonRarity rarity) {
		this.rarity = rarity;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name;
	}
}
