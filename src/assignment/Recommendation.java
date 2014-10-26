package assignment;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

	public static double OPTIMIZE_RMSE_DELTA_STOP = 1e-6;
	
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
		double[][] matriceTest = createMatrix(10, 10, -5, 10);
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
	 * @author Dominique
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
			 // Une tabulation et debut de la ligne
			SMatrix += "\t{";
			
			// On parcours les colonnes de la matrice
			for(int j=0; j<A[i].length; ++j) {
				SMatrix += f.format(A[i][j]);
				// Ajout d'une virgule après chaque valeur sauf la dernière
				if(j!=A[i].length-1) SMatrix += ", ";
			}
			
			// fin de la ligne de matrice
			SMatrix += "}";
			// Ajout d'une virgule après chaque ligne de matrice excepté la dernière
			if(i!=A.length-1) SMatrix += ","; 
			// Retour à la ligne après chaque ligne de matrice
			SMatrix += "\n"; 
		}
		SMatrix += "};";
		
		return SMatrix;
	}
	
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
	 * @author Dominique
	 * @return Matrice nxm contenant des nombres réels générés aléatoirements
	 */
	public static double[][] createMatrix( int n, int m, int k, int l) {
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
	
	/**
	 * Calcul du RMSE entre les matrices M et P pour tous les éléments non nuls de M
	 * @param M Matrice M
	 * @param P Matrice P
	 * @author Dominique
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
		
		double Smean = S/notNullEntries;
		
		return Math.sqrt(Smean);
	}
	
	/**
	 * Retourne la dimension de la matrice dans un tableau [ligne, colonne]
	 * @param A Matrice (tableau à 2 dimensions)
	 * @author Dominique
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
	 * @author Dominique
	 * @return true : A et B ont la même dimension, sinon, false
	 */
	public static boolean compareMatrixDimensions(double[][] A, double[][] B) {
		int[] dimA = getMatrixDimension(A);
		int[] dimB = getMatrixDimension(B);
		return (dimA[0]==dimB[0] && dimA[1]==dimB[1]);
	}
	
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

		return numerator / denominator;
	}
	
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

		return numerator / denominator;
	}

	public static double[][] optimizeUIter( double[][] M, double[][] U, double[][] V) {
		/* Matrices valides */
		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return null;
		}

		int n = M.length;
		int m = M[0].length;
		int d = V.length;

		/* Copie de U */
		double[][] Up = new double[n][];
		for (int i = 0; i < n; i++) {
			Up[i] = Arrays.copyOf(U[i], d);
		}

		/* Amélioration de U */
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < d; j++) {
				Up[i][j] = updateUElem(M, Up, V, i, j);
			}
		}

		return Up;
	}

	public static double[][] optimizeU( double[][] M, double[][] U, double[][] V) {
		/* Matrices valides */
		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return null;
		}

		double[][] lastU = U;
		double[][] currentU = U;
		double lastRmse = 0;
		double currentRmse = Double.POSITIVE_INFINITY;
		double[][] P = null;

		do {
			lastRmse = currentRmse;
			lastU = currentU;

			currentU = optimizeUIter(M, lastU, V);

			P = multiplyMatrix(currentU, V);
			currentRmse = rmse(M, P);
		} while (Math.abs(lastRmse - currentRmse) < OPTIMIZE_RMSE_DELTA_STOP);

		return currentU;
	}

	public static double[][] optimizeVIter( double[][] M, double[][] U, double[][] V) {
		/* Matrices valides */
		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return null;
		}

		int n = M.length;
		int m = M[0].length;
		int d = V.length;

		/* Copie de V */
		double[][] Vp = new double[d][];
		for (int i = 0; i < d; i++) {
			Vp[i] = Arrays.copyOf(V[i], m);
		}

		/* Amélioration de V */
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < m; j++) {
				Vp[i][j] = updateVElem(M, U, Vp, i, j);
			}
		}

		return Vp;
	}

	public static double[][] optimizeV( double[][] M, double[][] U, double[][] V) {
		/* Matrices valides */
		if (isMatrix(M) == false || isMatrix(U) == false || isMatrix(V) == false) {
			return null;
		}

		double[][] lastV = V;
		double[][] currentV = V;
		double lastRmse = 0;
		double currentRmse = Double.POSITIVE_INFINITY;
		double[][] P = null;

		do {
			lastRmse = currentRmse;
			lastV = currentV;

			currentV = optimizeVIter(M, U, lastV);

			P = multiplyMatrix(U, currentV);
			currentRmse = rmse(M, P);
		} while (Math.abs(lastRmse - currentRmse) < OPTIMIZE_RMSE_DELTA_STOP);

		return currentV;
	}
	
	/**
	 * Cette méthode retourne le tableau des recommandations
	 * @param M Matrice nxm. Les lignes de M correspondent aux utilisateurs, les colonnes contiennent les notes des utilisateurs pour chaque films
	 * @param d Dimension définissant les tailles de U, V (Si M est n x m => U = n x d, V = d x m)
	 * @return Tableau d'entiers indiquant à la position i, la meilleure recommandation de l'utilisateur i.
	 */
	public static int[] recommend( double[][] M, int d) {
		
		if(!isMatrix(M) || d<=0) {
			return null;
		}
		
		System.out.println("===== Recommandation =====\n\n");
	
		
		double c = 1;
		
		double v = 0;
		double sum = 0;
		for(int i=0; i<M.length; ++i) {
			for(int j=0; j<M[0].length; ++j) {
				if(M[i][j]!=0) {
					sum += M[i][j];
				}
			}
		}
		v = Math.sqrt(sum/d);
		
		int minVal = (int)(v-c);
		int maxVal = (int)(v+c);
		
		// U (nxd)
		double[][] U = createMatrix(M.length, d, minVal, maxVal);
		// V (dxm)
		double[][] V = createMatrix(d, M[0].length, minVal, maxVal);
		
		System.out.println("===== U non optimisé (générée aléatoirement) =====");
		System.out.println(matrixToString(U)+"\n\n");
		
		System.out.println("===== V non optimisé (générée aléatoirement) =====");
		System.out.println(matrixToString(V)+"\n\n");
		
		// Optimisation des matrices U et V
		U = optimizeU(M, U, V);
		V = optimizeV(M, U, V);
		
		System.out.println("===== U optimisé =====");
		System.out.println(matrixToString(U)+"\n\n");
		
		System.out.println("===== V optimisé =====");
		System.out.println(matrixToString(V)+"\n\n");
		
		double[][] P = multiplyMatrix(U, V);
		
		
		System.out.println("===== Matrice P = UV =====");
		System.out.println(matrixToString(P)+"\n\n");

		// Stock les valeurs trouvées dans P pour les indices des entrées à 0 dans M
		double[][] valuesP = new double[M.length][M[0].length];
		/* Chaine de caractère qui sera coupée en tableau qui contiendra
		 * temporairement les valeurs de P pour lesquelles
		 * la valeur au meme indice dans M est nulle */
		String valuesForUser = "";
		// Variable temporaire contenant le tableau de la chaine valuesForUser éclatée
		String[] tmpValues = null;
		
		// Variable qui contient la valeur maximum stockée dans valuesP pour chaque utilisateur
		double maxValueInP = 0; 

		/* Contiendra la valeur maximum de P pour chaque utilisateur,
		 * dont les valeurs aux memes indices dans M sont nulles
		 */
		int[] recommended = new int[M.length];
		
		// On parcours M pour enregistrer l'indice des valeurs à 0
		for(int i=0; i<M.length; ++i) {
			// On vide la chaine a chaque tour de boucle (puisqu'on pointe sur un nouvel utilisateur)
			valuesForUser = "";
			
			// On parcours les entrées de M
			for(int j=0; j<M[0].length; ++j) {
				// Si l'entrée dans M vaut 0 on stocke la valeur correspondante de P dans une chaine de caractère
				if(M[i][j]==0) {
					valuesForUser += P[i][j]+";";
				}
			}
			
			maxValueInP = 0;
			
			if(!valuesForUser.isEmpty()) {
				// On éclate la chaine en tableau
				tmpValues = valuesForUser.split(";");
				
				if(tmpValues.length>0) {
					if(tmpValues.length==1) {
						maxValueInP = Double.parseDouble(tmpValues[0]);
					} else {
						// On stocke chaque valeurs enregistrée pour l'utilisateur
						for(int j=0; j<tmpValues.length; ++j) {
							
							valuesP[i][j] = Double.parseDouble(tmpValues[j]);
						}
						
						// Pour l'utilisateur, on récupère la valeur maximum parmi les valeurs récupérées dans P
						for(int j=0; j<valuesP[i].length; ++j) {
							if(valuesP[i][j]>maxValueInP) {
								maxValueInP = valuesP[i][j];
							}
						}
					}
				}
			}
			
			// Cette valeur max est la recommandation, on la stocke dans le tableau de retour
			recommended[i] = (int)maxValueInP;
		}
				
		
		return recommended;
	}
}


