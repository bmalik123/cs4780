

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Scanner;

public class ChangeByte {
	
	public static void main(String[] args) {
		Scanner changebyteSC = new Scanner(System.in);
		System.out.print("--------------------.signed file to temper----------------\n");
		String theFilename = changebyteSC.next();
		File tamperingFile = new File(theFilename);
		byte[] tamperingBinary = byteFetcher(tamperingFile);
		System.out.print("*********Total bytes (0-" + (tamperingBinary.length-1) + ") to tamper: ");
		String dumpingInput = changebyteSC.next();
		while(!dumpingInput.matches("\\d+") || (Integer.parseInt(dumpingInput) < 0 || Integer.parseInt(dumpingInput) >= tamperingBinary.length)){
			System.out.print("Enter an number between 0 and " + (tamperingBinary.length-1) + "to temper bytes: ");
			dumpingInput = changebyteSC.next();
		}
		
		int positionByte = Integer.parseInt(dumpingInput);
		
		isError(tamperingFile,positionByte,tamperingBinary);
		
		changebyteSC.close();
		System.exit(0);
	}
	public static byte[] byteFetcher(File file) {
		
		byte[] binaryFile = new byte[(int)file.length()];		
		
		try{			
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			inputStream.read(binaryFile); 
			inputStream.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return binaryFile;
	}
	


	public static void isError(File tamfile, int bytePos, byte[] binarray){
		byte[] randomByte = new byte[1];
		new Random().nextBytes(randomByte);
		
		while(randomByte[0] == binarray[bytePos])
			new Random().nextBytes(randomByte);
					
		final int SIDERANGE = 2; 
		int min = 0;
		if(binarray.length > SIDERANGE * 2 + 1){
			min = bytePos - (SIDERANGE);
			while(min < 0) min++;
			while(min + (SIDERANGE * 2) > binarray.length) min--;
		}
			
		binarray[bytePos] = randomByte[0];
								
		try {
			OutputStream fileOut = null;
			try {
				fileOut = new BufferedOutputStream(new FileOutputStream(tamfile));
				fileOut.write(binarray);
			} finally {
				fileOut.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n The file done tempring with byte index " + bytePos);
	}
}
