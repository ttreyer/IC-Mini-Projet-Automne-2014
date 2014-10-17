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
	
	/** 
	 * Converti une matrice en chaine de caractère
	 * @param A Matrice à convertir
	 * @return La matrice passée en argument sous forme de chaine de caractère
	 */
	public static String matrixToString(double[][] A) {
		/* TODO: Dominique */
		/* Méthode à coder */	

		// Chaine contenant la matrice
		String SMatrix = "{\n";
		for(int i=0; i<A.length; ++i) {
			SMatrix += "  {";
			for(int j=0; j<A[i].length; ++j) {
				SMatrix += Double.toString(A[i][j]);
				if(j!=A[i].length-1) SMatrix += ",";
			}
			SMatrix += "}";
			if(i!=A.length-1) SMatrix += ",";
			SMatrix += "\n";
		}
		SMatrix += "};";
		
		
		return SMatrix;
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
	
	/**
	 * Créer une matrice a partir d'une taille donnée et d'une plage de valeur
	 * aléatoire
	 * @param n Lignes 
	 * @param m Colonnes
	 * @param k Borne inférieure du randomize
	 * @param l Borne supérieure du randomize
	 * @return Matrice nxm contenant des nombres réels générés aléatoirements
	 */
	public static double[][] createMatrix( int n, int m, int k, int l) {
		/* TODO: Dominique */
		double randValue;
		
		if(m==0 && n==0 || k>l) return null;
		
		double[][] matrice = new double[n][m];
		
		for(int ligne=0; ligne<n; ++ligne) {
			for(int c=0; c<m; ++c) {
				// Génération d'une valeur réelle aléatoire comprise entre k et l
				randValue = k+random.nextDouble()*l;
				
				//Remplissage de la matrice
				matrice[ligne][c] = randValue;
			}
		}
		
		return matrice;
	}
	
	public static double rmse(double[][] M, double[][] P) {
		/* TODO: Dominique */
		/* Méthode à coder */	
		return 0;
	}
	
	public static double updateUElem( double[][] M, double[][] U, double[][] V, int r, int s ) {
		/* TODO: Thierry */

		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return -1;
		}

		int n = M.length;
		int m = M[0].length;
		int d = V.length;

		/* r et s ont des valeurs valides */
		if (r < 0 || r >= n || s < 0 || s >= d) {
			return -1;
		}

		double numerator = 0;
		double denominator = 0;

		for (int j = 0; j < m; j++) {
			if (M[r][j] == 0) continue;

			double suv = 0;
			for (int k = 0; k < d; k++) {
				if (k == s) continue;
				suv += U[r][k] * V[k][j];
			}

			double vsj = V[s][j];
			double mrj = M[r][j];

			numerator   += vsj * (mrj - suv);
			denominator += vsj * vsj;
		}

		return numerator / denominator;
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


