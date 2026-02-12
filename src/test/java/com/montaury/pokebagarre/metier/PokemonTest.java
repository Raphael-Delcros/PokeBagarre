package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.fixtures.ConstructeurDePokemon;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/*
Cette classe effectue les tests à faire sur le fichier Pokemon.java

Tests effectués
– Pokemon 1 a plus d'attaque
- Pokemon 2 a plus d'attaque
- Pokemon 1 et 2 on attaque = mais pokémon 1 à plus de défense
- Pokemon 1 et 2 on attaque = mais pokémon 2 à plus de défense
- Pokemon 1 et 2 on attaque et défense = donc pokemon 1 gagne
 */

class PokemonTest {

    @Test
    void envoie_pokemon1_si_pokemon1_a_plus_de_force() {
        // GIVEN
        Pokemon pokemon1 = ConstructeurDePokemon.unPokemon().avecAttaque(50).construire();
        Pokemon pokemon2 = ConstructeurDePokemon.unPokemon().avecAttaque(25).construire();


        // WHEN
        boolean resultat_depuis_pokemon1 = pokemon1.estVainqueurContre(pokemon2);

        // THEN
        assertThat(resultat_depuis_pokemon1).isTrue();
    }

    @Test
    void envoie_pokemon2_si_pokemon2_a_plus_de_force() {
        // GIVEN
        Pokemon pokemon1 = ConstructeurDePokemon.unPokemon().avecAttaque(25).construire();
        Pokemon pokemon2 = ConstructeurDePokemon.unPokemon().avecAttaque(50).construire();
        
        // WHEN
        boolean resultat_depuis_pokemon1 = pokemon1.estVainqueurContre(pokemon2);

        // THEN
        assertThat(resultat_depuis_pokemon1).isFalse();
    }

    @Test
    void envoie_pokemon1_si_pokemons_ont_meme_force_mais_pokemon1_a_plus_de_defense() {
        // GIVEN
        Pokemon pokemon1 = ConstructeurDePokemon.unPokemon().avecAttaque(50).avecDefense(50).construire();
        Pokemon pokemon2 = ConstructeurDePokemon.unPokemon().avecAttaque(50).avecDefense(25).construire();

        // WHEN
        boolean resultat_depuis_pokemon1 = pokemon1.estVainqueurContre(pokemon2);

        // THEN
        assertThat(resultat_depuis_pokemon1).isTrue();
    }

    @Test
    void envoie_pokemon2_si_pokemons_ont_meme_force_mais_pokemon2_a_plus_de_defense() {
        // GIVEN
        Pokemon pokemon1 = ConstructeurDePokemon.unPokemon().avecAttaque(50).avecDefense(25).construire();
        Pokemon pokemon2 = ConstructeurDePokemon.unPokemon().avecAttaque(50).avecDefense(50).construire();

        // WHEN
        boolean resultat_depuis_pokemon1 = pokemon1.estVainqueurContre(pokemon2);

        // THEN
        assertThat(resultat_depuis_pokemon1).isFalse();
    }

    @Test
    void envoie_pokemon_mis_en_premier_si_stats_egales(){
        // GIVEN
        Pokemon pokemon1 = ConstructeurDePokemon.unPokemon().avecAttaque(50).avecDefense(50).construire();
        Pokemon pokemon2 = ConstructeurDePokemon.unPokemon().avecAttaque(50).avecDefense(50).construire();

        // WHEN
        boolean resultat_depuis_pokemon1 = pokemon1.estVainqueurContre(pokemon2);

        // THEN
        assertThat(resultat_depuis_pokemon1).isTrue();
    }
}