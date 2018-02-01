

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CrackingSDES {
	static List<String> pkey = new ArrayList<>();
	
	static byte[] plaintext;
	public static void main(String[] args) {		
		final String text_encode = "CRYPTOGRAPHY";
		final String raw_Key = "0111001101";
		byte[] results_encrypted = withKeyEncryption(text_encode, raw_Key);
		System.out.print("\nText Encryption is as following :");
		String encryption_test= "";
		for (byte result : results_encrypted) {
			System.out.print(result);
			encryption_test = encryption_test.concat(Byte.toString(result));			
		}
		results_encrypted = decryptwithKey(encryption_test, raw_Key);
		String casciiString = CASCII.toString(results_encrypted);
		System.out.println("\n The word before we encrypt was : "+casciiString);
		char array[] = {'0', '1'};        
		allPermutations(array, "", array.length, 10);
        fileDecryptionSDES();
	}
	
	public static void allPermutations(char array[], String prefix, int a, int b) {         
        if (b == 0) {
        	pkey.add(prefix);
        	return;
        }
        
        for (int i = 0; i < a; ++i) {             
            String newPrefix = prefix + array[i];           
            allPermutations(array, newPrefix, a, b-1); 
        }
    }
	public static byte[] decryptwithKey(String encryption_test, String rawKey) {
		
		byte[] results = new byte[encryption_test.length()];
		for (int n = 0, p = 0; p < encryption_test.length();) {
			n = p + 8;			
			byte[] temp = SDES.Decrypt(SDES.convertToByte(rawKey), Arrays.copyOfRange(SDES.convertToByte(encryption_test), p, n));
			for (int y = 0, x = p; y < temp.length; x++, y++) {
				results[x] = temp[y];
				}
			p += 8;
		}						
		return results;
	}
	public static byte[] withKeyEncryption(String text_encode, String rawKey) {

		byte[] casciiEncoded = CASCII.Convert(text_encode);
		System.out.print(" Endoded byte in CASCII  is :");
		for (byte each : casciiEncoded) {
			System.out.print(each);
		}
		byte[] results = new byte[casciiEncoded.length];
		for (int c = 0, d = 0; d < casciiEncoded.length;) {
			c = d + 8;
			byte[] temp = SDES.Encrypt(SDES.convertToByte(rawKey), Arrays.copyOfRange(casciiEncoded, d, c));
			for (int k = 0, m = d; k < temp.length; m++, k++) {
				results[m] = temp[k];
			}
			d += 8;
		}
		return results;
	}
	public static byte[] fileDecryptionSDES() {

		Path pathfollowed = Paths.get("msg1.txt");
		
		try (Stream<String> line = Files.lines(pathfollowed)) {
			line.forEach(s -> {
				pkey.forEach( key -> {
					byte[] resultsAfterDecryption = decryptwithKey(s, key);					
					String casciiString = CASCII.toString(resultsAfterDecryption);					
					System.out.println("The message after decryption  is: " + casciiString +" And key is: " +key);
					if(casciiString.contains("CRYPTOGRAPHY")){
						System.out.println("The message is: " + casciiString +" And key is: " +key);
						return;
					}									
				});				
			});

		} catch (IOException l) {
			System.out.println(" while reading file THE ERROR is : " + l);
		}
		return null;
	}
}
