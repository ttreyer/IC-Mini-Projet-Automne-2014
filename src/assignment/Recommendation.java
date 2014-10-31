package assignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class Recommendation {

	/* Etudiant 1 */
	public static String NAME1 = "Dominique Roduit";
	public static int SCIPER1 = 234868;

	/* Etudiant 2 */
	public static String NAME2 = "Thierry Treyer";
	public static int SCIPER2 = 235116;

	static Random random = new Random();

	public static double OPTIMIZE_RMSE_DELTA_MAX = 6e-5;
	public static double OPTIMIZE_RMSE_DELTA_MIN = 1e-10;
	public static double OPTIMIZE_RMSE_DELTA_STOP = 1e-5;

	// ======================================================================================
	/* Utility functions */
	// ======================================================================================

	public static double[][] matrixCopy (double[][] A) {
		if (isMatrix(A) == false) {
			return null;
		}

		int n = A.length;
		double[][] B = new double[n][];

		for (int i = 0; i < n; i++) {
			B[i] = Arrays.copyOf(A[i], A[i].length);
		}

		return B;
	}

	public static double matrixAverage (double[][] A) {
		if (isMatrix(A) == false) {
			return -1.0;
		}

		double sum = 0.0;
		int count = 0;

		for (int i = 0, m = A.length; i < m; i++) {
			for (int j = 0, n = A[i].length; j < n; j++) {
				if (A[i][j] != 0) {
					sum += A[i][j];
					count++;
				}
			}
		}

		// Si tous les éléments du tableau sont nuls, on retourne la valeur maximal double positif
		if (count > 0) {
			return sum / count;
		} else {
			return Double.POSITIVE_INFINITY;
		}
	}

	public static String join (String separator, double[] items, DecimalFormat format) {
		int l = items.length;

		if (l == 0) {
			return "";
		}

		StringBuilder joinedItems = new StringBuilder();

		joinedItems.append(format.format(items[0]));
		for (int i = 1; i < l; i++) {
			joinedItems.append(separator).append(format.format(items[i]));
		}

		return joinedItems.toString();
	}


	// ======================================================================================

	/** 
	 * Convertion d'une matrice en chaine de caractère
	 * @param A Matrice à convertir
	 * @return La matrice passée en argument sous forme de chaine de caractère
	 */
	public static String matrixToString(double[][] A) {
		if(!isMatrix(A)) return null;

		// Formattage des valeurs de la matrice à 1 décimals au min et au max
		DecimalFormat f = new DecimalFormat();
		f.setMinimumFractionDigits(1);
		f.setMaximumFractionDigits(1);

		// On ne veux pas d'apostrophe de séparation pour les grands chiffres (ex: 1'000)
		f.setGroupingUsed(false);

		// Chaine contenant la matrice
		String SMatrix = "{\n";

		// On parcours les lignes de la matrice
		for(int i=0; i<A.length; ++i) {
			SMatrix += String.format("\t{ %s },\n", join(", ", A[i], f));
		}

		SMatrix += "};";

		return SMatrix;
	}

	/**
	 * Contrôle que le tableau 2D passé en paramètre soit la représentation d'une matrice valide
	 * @param A Tableau 2D a vérifier
	 * @return true : A est une matrice, false : A n'est pas une matrice valable
	 */
	public static boolean isMatrix( double[][] A ) {
		/* A n'est pas null ou vide */
		if (A == null || A.length == 0) {
			return false;
		}

		/* La première ligne de A n'est pas null ou vide */
		if (A[0] == null || A[0].length == 0) {
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

	/**
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	public static double[][] multiplyMatrix(double[][] A, double[][] B) {
		/* Matrices A et B valides */
		if (isMatrix(A) == false || isMatrix(B) == false) {
			return null;
		}

		/* Matrices de tailles compatibles */
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
		return createMatrix(n, m, (double) k, (double) l, 1.0);
	}

	public static double[][] createMatrix( int n, int m, double k, double l) {
		return createMatrix(n, m, k, l, 1.0);
	}

	public static double[][] createMatrix( int n, int m, double k, double l, double r) {
		double randValue = 0;
		// Si les dimensions de la matrices ou la plage ne valeurs ne sont pas correctes
		if(m<=0 || n<=0 || l<k) return null;

		// Matrice
		double[][] matrice = new double[n][m];

		// On parcours chaque ligne de la matrice
		for(int ligne=0; ligne<n; ++ligne) {
			// On parcours chaque colonne
			for(int c=0; c<m; ++c) {

				// Génération d'une valeur réelle aléatoire comprise entre k et l
				randValue = k+(l-k)*random.nextDouble();
	
				//Remplissage de la matrice
				if (random.nextDouble() < r) {
					matrice[ligne][c] = randValue;
				}
			}
		}

		// On ne retourne la matrice que si celle-ci est correcte
		if(isMatrix(matrice)) 
			return matrice;
		else
			return null;
	}

	/**
	 * Calcul du RMSE entre les matrices M et P pour tous les éléments non nuls de M
	 * @param M Matrice M
	 * @param P Matrice P
	 * @return Calcul du RMSE
	 */
	public static double rmse(double[][] M, double[][] P) {
		//  Si les tableaux ne sont pas des matrices
		if(!isMatrix(M) || !isMatrix(P)) {
			return -1;
		}

		// Si les matrices ne sont pas de même dimensions
		if(M.length!=P.length || M[0].length!= P[0].length) {
			return -1;
		}


		int notNullEntries = 0;
		double S = 0;

		// Parcours des lignes
		for(int i=0; i<M.length; ++i) {
			for(int j=0; j<M[0].length; ++j) {
				if(M[i][j]!=0) {
					S += Math.pow(M[i][j]-P[i][j], 2);
					++notNullEntries;
				} 
			}
		}

		// Si le denominateur de Smean est 0 on ne fais pas la division !
		if (notNullEntries > 0) {
			return Math.sqrt(Math.abs(S / notNullEntries));
		} else {
			return -1;
		}
	}

	/**
	 * Mise à jour d'un élément de la matrice des utilisateur 
	 * @param M UtilityMatrix
	 * @param U Matrice des utilisateurs
	 * @param V Matrice des features
	 * @param r Indice
	 * @param s Indice
	 * @return Nouvelle valeur 
	 */
	public static double updateUElem( double[][] M, double[][] U, double[][] V, int r, int s ) {
		/* Matrices valides */
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

		/* U a une taille valide */
		if (U.length != n || U[0].length != d) {
			return -1;
		}

		/* V a une taille valide */
		if (V.length != d || V[0].length != m) {
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

		if (denominator > 0) {
			return numerator / denominator;
		} else {
			return -1;
		}
	}

	/**
	 * Met à jour une valeur de V donnée par son indice r et s pour réduire le RMSE entre M et P=UV
	 * @param M 
	 * @param U
	 * @param V
	 * @param r indice de ligne
	 * @param s indice de colonne
	 * @return Nouvelle valeur de l'élément de V
	 */
	public static double updateVElem( double[][] M, double[][] U, double[][] V, int r, int s ) {
		/* Matrices valides */
		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return -1;
		}

		int n = M.length;
		int m = M[0].length;
		int d = V.length;

		/* r et s ont des valeurs valides */
		if (r < 0 || r >= d || s < 0 || s >= m) {
			return -1;
		}

		/* U a une taille valide */
		if (U.length != n || U[0].length != d) {
			return -1;
		}

		/* V a une taille valide */
		if (V.length != d || V[0].length != m) {
			return -1;
		}

		double numerator = 0;
		double denominator = 0;

		for (int i = 0; i < n; i++) {
			if (M[i][s] == 0) continue;

			double suv = 0;
			for (int k = 0; k < d; k++) {
				if (k == r) continue;
				suv += U[i][k] * V[k][s];
			}

			double uir = U[i][r];
			double mis = M[i][s];

			numerator   += uir * (mis - suv);
			denominator += uir * uir;
		}

		if (denominator > 0) {
			return numerator / denominator;
		} else {
			return -1;
		}
	}

	/**
	 * Optimisationde la matrice U élément par élément
	 * @param M UtilityMatrix
	 * @param U Matrice des utilisateurs
	 * @param V Matrice des features
	 * @return Matrice U Optimisée
	 */
	public static double[][] optimizeUIter( double[][] M, double[][] U, double[][] V) {
		/* Matrices valides */
		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return null;
		}

		int n = M.length;
		int m = M[0].length;
		int d = V.length;

		/* Copie de U */
		double[][] Up = matrixCopy(U);

		/* Amélioration de U */
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < d; j++) {
				Up[i][j] = updateUElem(M, Up, V, i, j);
			}
		}

		return Up;
	}

	/**
	 * Recherche de la meilleure optimisation possible
	 * @param M Utility Matrix
	 * @param U Matrice des utilisateurs
	 * @param V Matrice des features
	 * @return Matrice U optimisée
	 */
	public static double[][] optimizeU( double[][] M, double[][] U, double[][] V) {
		/* Matrices valides */
		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return null;
		}

		double[][] lastU    = U;
		double[][] currentU = U;

		double lastRmse    = 0;
		double currentRmse = Double.POSITIVE_INFINITY;
		double deltaRmse   = Double.POSITIVE_INFINITY;

		while (deltaRmse > OPTIMIZE_RMSE_DELTA_STOP) {
			lastRmse = currentRmse;
			lastU    = currentU;

			currentU = optimizeUIter(M, lastU, V);

			currentRmse = rmse(M, multiplyMatrix(currentU, V));
			deltaRmse   = Math.abs(lastRmse - currentRmse);
		}

		return currentU;
	}

	/**
	 * Optimisation de la matrice V élément par élément
	 * @param M UtilityMatrix
	 * @param U Matrice des utilisateurs
	 * @param V Matrice des features
	 * @return Matrice V Optimisée
	 */
	public static double[][] optimizeVIter( double[][] M, double[][] U, double[][] V) {
		/* Matrices valides */
		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return null;
		}

		int n = M.length;
		int m = M[0].length;
		int d = V.length;

		/* Copie de V */
		double[][] Vp = matrixCopy(V);

		/* Amélioration de V */
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < m; j++) {
				Vp[i][j] = updateVElem(M, U, Vp, i, j);
			}
		}

		return Vp;
	}

	/**
	 * Recherche de la meilleure optimisation possible pour la matrice V
	 * @param M Utility Matrix
	 * @param U Matrice U
	 * @param V Matrice V
	 * @return Matrice V optimisée
	 */
	public static double[][] optimizeV( double[][] M, double[][] U, double[][] V) {
		/* Matrices valides */
		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return null;
		}

		double[][] lastV    = V;
		double[][] currentV = V;

		double lastRmse    = 0;
		double currentRmse = Double.POSITIVE_INFINITY;
		double deltaRmse   = Double.POSITIVE_INFINITY;

		while (deltaRmse > OPTIMIZE_RMSE_DELTA_STOP) {
			lastRmse = currentRmse;
			lastV    = currentV;

			currentV = optimizeVIter(M, U, lastV);

			currentRmse = rmse(M, multiplyMatrix(U, currentV));
			deltaRmse   = Math.abs(lastRmse - currentRmse);
		}

		return currentV;
	}

	/**
	 * Cette méthode retourne le tableau des recommandations
	 * @param M Matrice nxm. Les lignes de M correspondent aux utilisateurs, les colonnes contiennent les notes des utilisateurs pour chaque films
	 * @param d Dimension définissant les tailles de U, V (Si M est n x m => U = n x d, V = d x m)
	 * @return Tableau d'entiers indiquant à la position i, la meilleure recommandation de l'utilisateur i.
	 */
	public static int[] recommend( double[][] M, int d) {
		// Vérifications des paramètres
		if(!isMatrix(M) || d<=0) {
			return null;
		}

		int n = M.length;
		int m = M[0].length;

		int s = n * d + d * m;
		double x = s / 6000.0;

		OPTIMIZE_RMSE_DELTA_STOP = x * OPTIMIZE_RMSE_DELTA_MAX + OPTIMIZE_RMSE_DELTA_MIN;

		// Meilleur recommendation pour chaques utilisateurs, initialisé à -1
		int[] recommended = new int[n];
		for (int i = 0; i < n; i++) {
			recommended[i] = -1;
		}

		// Valeur moyenne de M
		double average = Math.abs(matrixAverage(M));
		if (average == Double.POSITIVE_INFINITY) {
			return recommended; // => M est vide
		}

		double v = Math.sqrt(average / d);

		double minRMSE = Double.POSITIVE_INFINITY; // RMSE minimale entre M et P
		double[][] P = null; // Meilleurs matrice après les différentes optimisations

		// On recherche pour quelle valeur de C, la RMSE de P et M est la meilleure
		for (double c = 0; c <= 1.0; c += 0.2) {
			double lastRMSE    = Double.POSITIVE_INFINITY;
			double currentRMSE = Double.POSITIVE_INFINITY;
			double deltaRMSE   = Double.POSITIVE_INFINITY;

			double[][] currentP = null;

			// Génération de U et V
			double[][] U = createMatrix(n, d, (v - c * v), (v + c * v));
			double[][] V = createMatrix(d, m, (v - c * v), (v + c * v));

			while (deltaRMSE > OPTIMIZE_RMSE_DELTA_STOP) {
				lastRMSE = currentRMSE;

				// Optimisation des matrices U et V
				U = optimizeU(M, U, V);
				V = optimizeV(M, U, V);

				currentP = multiplyMatrix(U, V);

				// RMSE entre M et P pour la valeur actuelle de C
				currentRMSE = rmse(M, currentP);
				deltaRMSE = Math.abs(lastRMSE - currentRMSE);
			}

			// On enregistre ce résultat s'il est meilleur que le précédent
			if (currentRMSE < minRMSE) {
				minRMSE = currentRMSE;
				P = currentP;
			}
		}

		// Pour tous les utilisateurs...
		for (int i = 0; i < n; i++) {
			double maxValueInP = Double.NEGATIVE_INFINITY;

			// On recherche la meilleur recommendation
			for (int j = 0; j < m; j++) {
				if (M[i][j] != 0) continue;

				if (P[i][j] > maxValueInP) {
					maxValueInP = P[i][j];
					recommended[i] = j;
				}
			}
		}

		return recommended;
	}
}

