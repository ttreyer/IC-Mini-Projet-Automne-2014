package assignment;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRecommendation {
	private final int DEFAULT_LENGTH = 5;
	private final double DOUBLE_DIFF_DELTA = 0.01;

	static double[][] U = { { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 } };
	static double[][] V = { { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1 } };

	static double[][] M = {
		{ 11,  0,  9,  8,  7 },
		{ 18,  0, 18, 18, 18 },
		{ 29, 28, 27,  0, 25 },
		{ 6,   6,  0,  6,  6 },
		{ 17, 16, 15, 14,  0 } };

	static double[][] M1 = {
		{  0,  8,  9,  8,  7 },
		{ 18,  0, 18, 18, 18 },
		{ 29, 28, 27,  0, 25 },
		{ 6,   6,  0,  6,  6 },
		{ 17, 16, 15, 14,  0 } };

	@Test
	public void testIsMatrix() {
		/* Test pour null */
		double[][] nullMatrix = null;
		assertFalse(Recommendation.isMatrix(nullMatrix));

		/* Test pour matrice vide */
		double[][] emptyMatrix = new double[0][0];
		assertFalse(Recommendation.isMatrix(emptyMatrix));

		/* Tests pour lignes ligne null */
		double[][] nullRowMatrix = new double[DEFAULT_LENGTH][];
		for (int i = 0; i < nullRowMatrix.length; i++) {
			assertFalse(Recommendation.isMatrix(nullRowMatrix));

			nullRowMatrix[i] = new double[DEFAULT_LENGTH];
		}

		/* Test pour première ligne vide */
		double[][] emptyRowMatrix = new double[DEFAULT_LENGTH][];
		for (int i = 0; i < emptyRowMatrix.length; i++) {
			emptyRowMatrix[i] = new double[0];
			assertFalse(Recommendation.isMatrix(emptyRowMatrix));

			emptyRowMatrix[i] = new double[DEFAULT_LENGTH];
		}

		double[][] validMatrix = {
			{ 1, 2, 3, 4 },
			{ 2, 4, 6, 8 },
			{ 5, 6, 7, 8 },
			{ 0, 2, 4, 6 } };
		assertTrue(Recommendation.isMatrix(validMatrix));
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
		assertNull(Recommendation.multiplyMatrix(null, squareB));

		/* Test matrice B invalide */
		assertNull(Recommendation.multiplyMatrix(squareA, null));

		/* Test avec matrice ne pouvant pas être multipliées */
		assertNull(Recommendation.multiplyMatrix(mismatchA, mismatchB));

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
	
	@Test
	public void testUpdateUElem() {
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
	
	@Test
	public void testUpdateVElem() {
		/* Test avec M invalide */
		assertEquals(-1.0, Recommendation.updateVElem(null, U, V, 0, 0), DOUBLE_DIFF_DELTA);

		/* Test avec U invalide */
		assertEquals(-1.0, Recommendation.updateVElem(M, null, V, 0, 0), DOUBLE_DIFF_DELTA);

		/* Test avec V invalide */
		assertEquals(-1.0, Recommendation.updateVElem(M, U, null, 0, 0), DOUBLE_DIFF_DELTA);

		/* Test avec r invalide */
		assertEquals(-1.0, Recommendation.updateVElem(M, U, V, -10, 0), DOUBLE_DIFF_DELTA);
		assertEquals(-1.0, Recommendation.updateVElem(M, U, V, 100, 0), DOUBLE_DIFF_DELTA);

		/* Test avec s invalide */
		assertEquals(-1.0, Recommendation.updateVElem(M, U, V, 0, -10), DOUBLE_DIFF_DELTA);
		assertEquals(-1.0, Recommendation.updateVElem(M, U, V, 0, 100), DOUBLE_DIFF_DELTA);

		/* Test avec M */
		assertEquals(7.1, Recommendation.updateVElem(M, U, V, 0, 0), 0.001);

		/* Test avec M1 */
		assertEquals(7.75, Recommendation.updateVElem(M1, U, V, 0, 0), 0.001);
	}
	
	@Test
	public void testMatrixToString() {
		double[][] testMatrix = {
			{11.0,0.0,9.0,8.0,7.0},
			{18.0,0.0,18.0,18.0,18.0},
			{29.0,28.0,27.0,0.0,25.0},
			{6.0,6.0,0.0,6.0,6.0},
			{17.0,16.0,15.0,14.0,0.0}
		};
		
		double[][] incorrectMatrix = {
			{5, 0, 0, 0},
			{1, 5, 6, 8, 9}
		};
		
		String correctMatrixString = "{\n"
				+ "\t{11.0,0.0,9.0,8.0,7.0},\n"
				+ "\t{18.0,0.0,18.0,18.0,18.0},\n"
				+ "\t{29.0,28.0,27.0,0.0,25.0},\n"
				+ "\t{6.0,6.0,0.0,6.0,6.0},\n"
				+ "\t{17.0,16.0,15.0,14.0,0.0}\n"
				+ "};";
		
		// Test avec matrice correcte
		assertEquals(correctMatrixString, Recommendation.matrixToString(testMatrix));
		
		// Test avec matrice incorrecte
		assertNull(Recommendation.matrixToString(incorrectMatrix));
	}
	
	@Test
	public void testCreateMatrix() {
		// Test que toutes les valeurs de l'interval spécifié sont respectées
		int minVal = -200;
		int maxVal = 80;
		boolean inRange = false;
		double[][] matrix = Recommendation.createMatrix(12, 14, minVal, maxVal);
		for(int i=0; i<matrix.length; ++i) {
			for(int j=0; j<matrix[i].length; ++j) {
				inRange = (matrix[i][j]>=minVal && matrix[i][j]<=maxVal);
				assertTrue(inRange);
			}
		}
		
		// Test matrice avec un range invalide
		assertNull(Recommendation.createMatrix(10, 10, 10, 5));
		
		// Test matrice avec dimension invalide
		assertNull(Recommendation.createMatrix(-20, 5, 5, 5));
	}
}
