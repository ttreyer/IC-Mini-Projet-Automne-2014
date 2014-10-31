package assignment;

public class MainRecommendation extends Recommendation {
	public static void printRecommend (int[] recommend) {
		for (int i = 0; i < recommend.length; i++) {
			System.out.format("%d ", recommend[i]);
		}

		System.out.println();
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

		M = createMatrix(100, 200, 1, 9, 0.8);

		for (int n = 0; n < 1; n++) {
			int[] recommended = recommend(M, 20);
			System.out.print("# ");
			printRecommend(recommended);
		}
	}
}
