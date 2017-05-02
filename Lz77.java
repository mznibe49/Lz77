public class Lz77 {

    int pointer;
    int longueur;
    char nextChar;

    public Lz77(int p, int l, char n){
	this.pointer = p;
	this.longueur = l;
	this.nextChar = n;
    }

    public int getPointer(){ return pointer; }
    public int getLongueur(){ return longueur; }
    public char getNextChar(){ return nextChar; }



}
