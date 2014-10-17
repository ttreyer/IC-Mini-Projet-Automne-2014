package assignment;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRecommendation {
	private final int DEFAULT_LENGTH = 5;
	private final double DOUBLE_DIFF_DELTA = 0.01;

	static double[][] U = { { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 } };
	static double[][] V = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };

	static double[][] M = {
		{ 11,  0,  9,  8,  7 },
		{ 18,  0, 18, 18, 18 },
		{ 29, 28, 27,  0, 25 },
		{ 6,   6,  0,  6,  6 },
		{ 17, 16, 15, 14,  0 } };

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

	@Test
	public void testMultiplyMatrix() {
		double[][] mismatchA = { { 1, 2, 3 }, { 4, 5, 6 } };
		double[][] mismatchB = { { 2, 4, 6, 8 } };

		double[][] squareA = { { 0,  1, 2 }, {  3,  4,  5 }, {  6, 7, 8 } };
		double[][] squareB = { { 1, -7, 4 }, { -6, -3, -6 }, { -8, 5, 2 } };
		double[][] squareC = { { -22, 7, -2 }, { -61, -8, -2 }, { -100, -23, -2 } };

		double[][] rectA = { { 1.5, 2.4, 3.1 }, { 7.7, 8.9, 9.6 } };
		double[][] rectB = { { 0.2, 1.3 }, { 3.8, 4.9 }, { 6.1, 7.5 } };
		double[][] rectC = { { 28.33, 36.96 }, { 93.92, 125.62 } };

		/* Test matrice A invalide */
		assertNull("Erreur: devrait retourner null", Recommendation.multiplyMatrix(null, squareB));

		/* Test matrice B invalide */
		assertNull("Erreur: devrait retourner null", Recommendation.multiplyMatrix(squareA, null));

		/* Test avec matrice ne pouvant pas être multipliées */
		assertNull("Erreur: devrait retourner null", Recommendation.multiplyMatrix(mismatchA, mismatchB));

		/* Tests avec matrices carrées */
		double[][] testSquareC = Recommendation.multiplyMatrix(squareA, squareB);
		for (int i = 0, l = testSquareC.length; i < l; i++) {
			assertArrayEquals(squareC[i], testSquareC[i], DOUBLE_DIFF_DELTA);
		}

		/* Tests avec matrices rectangulaires */
		double[][] testRectC = Recommendation.multiplyMatrix(rectA, rectB);
		for (int i = 0, l = testRectC.length; i < l; i++) {
			assertArrayEquals(rectC[i], testRectC[i], DOUBLE_DIFF_DELTA);
		}
	}
	
	public void testUpdateUElem() {
		double[][] M1 = {
			{  0,  8,  9,  8,  7 },
			{ 18,  0, 18, 18, 18 },
			{ 29, 28, 27,  0, 25 },
			{ 6,   6,  0,  6,  6 },
			{ 17, 16, 15, 14,  0 } };

		/* Test avec M invalide */
		assertEquals(-1.0, Recommendation.updateUElem(null, U, V, 0, 0), DOUBLE_DIFF_DELTA);

		/* Test avec U invalide */
		assertEquals(-1.0, Recommendation.updateUElem(M, null, V, 0, 0), DOUBLE_DIFF_DELTA);

		/* Test avec V invalide */
		assertEquals(-1.0, Recommendation.updateUElem(M, U, null, 0, 0), DOUBLE_DIFF_DELTA);

		/* Test avec r invalide */
		assertEquals(-1.0, Recommendation.updateUElem(M, U, V, -10, 0), DOUBLE_DIFF_DELTA);
		assertEquals(-1.0, Recommendation.updateUElem(M, U, V, 100, 0), DOUBLE_DIFF_DELTA);

		/* Test avec s invalide */
		assertEquals(-1.0, Recommendation.updateUElem(M, U, V, 0, -10), DOUBLE_DIFF_DELTA);
		assertEquals(-1.0, Recommendation.updateUElem(M, U, V, 0, 100), DOUBLE_DIFF_DELTA);

		/* Test avec M */
		assertEquals(6.75, Recommendation.updateUElem(M, U, V, 0, 0), 0.001);

		/* Test avec M1 */
		assertEquals(6.0, Recommendation.updateUElem(M1, U, V, 0, 0), 0.001);
	}
}
