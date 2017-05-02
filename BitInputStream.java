import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

public class BitInputStream extends FilterInputStream {
    int bits;
    int mask;

    public BitInputStream(InputStream in) {
	super(in);
    }

    /**
     * Reads the next bit from this input stream. The value is returned as an
     * int in the range 0 to 1. If no bit is available because the end of the
     * stream has been reached, the value -1 is returned. This method blocks
     * until input data is available, the end of the stream is detected, or an
     * exception is thrown.
     */
    public int readBit() throws IOException {
	int bits;
	int mask = this.mask;

	if (mask == 0) {
	    bits = read();
	    if (bits == -1)
		return -1;

	    this.bits = bits;
	    mask = 0x80;
	} else {
	    bits = this.bits;
	}

	if ((bits & mask) == 0) {
	    this.mask = mask >> 1;
	    return 0;
	} else {
	    this.mask = mask >> 1;
	    return 1;
	}
    }
	
    public int[] readBit16(int nombre ) throws IOException{
	int[] bits = new int[nombre];
	for(int i=0; i<bits.length;i++)
	    bits[i] = -1;
		
	int j = 0;
	int tmp=-1;
	for(int i = 0;i < bits.length; i++){
	    tmp = readBit();
	    if(tmp < 0){
		break;
	    }
	    else{
		bits[j] = tmp;
		j++;
	    }
	}
	return bits;
    }
	
    public LinkedList<Integer> bits(int [] bits){
	LinkedList<Integer> tmp = new LinkedList<Integer>();
	for(int i=0; i< bits.length;i++){
	    if(bits[i] == 1){
		tmp.add(bits[i]);
		for(i=i+1; i<bits.length; i++){
		    tmp.add(bits[i]);
		}
	    }
	}
	return tmp;
    }
	
	
    public int convertInDecimal(LinkedList<Integer> binaires){
	int n = binaires.size();
	int p = n -1;
	int res = 0;
	for(int val : binaires){
	    res =  (int)(res+ val*Math.pow(2, p));
	    p--;
	}
	return res;
    }
	
    public static char intToChar(int n){
	return (char) n;
    }
	
	
    public void decompression() throws IOException {
	StringBuffer t =new StringBuffer("");
	int reference = bits(readBit16(32)).size();
	//System.out.println(reference);
	int j; //pointeur
	int k; //longueur
	char lettre; //char
	int cpt=0;
	while (this.available()!=0){
	    //mise a jour des variables toujours au debut
	    j = convertInDecimal(bits(readBit16(reference))); 
	    k = convertInDecimal(bits(readBit16(reference))); 
	    lettre = intToChar(convertInDecimal(bits(readBit16(8))));
	    //System.out.println("("+j+", "+k+", "+lettre+")");
	    if (j==0){ // si pointeur null
		t.append(lettre);
		cpt++;
		//System.out.println(" "+cpt);
	    }else{ //sinon
		int a = j;
		int b = k;
		int c = t.length()-a; //position du pointeur par rapport a la nouvelle taille du string
		int d = c+b; // la ou je dois m'arreter de copier
		cpt = cpt +k+1;
		//System.out.println(" "+cpt);
		for (int m=c; m<d; m++){
		    t.append(t.charAt(m));
		}
		t.append(lettre);
	    }
	    //System.out.println(t); // juste pour verifier que ca fait ce qu'il faut
	}
	String end = "";
	if (cpt != t.length()){
	    end = t.substring(0, cpt);
	} else {
	    end = t.substring(0,t.length());
	}
			
		
	PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("decode.txt")));
	out.write(end);
	out.close();
    }
}
