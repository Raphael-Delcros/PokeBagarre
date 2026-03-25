package com.montaury.pokebagarre.ui;

import java.util.concurrent.TimeUnit;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.awaitility.Awaitility.await;

/*
Classe de tests end2end:
- Nom du premier pokemon vide
- Nom du second pokemon vide
- Noms identiques
- Premier pokemon gagne la bagarre s'il gagne
- Deuxième pokemon gagne la bagarre s'il gagne
- Erreur de récupération API du premier pokemon
- Erreur de récupération API du second pokemon
 */


@ExtendWith(ApplicationExtension.class)
class PokeBagarreAppTest {
	private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1 = "#nomPokemon1";
	private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2 = "#nomPokemon2";
	private static final String IDENTIFIANT_BOUTON_BAGARRE = ".button";

	private static String getResultatBagarre(FxRobot robot) {
		return robot.lookup("#resultatBagarre").queryText().getText();
	}

	private static String getMessageErreur(FxRobot robot) {
		return robot.lookup("#resultatErreur").queryLabeled().getText();
	}

	@Start
	private void start(Stage stage) {
		new PokeBagarreApp().start(stage);
	}

	@Test
	void devrait_afficher_erreur_si_premier_champ_est_vide(FxRobot robot) {
		// WHEN
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
		robot.write(" ");
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
		robot.write("pikachu");
		robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);
		// THEN
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Le premier pokemon n'est pas renseigne");
		});
	}

	@Test
	void devrait_afficher_erreur_si_deuxieme_champ_est_vide(FxRobot robot) {
		// WHEN
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
		robot.write("pikachu");
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
		robot.write(" ");
		robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

		// THEN
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Le second pokemon n'est pas renseigne");
		});
	}

	@Test
	void devrait_afficher_erreur_si_les_pokemons_ont_le_meme_nom(FxRobot robot) {
		// WHEN
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
		robot.write("pikachu");
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
		robot.write("pikachu");
		robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

		// THEN
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Impossible de faire se bagarrer un pokemon avec lui-meme");
		});
	}

	@Test
	void devrait_annoncer_premier_pokemon_gagne_si_plus_fort(FxRobot robot) {
		// WHEN
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
		robot.write("arceus");
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
		robot.write("pikachu");
		robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

		// THEN
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(getResultatBagarre(robot)).isEqualTo("Le vainqueur est: arceus");
		});
	}

	@Test
	void devrait_annoncer_second_pokemon_gagne_si_plus_fort(FxRobot robot) {
		// WHEN
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
		robot.write("pikachu");
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
		robot.write("arceus");
		robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

		// THEN
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(getResultatBagarre(robot)).isEqualTo("Le vainqueur est: arceus");
		});
	}

	@Test
	void devrait_afficher_erreur_quand_premier_pokemon_inconnu(FxRobot robot) {
		// WHEN
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
		robot.write("inconnu");
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
		robot.write("pikachu");
		robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

		//THEN
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Impossible de recuperer les details sur 'inconnu'");
		});
	}

	@Test
	void devrait_afficher_erreur_quand_second_pokemon_inconnu(FxRobot robot) {
		// WHEN
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1);
		robot.write("pikachu");
		robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2);
		robot.write("inconnu");
		robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

		//THEN
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(getMessageErreur(robot)).isEqualTo("Erreur: Impossible de recuperer les details sur 'inconnu'");
		});
	}
}