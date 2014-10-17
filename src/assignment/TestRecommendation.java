package assignment;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRecommendation {
	private final int DEFAULT_LENGTH = 5;

	@Test
	public void testIsMatrix() {
		/* Test pour null */
		double[][] nullMatrix = null;
		assertFalse("Erreur: devrait retourner false", Recommendation.isMatrix(nullMatrix));

		/* Test pour matrice vide */
		double[][] emptyMatrix = new double[0][0];
		assertFalse("Erreur: devrait retourner false", Recommendation.isMatrix(emptyMatrix));

		/* Tests pour lignes ligne null */
		double[][] nullRowMatrix = new double[DEFAULT_LENGTH][];
		for (int i = 0; i < nullRowMatrix.length; i++) {
			assertFalse("Erreur: devrait retourner false", Recommendation.isMatrix(nullRowMatrix));

			nullRowMatrix[i] = new double[DEFAULT_LENGTH];
		}

		/* Test pour première ligne vide */
		double[][] emptyRowMatrix = new double[DEFAULT_LENGTH][];
		for (int i = 0; i < emptyRowMatrix.length; i++) {
			emptyRowMatrix[i] = new double[0];
			assertFalse("Erreur: devrait retourner false", Recommendation.isMatrix(emptyRowMatrix));

			emptyRowMatrix[i] = new double[DEFAULT_LENGTH];
		}

		double[][] validMatrix = {
			{ 1, 2, 3, 4 },
			{ 2, 4, 6, 8 },
			{ 5, 6, 7, 8 },
			{ 0, 2, 4, 6 } };
		assertTrue("Erreur: devrait retourner true", Recommendation.isMatrix(validMatrix));
	}

}
