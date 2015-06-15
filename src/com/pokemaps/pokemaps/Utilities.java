package com.pokemaps.pokemaps;

import java.util.Random;

import com.google.android.gms.maps.model.LatLng;
import com.pokemaps.pokemaps.data.Item;
import com.pokemaps.pokemaps.data.Pokemon;
import com.pokemaps.pokemaps.data.Pokemon.PokemonRarity;
import com.pokemaps.pokemaps.data.Pokemon.PokemonType;
import com.pokemaps.pokemaps.data.User;
import com.pokemaps.pokemaps.data.Zone.ZoneType;

public class Utilities {

	public static final int IMAGES[] = { R.drawable.bulbasaur,
			R.drawable.ivysaur, R.drawable.venusaur, R.drawable.charmander,
			R.drawable.charmeleon, R.drawable.charizard, R.drawable.squirtle,
			R.drawable.wartortle, R.drawable.blastoise, R.drawable.caterpie,
			R.drawable.metapod, R.drawable.butterfree, R.drawable.weedle,
			R.drawable.kakuna, R.drawable.beedrill, R.drawable.pidgey,
			R.drawable.pidgeotto, R.drawable.pidgeot, R.drawable.rattata,
			R.drawable.raticate, R.drawable.spearow, R.drawable.fearow,
			R.drawable.ekans, R.drawable.arbok, R.drawable.pikachu,
			R.drawable.raichu, R.drawable.sandshrew, R.drawable.sandslash,
			R.drawable.nidoranf, R.drawable.nidorina, R.drawable.nidoqueen,
			R.drawable.nidoranm, R.drawable.nidorino, R.drawable.nidoking,
			R.drawable.clefairy, R.drawable.clefable, R.drawable.vulpix,
			R.drawable.ninetails, R.drawable.jigglypuff, R.drawable.wigglytuff,
			R.drawable.zubat, R.drawable.golbat, R.drawable.oddish,
			R.drawable.gloom, R.drawable.vileplume, R.drawable.paras,
			R.drawable.parasect, R.drawable.venonat, R.drawable.venomoth,
			R.drawable.diglett, R.drawable.dugtrio, R.drawable.meowth,
			R.drawable.persian, R.drawable.psyduck, R.drawable.golduck,
			R.drawable.mankey, R.drawable.primeape, R.drawable.growlithe,
			R.drawable.arcanine, R.drawable.poliwag, R.drawable.poliwhirl,
			R.drawable.poliwrath, R.drawable.abra, R.drawable.kadabra,
			R.drawable.alakazam, R.drawable.machop, R.drawable.machoke,
			R.drawable.bellsprout, R.drawable.weepinbell, R.drawable.victreebel,
			R.drawable.tentacool, R.drawable.tentacruel, R.drawable.geodude,
			R.drawable.graveler, R.drawable.golem, R.drawable.ponyta,
			R.drawable.rapidash, R.drawable.slowpoke, R.drawable.slowbro,
			R.drawable.magnemite, R.drawable.magneton, R.drawable.farfetchd,
			R.drawable.doduo, R.drawable.dodrio, R.drawable.seel,
			R.drawable.dewgong, R.drawable.grimer, R.drawable.muk,
			R.drawable.shellder, R.drawable.cloyster, R.drawable.gastly,
			R.drawable.haunter, R.drawable.gengar, R.drawable.onix,
			R.drawable.drowzee, R.drawable.hypno, R.drawable.krabby,
			R.drawable.kingler, R.drawable.voltorb, R.drawable.electrode,
			R.drawable.exeggcute, R.drawable.exeggutor, R.drawable.cubone,
			R.drawable.marowak, R.drawable.hitmonlee, R.drawable.hitmonchan,
			R.drawable.lickitung, R.drawable.koffing, R.drawable.weezing,
			R.drawable.rhyhorn, R.drawable.rhydon, R.drawable.chansey,
			R.drawable.tangela, R.drawable.kangaskhan, R.drawable.horsea,
			R.drawable.seadra, R.drawable.goldeen, R.drawable.seaking,
			R.drawable.staryu, R.drawable.staryu, R.drawable.mr_mime,
			R.drawable.scyther, R.drawable.jynx, R.drawable.electabuzz,
			R.drawable.magmar, R.drawable.pinsir, R.drawable.tauros,
			R.drawable.magikarp, R.drawable.gyarados, R.drawable.lapras,
			R.drawable.ditto, R.drawable.eevee, R.drawable.vaporean,
			R.drawable.jolteon, R.drawable.flareon, R.drawable.porygon,
			R.drawable.omanyte, R.drawable.omastar, R.drawable.kabuto,
			R.drawable.kabutops, R.drawable.aerodactyl, R.drawable.snorlax,
			R.drawable.articuno, R.drawable.zapdos, R.drawable.moltres,
			R.drawable.dratini, R.drawable.dragonair, R.drawable.dragonite,
			R.drawable.mewtwo, R.drawable.mew };

