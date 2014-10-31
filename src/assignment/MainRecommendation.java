package assignment;

public class MainRecommendation extends Recommendation {
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

		double[][] P = multiplyMatrix(U, V);
		System.out.println(matrixToString(P));
		System.out.println(rmse(M,P));

		U = optimizeU(M, U, V);
		V = optimizeV(M, U, V);

		P = multiplyMatrix(U, V);
		System.out.println(rmse(M, P));

		int[] recommended = null;
		int error = 0;
		for (int j = 0; j < 200; j++) {
			recommended = recommend(M, 5);
			for(int i=0; i<recommended.length; ++i) {
				System.out.print(recommended[i]+" ");
			}
			System.out.println();
		}
	}
}
