import java.io.*;
import java.util.*;

public class Reader {

    private File f;

    public Reader(File f) {
	this.f = f;
    }

    public String texte() throws IOException {
	Scanner in = new Scanner(this.f);
	StringBuffer s = new StringBuffer("");
	while(in.hasNextLine()){
	    s.append(in.nextLine());
	    s.append("\n");
	}
	if(s.equals("")) return "";
	String newS = s.substring(0,s.length()-1);
	//System.out.println(newS+'\n');
	return newS;
    }



    public int exist(StringBuffer s, int index, String ph){
	String tmp = ph.substring(0,index);
	if(tmp.contains(s)){
	    String k = s.substring(0,s.length());
	    int p = tmp.length() - tmp.lastIndexOf(k);
	    return p;	
	} else {
	    return -1;
	}
    }

    
    public void compresser(String phrase, ArrayList<Lz77> list) {

	StringBuffer s = new StringBuffer("");
	int pointeur = 0;
	String res = "";
	
	//on parcours tt la phrase chaque caractere
	for(int i = 0; i<phrase.length(); i++){

	    //on cherche les matchs 
	    for(int j = i ; j < phrase.length(); j++){
		
		// pour chaque char on cherche la taille et son pointeur
		s.append(phrase.charAt(j));

		// si jamais le char exist pas avant on le stock comme le premier char  de la phrase
		if( (exist(s,i,phrase) == -1) && s.length() == 1){
		    Lz77 tmp2 = new Lz77(0,0,s.charAt(0));
		    list.add(tmp2);
		    //System.out.println("(0,0,"+s.charAt(0)+")");
		    //s = "";
		    s = new StringBuffer("");
		    j = phrase.length();
		}
		// si l'element existe et que ça taille est sur a 1 on stock l'emplacement du pointeur       
		if ( exist(s,i,phrase) != -1 && s.length() >= 1) {			
		    pointeur = exist(s,i,phrase);
		    res = s.substring(0,s.length());
		    // si l'ement n'existe pas et que la taille est supperieur a 1	
		} else  if ( exist(s,i,phrase) == -1 && s.length() >= 1){
		    res = s.substring(0,s.length()-1);
		    j = phrase.length();
		}		    
	    }
	    if(res.length() >=1 ){
		    
		i += res.length();
		Lz77 tmp3 = null;
		if(i>= phrase.length()) {
		    tmp3 = new Lz77(pointeur,res.length(),'\u0000');
		} else {
		    tmp3 = new Lz77(pointeur,res.length(),phrase.charAt(i));
		}
		list.add(tmp3);
		//System.out.println("("+tmp3.getPointer()+","+tmp3.getLongueur()+","+tmp3.getNextChar()+")");
		s = new StringBuffer("");
		res = "";
		pointeur = 0;
	    }
        }
	// on arrive pas a affiché cette ligne qd le fichier est grnd
	System.out.println("=> :");
    }

    
    // convertion d'un entier entre 0 et 9 a un char '0','1',..,'9'
    public char intToChar(int a){
	String s = ""+a;
	return s.charAt(0);
    }

    // convertion d'un int a un String qui contient sa representation binaire
    // ex : un 7 devient "111"
    public StringBuffer intToByte(int a){
	if(a == 0) return new StringBuffer("0");
        ArrayList<Integer> list = new ArrayList<Integer>();
	while( a != 0 ){
	    int x = a%2;
	    list.add(x);
	    a /= 2;
	}	
	StringBuffer b = new StringBuffer("");
	for(int i = list.size()-1; i>=0; i--){
	    char tmp = intToChar(list.get(i));
	    b.append(tmp);		     
	}
	return b;
    }

    // renvoie le max de l'array list entre la taille et le pointeur
    // pour representer une taille fixe
    public int max_list(ArrayList<Lz77> list){
	int max = 0;
	for(int i = 0; i<list.size(); i++){
	    if(max <= list.get(i).getPointer())
		max = list.get(i).getPointer();
	    if(max <= list.get(i).getLongueur())
		max = list.get(i).getLongueur();
	}
	return max;
    }

    
    //ecrire  4oc dans le fichiers pour representer la Taille max que Pointer/Length du triplet
    public void writeLengthInFile(BitOutputStream x, StringBuffer res) throws IOException {
	int z = 0;
	while (z < 32-res.length()) {
	    x.writeBit(0);
	    z++;
	}
	for(int i = 0; i<res.length(); i++){
	    if(res.charAt(i) == '1')
		x.writeBit(1);
	    else
		x.writeBit(0);    
	}
    }

    // PT veut dire pointer ou taille
    public void writePTinFile(BitOutputStream x, StringBuffer res, StringBuffer pt) throws IOException{
	//on ecrit le pointer tel qu'il a une taille fixe
	int w = res.length()-pt.length();
	while(w != 0){
	    x.writeBit(0);
	    w--;
	}
	//on ecrit ke reste du pointeur
	for(int i = 0; i<pt.length(); i++){
	    if(pt.charAt(i) == '1')
		x.writeBit(1);
	    else
		x.writeBit(0);
	}
    }

    // on ecrite le char sur 1oc
    public void writeCharInFile(BitOutputStream x, char c) throws IOException {
	int a = (int)(c);
	int z = 0;
	StringBuffer res = intToByte(a);
	//System.out.println(" "+res);
	while(z<8-res.length()){
	    x.writeBit(0);
	    z++;
	}
	for(int i = 0; i<res.length(); i++){
	    if(res.charAt(i) == '1')
		x.writeBit(1);
	    else
		x.writeBit(0);
	}
    }
    
    // ecrire bit par bit dans le fichier
    // par ex un 1,a si 5 est le plus grand nombre
    // res : 101,001,ascii(a)
    public void writeInFileBit(ArrayList<Lz77> list) throws IOException {
	//PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("lz.txt")));
	BitOutputStream x = new BitOutputStream(new FileOutputStream("code.txt"));

	int max = max_list(list);
	StringBuffer res =  new StringBuffer("");

	if (max == 0)
	    res = new StringBuffer("0");
	else
	    res = intToByte(max);

	System.out.println();	
	
	//on ocuppe 4oc pour stocker le max
	writeLengthInFile(x,res);
	
	for (int i = 0; i<list.size(); i++){ 	    		
	    StringBuffer pointer = intToByte(list.get(i).getPointer());
	    StringBuffer taille = intToByte(list.get(i).getLongueur());

            //on ecrit le pointer dans le fichier sur la taille max
	    writePTinFile(x,res,pointer);
	    // on ecrit la taille dans le fichier sur la taille max
	    writePTinFile(x,res,taille);
	    //on ecrite le char sur 1oc
	    writeCharInFile(x,list.get(i).getNextChar());
	}
	x.close();
    }

    public void gen(BitOutputStream x,char c) throws IOException {
	writeCharInFile(x,c);
    }
}
