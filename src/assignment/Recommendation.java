package assignment;

import java.util.Random;

public class Recommendation {

	/*
	 * Inscrivez votre nom complet (prénom et nom de famille)
	 * ainsi que votre numéro sciper ci-dessous :
	 */
	
	/* Etudiant 1 */
	public static String NAME1 = "Dominique Roduit";
	public static int SCIPER1 = 234868;
	
	/* Etudiant 2 - laissez tel quel si vous avez codé le projet tout seul */
	public static String NAME2 = "Thierry Treyer";
	public static int SCIPER2 = 235116;
	
	static Random random = new Random();
	
	public static String matrixToString(double[][] A) {
		/* TODO: Dominique */
		/* Méthode à coder */		
		return "";
	}
	
	public static boolean isMatrix( double[][] A ) {
		/* TODO: Thierry */

		/* A n'est pas null */
		if (A == null) {
			return false;
		}

		/* A n'est pas vide */
		if (A.length == 0) {
			return false;
		}

		/* La première ligne de A n'est pas null */
		if (A[0] == null) {
			return false;
		}

		/* La première ligne de A n'est pas vide */
		if (A[0].length == 0) {
			return false;
		}

		/* Les lignes de A ne sont pas null et ont la même taille */
		int length = A[0].length;
		for (int i = 1, l = A.length; i < l; i++) {
			if (A[i] == null) {
				return false;
			}

			if (A[i].length != length) {
				return false;
			}
		}

		return true;
	}
	
	public static double[][] multiplyMatrix(double[][] A, double[][] B) {
		/* TODO: Thierry */

		/* Matrices A et B valides */
		if (isMatrix(A) == false || isMatrix(B) == false) {
			return null;
		}

		/* Matrices de taille compatibles */
		if (A[0].length != B.length) {
			return null;
		}

		int n = A.length; /* Nombre de lignes de la matrice finale */
		int m = B[0].length; /* Nombre de colonnes de la matrice finale */
		int p = A[0].length; /* Longueur d'une multiplication */
		double[][] C = new double[n][m];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				for (int k = 0; k < p; k++) {
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}

		return C;	
	}
	
	public static double[][] createMatrix( int n, int m, int k, int l) {
		/* TODO: Dominique */
		/* Méthode à coder */
		/* Utilisez la variable 'random', par exemple */
		int tmp = random.nextInt(5);
		return null;
	}
	
	public static double rmse(double[][] M, double[][] P) {
		/* TODO: Dominique */
		/* Méthode à coder */	
		return 0;
	}
	
	public static double updateUElem( double[][] M, double[][] U, double[][] V, int r, int s ) {
		/* TODO: Thierry */
		/* Méthode à coder */	
		return 0;
	}
	
	public static double updateVElem( double[][] M, double[][] U, double[][] V, int r, int s ) {
		/* TODO: Thierry */
		/* Méthode à coder */	
		return 0;		
	}
	
	public static double[][] optimizeU( double[][] M, double[][] U, double[][] V) {
		/* TODO: Thierry */
		/* Méthode à coder */	
		return null;		
	}

	public static double[][] optimizeV( double[][] M, double[][] U, double[][] V) {
		/* TODO: Thierry */
		/* Méthode à coder */	
		return null;		
	}
	
	public static int[] recommend( double[][] M, int d) {
		/* TODO: Dominique */
		/* Méthode à coder */	
		return null;
	}
}


