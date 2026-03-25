package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.erreurs.ErreurRecuperationPokemon;
import com.montaury.pokebagarre.fixtures.ConstructeurDePokemon;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/*
Cette classe fait les tests avec la classe Bagarre

Tests effectués :
– Premier pokemon est null
– Premier pokemon est vide
– Deuxième pokemon est null
– Deuxième pokemon est vide
– Les pokemons ont le même nom
– Le premier pokemon gagne s'il est le plus puissant
– Le deuxième pokemon gagne s'il est le plus puissant

 */

class BagarreTest {

	private PokeBuildApi fausseApi;
	private Bagarre bagarre;

	@Test
	public void genere_une_erreur_lorsque_premier_pokemon_est_null() {
		// GIVEN
		final Bagarre maBagarre = new Bagarre();

		// WHEN
		final Throwable thrown = catchThrowable(() -> maBagarre.demarrer(null, "Mewto"));

		// THEN
		assertThat(thrown).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le premier pokemon n'est pas renseigne");
	}

	@Test
	public void genere_une_erreur_lorsque_premier_pokemon_est_vide() {
		// GIVEN
		final Bagarre maBagarre = new Bagarre();

		// WHEN
		final Throwable thrown = catchThrowable(() -> maBagarre.demarrer("", "Mewto"));

		// THEN
		assertThat(thrown).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le premier pokemon n'est pas renseigne");
	}

	@Test
	public void genere_une_erreur_lorsque_deuxieme_pokemon_est_null() {
		// GIVEN
		final Bagarre maBagarre = new Bagarre();

		// WHEN
		final Throwable thrown = catchThrowable(() -> maBagarre.demarrer("Mewto", null));

		// THEN
		assertThat(thrown).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le second pokemon n'est pas renseigne");
	}

	@Test
	public void genere_une_erreur_lorsque_deuxieme_pokemon_est_vide() {
		// GIVEN
		final Bagarre maBagarre = new Bagarre();

		// WHEN
		final Throwable thrown = catchThrowable(() -> maBagarre.demarrer("Mewto", ""));

		// THEN
		assertThat(thrown).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le second pokemon n'est pas renseigne");
	}

	@Test
	void genere_une_erreur_si_pokemons_sont_egaux() {
		// GIVEN
		final Bagarre maBagarre = new Bagarre();

		// WHEN
		final Throwable thrown = catchThrowable(() -> maBagarre.demarrer("Mewto", "Mewto"));

		// THEN
		assertThat(thrown).isInstanceOf(ErreurMemePokemon.class).hasMessage("Impossible de faire se bagarrer un pokemon avec lui-meme");

	}

	@Test
	void verifier_api_avec_premier_pokemon() {
		// GIVEN
		Mockito.when(fausseApi.recupererParNom("inconnu"))
				.thenReturn(CompletableFuture
						.failedFuture(new ErreurRecuperationPokemon("inconnu")));
		Mockito.when(fausseApi.recupererParNom("pikachu"))
				.thenReturn(CompletableFuture
						.completedFuture(new Pokemon("pikachu", "url1",
								new Stats(1, 2))));

		// WHEN
			var futurVainqueur = bagarre.demarrer("inconnu", "pikachu");

		// THEN
		assertThat(futurVainqueur)
				.failsWithin(Duration.ofSeconds(2))
				.withThrowableOfType(ExecutionException.class)
				.havingCause()
				.isInstanceOf(ErreurRecuperationPokemon.class);

	}

	@Test
	void verifier_api_avec_deuxieme_pokemon() {
		// GIVEN
		Mockito.when(fausseApi.recupererParNom("pikachu"))
				.thenReturn(CompletableFuture
						.completedFuture(new Pokemon("pikachu", "url1",
								new Stats(1, 2))));

		Mockito.when(fausseApi.recupererParNom("inconnu"))
				.thenReturn(CompletableFuture
						.failedFuture(new ErreurRecuperationPokemon("inconnu")));

		// WHEN
		var futurVainqueur = bagarre.demarrer("pikachu", "inconnu");

		// THEN
		assertThat(futurVainqueur)
				.failsWithin(Duration.ofSeconds(2))
				.withThrowableOfType(ExecutionException.class)
				.havingCause()
				.isInstanceOf(ErreurRecuperationPokemon.class);

	}

	@Test
	void devrait_retourner_le_premier_pokemon_s_il_est_vainqueur(){
		// GIVEN
		Mockito.when(fausseApi.recupererParNom("pikachu"))
				.thenReturn(CompletableFuture
						.completedFuture(new Pokemon("pikachu", "url1",
								new Stats(3, 3))));

		Mockito.when(fausseApi.recupererParNom("dracaufeu"))
				.thenReturn(CompletableFuture
						.completedFuture(new Pokemon("dracaufeu", "url2",
								new Stats(1, 2))));

		// WHEN
		var futurVainqueur = bagarre.demarrer("pikachu", "dracaufeu");

		// THEN
		assertThat(futurVainqueur)
				.succeedsWithin(Duration.ofSeconds(2))
				.satisfies (pokemon -> {
							assertThat(pokemon.getNom())
									.isEqualTo("pikachu");
					assertThat(pokemon.getUrlImage()).isEqualTo("url1");
					assertThat(pokemon.getStats().getDefense()).isEqualTo(3);
					assertThat(pokemon.getStats().getAttaque()).isEqualTo(3);
						}
				) ;
	}

	@Test
	void devrait_retourner_le_deuxieme_pokemon_s_il_est_vainqueur(){
		// GIVEN
		Mockito.when(fausseApi.recupererParNom("pikachu"))
				.thenReturn(CompletableFuture
						.completedFuture(new Pokemon("pikachu", "url1",
								new Stats(3, 3))));

		Mockito.when(fausseApi.recupererParNom("dracaufeu"))
				.thenReturn(CompletableFuture
						.completedFuture(new Pokemon("dracaufeu", "url2",
								new Stats(100, 200))));

		// WHEN
		var futurVainqueur = bagarre.demarrer("pikachu", "dracaufeu");

		// THEN
		assertThat(futurVainqueur)
				.succeedsWithin(Duration.ofSeconds(2))
				.satisfies (pokemon -> {
							assertThat(pokemon.getNom())
									.isEqualTo("dracaufeu");
							assertThat(pokemon.getUrlImage()).isEqualTo("url2");
							assertThat(pokemon.getStats().getDefense()).isEqualTo(200);
							assertThat(pokemon.getStats().getAttaque()).isEqualTo(100);
						}
				) ;
	}

	@BeforeEach
	public void preparer() {
		fausseApi = mock(PokeBuildApi.class);
		bagarre = new Bagarre(this.fausseApi);
	}

}
