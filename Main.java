import java.io.*;
import java.util.*;

class Main {

    public static void main(String [] args) throws IOException {
	Chrono.Go_Chrono();
	long debut = System.currentTimeMillis();
	File f = new File("test.txt");
	Reader reader = new Reader(f);

	String texte = reader.texte();

	ArrayList<Lz77> list = new ArrayList<Lz77>();
		
	reader.compresser(texte,list);
	
	System.out.println("\n");
	
	//reader.ecrire(list);
	reader.writeInFileBit(list);
	
	BitInputStream input= new BitInputStream(new FileInputStream("code.txt"));
	input.decompression();
	//System.out.println(debut);
	Chrono.Stop_Chrono();

    }
}
