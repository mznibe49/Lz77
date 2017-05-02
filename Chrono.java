public class Chrono {

    // Fonctions pour le chronometre
    static double chrono = 0 ;


    
    // Lancement du chrono
    static void Go_Chrono() {
	chrono = java.lang.System.currentTimeMillis() ;
    }


    
    // Arret du chrono
    static void Stop_Chrono() {
	double chrono2 = java.lang.System.currentTimeMillis() ;
	double temps = chrono2 - chrono ;
	System.out.println("Temps ecoule = " + (temps/1000) + " s") ;
	//System.out.println("Temps ecoule = " + div + " s") ;
		
    } 

}
