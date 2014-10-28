package assignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class Recommendation {

	/* Etudiant 1 */
	public static String NAME1 = "Dominique Roduit";
	public static int SCIPER1 = 234868;
	
	/* Etudiant 2 - laissez tel quel si vous avez codé le projet tout seul */
	public static String NAME2 = "Thierry Treyer";
	public static int SCIPER2 = 235116;
	
	static Random random = new Random();

	/* U prend beaucoup plus de temps à optimizer
	 * On compense en augmentant la limite d'optimisation */
	public static double OPTIMIZE_U_RMSE_DELTA_STOP = 1e-5;
	public static double OPTIMIZE_V_RMSE_DELTA_STOP = 1e-6;

	/* Utility functions */
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
		
		if(count==0) return -1.0;
		
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

	public static void main(String[] args) {
		double[][] M = {{10, 0, 2, 0},
						{
						0, 5.5, 0, 3},
						{ 4,
						0, 1, 0},
						{ 0, 0, 0, 4.5
						}};
		
		System.out.println("===== Matrice M =======");
		System.out.println(matrixToString(M));
		int[] recommended = recommend(M, 10);
		
		System.out.println("Résultat recommend : ");
		for(int i=0; i<recommended.length; ++i) {
			System.out.println(recommended[i]);
		}
		
		System.out.println("################################################\n\n");
		
		System.out.println("===== Matrice M (générée aléatoirement) =======");
		double[][] matriceTest = createMatrixZeroRandom(1000, 1000, -5, 10);
		System.out.println(matrixToString(matriceTest));
		int[] recommended1 = recommend(matriceTest, 10);
		
		System.out.println("Résultat recommend : ");
		for(int i=0; i<recommended1.length; ++i) {
			System.out.println(recommended1[i]);
		}
		
		
	}

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
	 * 
	 * @param A
	 * @return
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
		double randValue;
		
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
				matrice[ligne][c] = randValue;
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
		// Si les matrices ne sont pas de même dimensions ou si les tableaux ne sont pas des matrices
		if(!isMatrix(M) || !isMatrix(P) || !compareMatrixDimensions(M, P)) {
			return -1;
		}
		
		// A partir d'ici, on sait que les dimensions sont les mêmes
		int[] dim = getMatrixDimension(M);
		
		double[] RMSE = new double[dim[0]];
		int notNullEntries = 0;
		
		// Parcours des lignes
		for(int i=0; i<dim[0]; ++i) {
			for(int j=0; j<dim[1]; ++j) {
				if(M[i][j]!=0) {
					RMSE[i] += Math.pow(M[i][j]-P[i][j], 2);
					++notNullEntries;
				} 
			}
		}
		
		double S = 0;
		for(int i=0; i<RMSE.length; ++i) {
			S += RMSE[i];
		}
		
		if(notNullEntries==0) return -1;
		
		double Smean = S/notNullEntries;
		
		return Math.sqrt(Smean);
	}
	
	/**
	 * Retourne la dimension de la matrice dans un tableau [ligne, colonne]
	 * @param A Matrice (tableau à 2 dimensions)
	 * @return Dimensions de la matrice
	 */
	public static int[] getMatrixDimension(double[][] A) {
		int lignes = A.length;
		int colonnes = A[0].length;
		return new int[]{lignes,colonnes};
	}
	
	/**
	 * Compare les dimensions de deux matrices
	 * @param A Matrice 1
	 * @param B Matrice 2
	 * @return true : A et B ont la même dimension, sinon, false
	 */
	public static boolean compareMatrixDimensions(double[][] A, double[][] B) {
		int[] dimA = getMatrixDimension(A);
		int[] dimB = getMatrixDimension(B);
		return (dimA[0]==dimB[0] && dimA[1]==dimB[1]);
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

		while (deltaRmse > OPTIMIZE_U_RMSE_DELTA_STOP) {
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

		while (deltaRmse > OPTIMIZE_V_RMSE_DELTA_STOP) {
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
		// Vérifie que M soit une matrice et que la dimension de matrice d ne soit pas <= 0
		if(!isMatrix(M) || d<=0) {
			return null;
		}
		
		// Initialisation
		double average = matrixAverage(M);
		if(average==-1) return null;
		
		double v = Math.sqrt(average/d);
		
		double minRMSE = 0; // RMSE minimale entre M et P
		double bestC = 0; // Valeur de C pour laquelle la RMSE est minimale
		double currentRMSE = 0; // RMSE actuelle dans chaque tours de boucles
		double[][] P = null; // Matrice P pour chaque tour de boucle
		double[][] bestP = null; // Matrice P avec la meilleure Optimisation (RMSE la plus faible)
		
		double minVal = 0; // Borne minimum pour la génération de U et V
		double maxVal = 0; // Borne maximum pour la génération de U et V
		
		// On recherche pour quelle valeur de C, la RMSE de P et M est la meilleure
		for(double c=0; c<=1; c+=0.1) {
			// Plage de valeurs générées
			minVal = v - c;
			maxVal = v + c;
			
			// Génération de U et V
			double[][] U = createMatrix(M.length, d, minVal, maxVal); // U (nxd)
			double[][] V = createMatrix(d, M[0].length, minVal, maxVal); // V (dxm)
			
			/*
			System.out.println("===== U non optimisé (générée aléatoirement) =====");
			System.out.println(matrixToString(U)+"\n\n");
			
			System.out.println("===== V non optimisé (générée aléatoirement) =====");
			System.out.println(matrixToString(V)+"\n\n");
			*/
			
			// Optimisation des matrices U et V
			U = optimizeU(M, U, V);
			V = optimizeV(M, U, V);
			
			/*
			System.out.println("===== U optimisé =====");
			System.out.println(matrixToString(U)+"\n\n");
			
			System.out.println("===== V optimisé =====");
			System.out.println(matrixToString(V)+"\n\n");
			*/
			
			// Multiplication de U et V optimisées
			P = multiplyMatrix(U, V);
			
			// RMSE entre M et P pour la valeur actuelle de C
			currentRMSE = rmse(M, P);
			
			// Au premier tour de boucle on définit la RMSE actuelle comme étant la min.
			if(c==0) minRMSE = currentRMSE;
			
			// Si la RMSE actuelle est plus petit que la min. deja enregistrée
			if(currentRMSE<minRMSE) {
				bestC = c; // Enregistrement de la meilleur valeur de c
				minRMSE = currentRMSE; // Enregistrement du RMSE min
				bestP = P; // Enregistrement de la meilleure matrice P
			}
		}
		
		// On veux utiliser la meilleur matrice P possible
		P = bestP;
		
		/*
		System.out.println("===== Matrice P = UV =====");
		System.out.println(matrixToString(P)+"\n\n");
		 */

		// Variable qui contient la valeur maximum stockée dans P pour chaque utilisateur
		double maxValueInP = Double.NEGATIVE_INFINITY;

		/* Contiendra les indices des valeurs maximum dans P pour chaque utilisateur,
		 * dont les valeurs aux memes indices dans M sont nulles
		 */
		int[] recommended = new int[M.length];
		
		// On parcours les lignes de M (les utilisateurs)
		for(int i=0; i<M.length; ++i) {
			// Par defaut, aucune recommandation
			recommended[i] = -1;
			// Valeur max au minimum pour qu'une valeur de P soit toujours plus grande
			maxValueInP = Double.NEGATIVE_INFINITY;
			
			// On parcours les entrées de M
			for(int j=0; j<M[0].length; ++j) {
				// Pour toutes les entrées nulles de M
				if(M[i][j]==0) {
					/* On stock pour chaque utilisateur la plus grande valeur
					 * de P correpondante aux indices en cours dans M
					 */
					if(P[i][j]>maxValueInP) recommended[i] = j;
				}
			}
		}
				
		return recommended;
	}
}


