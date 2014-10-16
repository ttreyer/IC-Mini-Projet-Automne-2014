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
		/* Méthode à coder */		
		return "";
	}
	
	public static boolean isMatrix( double[][] A ) {
		/* Méthode à coder */		
		return true;
	}
	
	public static double[][] multiplyMatrix(double[][] A, double[][] B) {
		/* Méthode à coder */
		return null;	
	}
	
	public static double[][] createMatrix( int n, int m, int k, int l) {
		/* Méthode à coder */
		/* Utilisez la variable 'random', par exemple */
		int tmp = random.nextInt(5);
		return null;
	}
	
	public static double rmse(double[][] M, double[][] P) {
		/* Méthode à coder */	
		return 0;
	}
	
	public static double updateUElem( double[][] M, double[][] U, double[][] V, int r, int s ) {
		/* Méthode à coder */	
		return 0;
	}
	
	public static double updateVElem( double[][] M, double[][] U, double[][] V, int r, int s ) {
		/* Méthode à coder */	
		return 0;		
	}
	
	public static double[][] optimizeU( double[][] M, double[][] U, double[][] V) {
		/* Méthode à coder */	
		return null;		
	}

	public static double[][] optimizeV( double[][] M, double[][] U, double[][] V) {
		/* Méthode à coder */	
		return null;		
	}
	
	public static int[] recommend( double[][] M, int d) {
		/* Méthode à coder */	
		return null;
	}
}