	public static final int GREY_IMAGES[] = { R.drawable.bulbasaur_grey,
		R.drawable.ivysaur_grey, R.drawable.venusaur_grey, R.drawable.charmander_grey,
		R.drawable.charmeleon_grey, R.drawable.charizard_grey, R.drawable.squirtle_grey,
		R.drawable.wartortle_grey, R.drawable.blastoise_grey, R.drawable.caterpie_grey,
		R.drawable.metapod_grey, R.drawable.butterfree_grey, R.drawable.weedle_grey,
		R.drawable.kakuna_grey, R.drawable.beedrill_grey, R.drawable.pidgey_grey,
		R.drawable.pidgeotto_grey, R.drawable.pidgeot_grey, R.drawable.rattata_grey,
		R.drawable.raticate_grey, R.drawable.spearow_grey, R.drawable.fearow_grey,
		R.drawable.ekans_grey, R.drawable.arbok_grey, R.drawable.pikachu_grey,
		R.drawable.raichu_grey, R.drawable.sandshrew_grey, R.drawable.sandslash_grey,
		R.drawable.nidoranf_grey, R.drawable.nidorina_grey, R.drawable.nidoqueen_grey,
		R.drawable.nidoranm_grey, R.drawable.nidorino_grey, R.drawable.nidoking_grey,
		R.drawable.clefairy_grey, R.drawable.clefable_grey, R.drawable.vulpix_grey,
		R.drawable.ninetails_grey, R.drawable.jigglypuff_grey, R.drawable.wigglytuff_grey,
		R.drawable.zubat_grey, R.drawable.golbat_grey, R.drawable.oddish_grey,
		R.drawable.gloom_grey, R.drawable.vileplume_grey, R.drawable.paras_grey,
		R.drawable.parasect_grey, R.drawable.venonat_grey, R.drawable.venomoth_grey,
		R.drawable.diglett_grey, R.drawable.dugtrio_grey, R.drawable.meowth_grey,
		R.drawable.persian_grey, R.drawable.psyduck_grey, R.drawable.golduck_grey,
		R.drawable.mankey_grey, R.drawable.primeape_grey, R.drawable.growlithe_grey,
		R.drawable.arcanine_grey, R.drawable.poliwag_grey, R.drawable.poliwhirl_grey,
		R.drawable.poliwrath_grey, R.drawable.abra_grey, R.drawable.kadabra_grey,
		R.drawable.alakazam_grey, R.drawable.machop_grey, R.drawable.machoke_grey,
		R.drawable.bellsprout_grey, R.drawable.weepinbell, R.drawable.victreebel_grey,
		R.drawable.tentacool_grey, R.drawable.tentacruel_grey, R.drawable.geodude_grey,
		R.drawable.graveler_grey, R.drawable.golem_grey, R.drawable.ponyta_grey,
		R.drawable.rapidash_grey, R.drawable.slowpoke_grey, R.drawable.slowbro_grey,
		R.drawable.magnemite_grey, R.drawable.magneton_grey, R.drawable.farfetchd_grey,
		R.drawable.doduo_grey, R.drawable.dodrio_grey, R.drawable.seel_grey,
		R.drawable.dewgong_grey, R.drawable.grimer_grey, R.drawable.muk_grey,
		R.drawable.shellder_grey, R.drawable.cloyster_grey, R.drawable.gastly_grey,
		R.drawable.haunter_grey, R.drawable.gengar_grey, R.drawable.onix_grey,
		R.drawable.drowzee_grey, R.drawable.hypno_grey, R.drawable.krabby_grey,
		R.drawable.kingler_grey, R.drawable.voltorb_grey, R.drawable.electrode_grey,
		R.drawable.exeggcute_grey, R.drawable.exeggutor_grey, R.drawable.cubone_grey,
		R.drawable.marowak_grey, R.drawable.hitmonlee_grey, R.drawable.hitmonchan_grey,
		R.drawable.lickitung_grey, R.drawable.koffing_grey, R.drawable.weezing_grey,
		R.drawable.rhyhorn_grey, R.drawable.rhydon_grey, R.drawable.chansey_grey,
		R.drawable.tangela_grey, R.drawable.kangaskhan_grey, R.drawable.horsea_grey,
		R.drawable.seadra_grey, R.drawable.goldeen_grey, R.drawable.seaking_grey,
		R.drawable.staryu_grey, R.drawable.staryu_grey, R.drawable.mr_mime_grey,
		R.drawable.scyther_grey, R.drawable.jynx_grey, R.drawable.electabuzz_grey,
		R.drawable.magmar_grey, R.drawable.pinsir_grey, R.drawable.tauros_grey,
		R.drawable.magikarp_grey, R.drawable.gyarados_grey, R.drawable.lapras_grey,
		R.drawable.ditto_grey, R.drawable.eevee_grey, R.drawable.vaporean_grey,
		R.drawable.jolteon_grey, R.drawable.flareon_grey, R.drawable.porygon_grey,
		R.drawable.omanyte_grey, R.drawable.omastar_grey, R.drawable.kabuto_grey,
		R.drawable.kabutops_grey, R.drawable.aerodactyl_grey, R.drawable.snorlax_grey,
		R.drawable.articuno_grey, R.drawable.zapdos_grey, R.drawable.moltres_grey,
		R.drawable.dratini_grey, R.drawable.dragonair_grey, R.drawable.dragonite_grey,
		R.drawable.mewtwo_grey, R.drawable.mew_grey };

	
	public static double CalculationByDistance(LatLng StartP, LatLng EndP) {
		double lat1 = StartP.latitude;
		double lat2 = EndP.latitude;
		double lon1 = StartP.longitude;
		double lon2 = EndP.longitude;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.asin(Math.sqrt(a));

		return 6366000 * c;
	}

