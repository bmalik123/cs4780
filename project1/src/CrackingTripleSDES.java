
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CrackingTripleSDES {
	
	static List<String> pkey = new ArrayList<>();
	
	static byte[] plaintext;
	public static void main(String[] args) throws InterruptedException, IOException {				
		
		char array[] = {'0', '1'};        
		allPermutations(array, "", array.length, 10);
        fileDecryptionSDES();
     }
	
	public static void allPermutations(char array[], String prefix, int n, int m) {         
        if (m == 0) {
        	pkey.add(prefix);
        	return;
        }
        
        for (int i = 0; i < n; ++i) {             
            String theNewPrefix = prefix + array[i];           
            allPermutations(array, theNewPrefix, n, m-1); 
        }
    }
	
	public static byte[] withKeyDecrypt(String encoding, String rawKey1, String rawKey2) {
		
		byte[] results = new byte[encoding.length()];
		for (int a = 0, b = 0; b < encoding.length();) {
			a = b + 8;			
			byte[] temp = TripleSDES.Decrypt(SDES.convertToByte(rawKey1), SDES.convertToByte(rawKey2),  Arrays.copyOfRange(SDES.convertToByte(encoding), b, a));
			for (int c = 0, d = b; c < temp.length; d++, c++) {
				results[d] = temp[c];
			}
			b += 8;
		}						
		return results;
	}

	public static byte[] fileDecryptionSDES() {

		Path pathFollowed = Paths.get("msg2.txt");
		
		try (Stream<String> lines = Files.lines(pathFollowed)) {

			lines.forEach(s -> {
				pkey.forEach( rawkey1 -> {
					System.out.println("--------Looking for the key: " +rawkey1);
					pkey.forEach(rawkey2 -> {
						byte[] results = withKeyDecrypt(s, rawkey1, rawkey2);					
						String stringCASCII = CASCII.toString(results);												
						if(stringCASCII.contains("---------THERE ARE NO SECRETS-----------")){
							System.out.println("------------The message is: " +stringCASCII+" --------------.First key is: " +rawkey1 + " and---------- Second is: "+rawkey2);
							return;
						}
						
					});				
													
				});				
			});

		} catch (IOException ex) {
			System.out.println("Error while reading file: " + ex);
		}
		return null;
	}
}
