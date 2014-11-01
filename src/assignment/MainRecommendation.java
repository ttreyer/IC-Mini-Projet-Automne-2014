package assignment;

public class MainRecommendation extends Recommendation {
	public static void printRecommend (int[] recommend) {
		for (int i = 0; i < recommend.length; i++) {
			System.out.format("%d ", recommend[i]);
		}

		System.out.println();
	}

	public static double[][] createEasyMatrix (int n, int m, int d, double r) {
		double[][] U = createMatrix(n, d, 0.5, 1);
		double[][] V = createMatrix(d, m, 0.5, 1);
		double[][] M = null;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < d; j++) {
				U[i][j] = Math.round(U[i][j] * 100.0) / 100.0;
			}
		}

		for (int i = 0; i < d; i++) {
			for (int j = 0; j < m; j++) {
				V[i][j] = Math.round(V[i][j] * 100.0) / 100.0;
			}
		}

		M = multiplyMatrix(U, V);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (random.nextDouble() >= r) {
					M[i][j] = 0;
				}
			}
		}

		return M;
	}

	public static void main (String[] args) {
		double[][] M={
			{ 3.0, 7.0, 6.0, 7.0, 6.0, 4.0},
			{ 3.0, 13.0, 10.0, 13.0, 12.0, 6.0},
			{ 5.0, 0.0, 0.0, 0.0, 0.0, 5.0},
			{ 6.0, 12.0, 7.0, 0.0, 0.0, 8.0},
			{ 4.0, 9.0, 0.0, 0.0, 6.0, 5.0}
		};

		double[][] U={
			{ 1.0, 2.0, 3.0},
			{ 2.0, 2.0, 1.0},
			{ 3.0, 2.0, 1.0},
			{ 2.0, 3.0, 2.0},
			{ 2.0, 2.0, 2.0}
		};

		double[][] V={
			{ 2.0, 0.0, 3.0, 3.0, 2.0, 2.0},
			{ 3.0, 3.0, 2.0, 2.0, 2.0, 3.0},
			{ 1.0, 1.0, 0.0, 1.0, 1.0, 0.0}
		};

		M = createEasyMatrix(50, 50, 10, 0.6);

		for (int n = 0; n < 1; n++) {
			int[] recommended = recommend(M, 10);
			System.out.print("# ");
			printRecommend(recommended);
		}
	}
}
