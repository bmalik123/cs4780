

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigitalSignature {
	
	public static final int bufferSize = 1024;	
	public static void writeSigned(File filenameSigned, BigInteger[] prKey){
		
		File fileSigned = null;
		try {
			fileSigned = new File(filenameSigned.getCanonicalPath() + ".signed");
			boolean madeFile = false;
			
			if (!fileSigned.exists()) {
				fileSigned.createNewFile();
			} else {
				madeFile = true;
			}
			if (!madeFile) {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			InputStream inputFile = new BufferedInputStream(new FileInputStream(filenameSigned));
			for (int got = 1; got > 0;) {
				int left = inputFile.available();
				byte[] read = new byte[left < bufferSize ? left : bufferSize];
				got = inputFile.read(read, 0, read.length);
				msgDigest.update(read);
			}
			inputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] bytesDigested = msgDigest.digest();
		//printArray(digestBytes);
		BigInteger digest = new BigInteger(1, bytesDigested);
		
		if (KeyGen.flag) {			
			//printArray(digest.toByteArray());
		}

		BigInteger digestSignature = digest.modPow(prKey[1], prKey[0]);

		try {
			ObjectOutputStream signedFileOut = new ObjectOutputStream(new FileOutputStream(fileSigned));
			signedFileOut.writeObject(digestSignature);

			InputStream inputFile = new BufferedInputStream(new FileInputStream(filenameSigned));
			for (int m = 1; m > 0;) {
				int left = inputFile.available();
				byte[] read = new byte[left < bufferSize ? left : bufferSize];
				m = inputFile.read(read, 0, read.length);
				signedFileOut.write(read);
			}
			inputFile.close();

			signedFileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean verify(File file, BigInteger[] publicKey) {
		
		
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		ObjectInputStream fileIn = null;
		try {
			fileIn = new ObjectInputStream(new FileInputStream(file));
			BigInteger encryptedDigest = (BigInteger) fileIn.readObject();
			BigInteger decryptedDigest = encryptedDigest.modPow(publicKey[1], publicKey[0]);
			byte[] digest = decryptedDigest.toByteArray();

			for (int got = 1; got > 0;) {
				int left = fileIn.available();
				byte[] read = new byte[left < bufferSize ? left : bufferSize];
				got = fileIn.read(read, 0, read.length);
				messageDigest.update(read);
			}

			fileIn.close();

			byte[] calculatedDigest = messageDigest.digest();			
			calculatedDigest = (new BigInteger(1, calculatedDigest)).toByteArray();

			if (KeyGen.flag) {				
				//printArray(digest);
				//printArray(calculatedDigest);
			}
			return checkDigest(digest, calculatedDigest);
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileIn != null) {
				try {
					fileIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	private static boolean checkDigest(byte[] b1, byte[] b2) {
		if (b1.length != b2.length) {
			return false;
		}
		for (int i = 0; i < b1.length; i++) {
			if (b1[i] != b2[i])
				return false;
		}
		return true;
	}

}