	public static ZoneType textToZoneType(String text) {
		if (text.equals("GRASSLAND")) {
			return ZoneType.GRASSLAND;
		}
		if (text.equals("WATER")) {
			return ZoneType.WATER;
		}
		if (text.equals("MOUNTAIN")) {
			return ZoneType.MOUNTAIN;
		}
		if (text.equals("SEA")) {
			return ZoneType.SEA;
		}
		if (text.equals("ROUGH")) {
			return ZoneType.ROUGH;
		}
		if (text.equals("URBAN")) {
			return ZoneType.URBAN;
		}
		if (text.equals("CAVE")) {
			return ZoneType.CAVE;
		}
		if (text.equals("FOREST")) {
			return ZoneType.FOREST;
		}

		return null;
	}

	public static PokemonType textToPokemonType(String text) {
		if (text.equals("GRASS")) {
			return PokemonType.GRASS;
		}
		if (text.equals("POISON")) {
			return PokemonType.POISON;
		}
		if (text.equals("FIRE")) {
			return PokemonType.FIRE;
		}
		if (text.equals("WATER")) {
			return PokemonType.WATER;
		}
		if (text.equals("BUG")) {
			return PokemonType.BUG;
		}
		if (text.equals("FLYING")) {
			return PokemonType.FLYING;
		}
		if (text.equals("NORMAL")) {
			return PokemonType.NORMAL;
		}
		if (text.equals("ELECTRIC")) {
			return PokemonType.ELECTRIC;
		}
		if (text.equals("GROUND")) {
			return PokemonType.GROUND;
		}
		if (text.equals("PSYCIC")) {
			return PokemonType.PSYCIC;
		}
		if (text.equals("FIGHTING")) {
			return PokemonType.FIGHTING;
		}
		if (text.equals("STEEL")) {
			return PokemonType.STEEL;
		}
		if (text.equals("GHOST")) {
			return PokemonType.GHOST;
		}
		if (text.equals("ICE")) {
			return PokemonType.ICE;
		}
		if (text.equals("DRAGON")) {
			return PokemonType.DRAGON;
		}
		return null;
	}

	public static PokemonRarity textToRarity(String text) {
		if (text.equals("VERYCOMMON")) {
			return PokemonRarity.VERYCOMMON;
		}
		if (text.equals("COMMON")) {
			return PokemonRarity.COMMON;
		}
		if (text.equals("UNCOMMON")) {
			return PokemonRarity.UNCOMMON;
		}
		if (text.equals("RARE")) {
			return PokemonRarity.RARE;
		}
		if (text.equals("VERYRARE")) {
			return PokemonRarity.VERYRARE;
		}
		return null;
	}

	public static boolean calculate_catch(User currentUser, Pokemon pokemon,
			Item pokeball) {

		Random rand = new Random();
		// Assume pokeball
		int N = rand.nextInt(pokeball.getExtraValue());

		if (N > pokemon.getCatchRate()) { // Breaks free!
			return false;
		} else {
			return true;
		}
	}
}
