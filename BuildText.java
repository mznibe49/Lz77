import java.io.*;
import java.util.*;

public class BuildText {

    public static void main(String [] args) throws IOException {
	BitOutputStream x = new BitOutputStream(new FileOutputStream("test.txt"));
	int i = 0;
	Reader r = new Reader(new File("test.txt"));	
	while ( i < 10 ){
	    int random = (int)(Math.random()*256);
	    //x.write((char)random);
	    System.out.println(random);
	    //r.gen(x,(char)random);
	    i++;
	}
	x.close();
    }

}
