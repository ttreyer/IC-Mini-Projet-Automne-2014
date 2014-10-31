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

	/* U prend beaucoup plus de temps à optimizer
	 * On compense en augmentant la limite d'optimisation */
	public static double OPTIMIZE_RMSE_DELTA_STOP = 10e-6;

	public static void main(String[] args) {

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


		double[][] testRMSEM = {
			{5,2,4,4,3},
			{3,1,2,4,1},
			{2,0,3,1,4},
			{2,5,4,3,5},
			{4,4,5,4,0}
		};
		double[][] testRMSEP = {
			{2,2,2,2,2},
			{2,2,2,2,2},
			{2,2,2,2,2},
			{2,2,2,2,2},
			{2,2,2,2,2}
		};

		System.out.println(matrixToString(multiplyMatrix(testRMSEM, testRMSEP)));



		int[] recommended = null;
		int error = 0;
		// for (int j = 0; j < 200; j++) {
			recommended = recommend(M, 5);
			for(int i=0; i<recommended.length; ++i) {
				System.out.print(recommended[i]+" ");
			}
			System.out.println();
		// }


		/*
		   double[][] M = createMatrixZeroRandom(100,200,-10,10);
		   int[] recom = recommend(M, 20);
		   System.out.println();
		   for(int i=0; i<recom.length; ++i) {
		   System.out.println(i+" : "+recom[i]);
		   }
		   */
	}

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
		if(count==0) return Double.POSITIVE_INFINITY;

		return sum / count;
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
		return createMatrix(n, m, (double) k, (double) l);
	}

	public static double[][] createMatrix( int n, int m, double k, double l) {
		double randValue = 0;

		//System.out.println("(k, l)=("+k+","+l+")");
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
				//System.out.println("rand (k="+k+", (l-k)="+(l-k)+"): "+randValue);
				//Remplissage de la matrice
				matrice[ligne][c] = (randValue==Double.NaN) ? 0 : randValue;
			}
		}

		// On ne retourne la matrice que si celle-ci est correcte
		if(isMatrix(matrice)) 
			return matrice;
		else
			return null;
	}

	public static double[][] createMatrixZeroRandom( int n, int m, int k, int l) {
		double[][] A = createMatrix(n, m, k, l);

		if(!isMatrix(A)) return null;

		double rand =  0;
		for(int ligne=0; ligne<n; ++ligne) {
			for(int c=0; c<m; ++c) {
				rand = random.nextDouble();
				if(rand<0.6) A[ligne][c] = 0;
			}
		}

		return A;
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
		if(notNullEntries==0) return -1;

		double Smean = S/notNullEntries;

		/*
		   System.out.println("Methode RMSE : ");
		   System.out.println("S : "+S);
		   System.out.println("Entrées différentes de 0 : "+notNullEntries);
		   System.out.println("Smean : "+Smean);
		   */

		return Math.sqrt(Smean);
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

		if(denominator==0) return -1;


		return numerator / denominator;
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

		if(denominator==0) return -1;

		return numerator / denominator;
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
		// Vérifie que M soit une matrice et que la dimension d pour les matrices U et V ne soit pas <= 0
		if(!isMatrix(M) || d<=0) {
			return null;
		}

		/* Contiendra les indices des valeurs maximum dans P pour chaque utilisateur,
		 * dont les valeurs aux memes indices dans M sont nulles
		 */
		int[] recommended = new int[M.length];

		// Initialisation du tableau des recommandations. Avant le calcul, par défaut, tout à -1 pour chaque utilisateurs
		for(int i=0; i<M.length; ++i) {
			recommended[i] = -1;
		}

		// Initialisation. On prend la valeur absolue de ce nombre parce qu'il sera inséré dans une racine 
		// donc si sa valeur est négative ont aura un retour Not a Number !
		double average = Math.abs(matrixAverage(M));
		if(average==Double.POSITIVE_INFINITY) {
			return recommended;
		}

		double v = Math.sqrt(average/(double)d);


		double minRMSE = Double.POSITIVE_INFINITY; // RMSE minimale entre M et P
		double currentRMSE = Double.POSITIVE_INFINITY; // RMSE actuelle dans chaque tours de boucles
		double[][] P = null; // Matrice P pour chaque tour de boucle
		double[][] currentP = null;
		double bestC = 0;
		double[][] U = null;
		double[][] V = null;

		// On recherche pour quelle valeur de C, la RMSE de P et M est la meilleure
		for(double c=0; c<=0.5; c+=0.1) {
			double lastRMSE = Double.POSITIVE_INFINITY;
			double deltaRMSE = Double.POSITIVE_INFINITY;
			// Génération de U et V
			U = createMatrix(M.length, d, (v - c), (v + c)); // U (nxd)
			V = createMatrix(d, M[0].length, (v - c), (v + c)); // V (dxm)

			//System.out.println("v-c = "+(v-c));
			// System.out.println("v+c = "+(v+c));
			//System.out.println(matrixToString(U));


			while (deltaRMSE > OPTIMIZE_RMSE_DELTA_STOP) {
				lastRMSE = currentRMSE;

				// Optimisation des matrices U et V
				U = optimizeU(M, U, V);
				V = optimizeV(M, U, V);

				// Multiplication de U et V optimisées
				currentP = multiplyMatrix(U, V);

				// RMSE entre M et P pour la valeur actuelle de C
				currentRMSE = rmse(M, currentP);
				deltaRMSE = Math.abs(lastRMSE - currentRMSE);
			}

			//System.out.println("rmse (c="+c+"): "+rmse(M, currentP));
			// Si la RMSE actuelle est plus petit que la min. deja enregistrée
			if(currentRMSE<minRMSE) {
				bestC = c;
				minRMSE = currentRMSE; // Enregistrement du RMSE min
				P = currentP; // Enregistrement de la meilleure matrice P
			}
		}

		// Important ! Ne pas supprimer la ligne 
		//P = currentP;

		/*
		   System.out.println(" === M ===\n"+matrixToString(M));
		   System.out.println(" === P ===\n"+matrixToString(P));
		   System.out.println(" === U ===\n"+matrixToString(U));
		   System.out.println(" === V ===\n"+matrixToString(V));
		   */
		System.out.print(" c : "+bestC+" ## ");
		System.out.print(" rmse : "+minRMSE+" ## ");




		// On parcours les lignes de M (les utilisateurs)
		for(int i=0; i<M.length; ++i) {
			// Valeur max au minimum pour qu'une valeur de P soit toujours plus grande
			double maxValueInP = Double.NEGATIVE_INFINITY;

			// On parcours les entrées de M
			for(int j=0; j<M[0].length; ++j) {
				// Pour toutes les entrées nulles de M
				if(M[i][j]==0) {
					/* On stock pour chaque utilisateur la plus grande valeur
					 * de P correpondante aux indices en cours dans M
					 */
					if(P[i][j]>maxValueInP) {
						maxValueInP = P[i][j];
						recommended[i] = j;
					}
				}
			}
		}

		return recommended;
	}
}

