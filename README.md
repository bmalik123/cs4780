 project2
# cs4780-Cryptography
Project 2
A RSA based Digital Signature System
 

One submission/group

In this project, you are going to implement a RSA based digital signature system. Please check the textbook or slides for the detailed description of the algorithm. You should use Java to implement the algorithm. In Java, class BigInteger provides the functionality to deal with large n-bit numbers.

1. Write a program KeyGen.java to implement the key generation part of the RSA system.

·         Pick p and q to be random primes of some specified length using the appropriate BigInteger constructor for Java.

·         Calculate n =  p x q

·         Calculate ø(n) = ( p-1)x(q-1)  

·         Pick e to be a random prime integer between 1 and ø(n), such that gcd(e, ø(n)) = . e should be similar in (bit) length to p and q, but does not have to be the same length.

·         Calculate  d = e-1 mod ø(n) :

In BigInteger the method used for this purpose is

public BigInteger modInverse(BigInteger m)

When you execute this program, it should generate new public and private keys for your RSA cryptosystem, where p, q and e as defined above are all 512-bit integers and n  should be ~1024 bits. Your program should output all three values e, d and n to the console as it generates them.  The values e and n should also be saved to a file called "pubkey.rsa" and the values d and n should be saved to a file "privkey.rsa".  To allow for nice access of these files, you MUST output and input these keys to and from the files using a Java ObjectOutputStream and ObjectInputStream.

2. Write a second program DigitalSignature.java that uses your RSA algorithm for a primitive digital signature system

A digital signature is a way for the receiver to make sure a message has not been forged or tampered with.  The basic idea is that the sender processes the message using some message digest system such as MD5 to form the "digest".  The sender then produces a signature by encrypting the digest using his/her private key and appends the signature to the end of the message.  Thus, the message itself is unencrypted plaintext (however, the sender could encrypt the entire signed message with the receiver's public key if he/she wanted it to be private).  The receiver then decrypts the signature using the sender's public key, processes the message in the same way the sender did, and verifies that the digests "match".  The basis for the success of this technique is the ability to process the message in a way that is fast yet that can detect (almost) any alteration to the message.  

Details:

Your goal is for the receiver of a message to be able to discover any tampering with the message sent by the sender, and, assuming that the sender's private key has not been compromised, to verify that the sender is indeed the one who sent the message.

Sender:

The sender of the message will create the message itself in plaintext.  This can be any text message stored in any file on the sender's computer.  Before "sending" the message, the sender will do the following:

1)      Process the plaintext file in the following way:

a) Read in the file byte by byte, adding each to an MD5 instance of a MessageDigest.  Once you have added the complete file to the MessageDigest, obtain the 128 bit (16 byte) digest array value from it.  For more details on using a MessageDigest, see the documentation in the Java API and see MD.java.

b) You now have a digest array which is ready to be signed.  Using the sign/magnitude constructor for BigInteger in Java, convert this array into a single BigInteger.  Next sign the BigInteger using a proper key that you generated in Part 1 of the project using the RSA algorithm.  To make verification easier, prepend this value to your plaintext file (i.e. put it in the front of the file).   You can do this using an ObjectOutputStream – first write your BigInteger signature value, then write the rest of your file byte by byte.  Your message is now ready to "send".  However, for the purposes of this project you will simply leave it in the file.  If your original file was <filename>.<ext>, call this file <filename>.<ext>.signed

Receiver

a) Upon "receiving" the message the receiver first reads the BigInteger value (using an ObjectInputStream), then "encrypts" it using the proper key generated in Part 1 of the assignment using the RSA algorithm.  The resulting BigInteger is then converted back to an array of bytes using the toByteArray() method, with the result being the digest array that you will now use to check to see if the message has been tampered with. 

b) Read the remaining bytes of the file, adding them to a new MessageDigest object.  Once you have added all of the bytes, obtain the message digest for the received file.  If this exactly matches the transmitted message digest, the message has not been tampered with – otherwise it has been tampered with.  The receiver should output a message indicating whether or not the received file was valid.

3.Program Main.java for Second Part

Have a menu-driven loop that allows the user to "send" or "receive" files.  "Send" will simply prompt for an input filename and process the file as specified above.  "Receive" will also prompt for a filename (should be a .signed file which had previously been output from a "Send") and process it as above, outputting whether or not the file has been tampered with.

4. Execution and Testing

To allow for easier grading, "capture" an interactive session in which you test your program.  If you are using Windows, this may involve cutting and pasting the snapshots of your screen to a file demo.doc.  Use test.txt (adapted  from cnn.com) to test your program

1) In demo.doc,  you should first show your key generation by running your first program. 

2)  "Send" test.txt as described above, yielding the signed file "test.txt.signed".

3)  Print out the signed file (note that since your signature is written as a BigInteger object, the beginning of this file may not show correctly – this is ok). 

4)  Next "receive" "test.txt.signed" to show that it is ok

5)  Next, change one byte in "test.txt.signed" to show "tampering".  In order to do this so as not to (necessarily) cause an IOException with a Java ObjectInputStream, you need to write a simple program ChangeByte.java to change a byte.  This program should do the following:

a. Prompt the user for the input file name

b. Open the file as a binary file

c. Prompt the user for which byte to change (i.e. you are indexing the file byte by byte)

d. Change the byte to a random value and close the file

6) Finally, "receive" the corrupted file and through the receiver indicate that it has been corrupted.  Note that if the byte you changed was within the BigInteger object portion of the file, the result may be an IOException when you try to read in the BigInteger. This should also be an indicator of tampering.
=======
# cs4780
##Project 1

Simplified DES Block cipher

----------------------------------------------------------------------------------------------------------------

Note: 1) Please upload  your  files( SDES.java, TripleDES.java, the two java files for part 3 and Project1.doc) to  CSNS before  23:59:59pm, 03/31.

           2)This is group project. One submission per group!

           3)The number of the submitted files should be 5 in total. All the source code should be put in java files (not doc /pdf files).

           4) Project1.doc should  contain special instructions needed to run your programs, the answers to the problems and screenshots.

You'll  lose points if you fail to follow the instructions.

 

1. SDES

In this section, you will implement a simplified version of the DES block cipher algorithm. Naturally enough, it is called SDES, and it is designed to have the features of the DES algorithm but scaled down so it is more tractable to understand. (Note however, that SDES is in no way secure and should not be used for serious cryptographic applications.) Here is the detailed specifications of SDES.


SDES encryption takes a 10 bit raw key (from which two 8 bit keys are generated as describedin the handout) and encrypts an 8 bit plaintext to produce an 8 bit ciphertext.
Implement the SDES algorithm in a class called SDES. The encryption an decryption methods should match the interface below:
 
public static byte[] Encrypt(byte[] rawkey, byte[] plaintext)
public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext)
 
Here, rather than compactly representing the SDES plaintext and ciphertext using byte-sized (8-bit) variables, the data is represented using byte arrays of length 8. Similarly the 10 bit keys are represented as arrays of length 10. Although this design is extremely inefficient (it uses 8 times more space), it makes the algorithm easier to implement and experiment with.
For example, one might declare a 10-bit raw key in a test program like this:
byte key1[] = {1, 1, 1, 0, 0, 0, 1, 1, 1, 0};
To verify that your implementation of SDES is correct, try the following test cases:

Raw Key           Plaintext             Ciphertext

0000000000     10101010            00010001

1110001110     10101010            11001010

1110001110     01010101            01110000

1111111111     10101010            00000100

Use your implementation to complete the following table:

Raw Key         Plaintext             Ciphertext
 
0000000000     00000000                 ?
1111111111     11111111                 ?
0000011111     00000000                 ?
0000011111     11111111                 ?
1000101110     ?                       00011100
1000101110     ?                       11000010
0010011111     ?                       10011101
0010011111     ?                       10010000
 

Pleas check here for details of SDES.


2. TripleSDES
 
The DES algorithm uses keys of length 56 bits, which, when DES was originally designed, was thought to be secure enough to meet most needs. However, the increase in computing power makes it more tractable to brute-force crack a 56-bit key. Thus, an alternative version of DES using longer keys was desirable. The result, known as Triple DES uses two 56-bit raw keys k1 and k2 and is implemented by composing DES with itself three times in the following way:
E3DES(p) = EDES(k1,DDES(k2,EDES(k1, p)))
Here, p is the plaintext to encrypt, EDES is the usual DES encryption algorithm and DDES is the DES decryption algorithm. This strategy doubles the number of bits in the key, at the expense of performing three times as many calculations. This approach was shown to offer only the security of a 57-bit key rather than 112 bits as you might expect.)
The TripleDES decryption algorithm is just the reverse:
D3DES(c) = DDES(k1,EDES(k2,DDES(k1, c)))
For this part of the project, implement a class called TripleSDES that provides the following methods and calculates the TripleSDES encryption.
 
public static byte[] Encrypt( byte[] rawkey1, byte[] rawkey2, byte[] plaintext )
public static byte[] Decrypt( byte[] rawkey1, byte[] rawkey2, byte[] ciphertext )
 
Use your implementation to complete the following table:
 
Raw Key 1        Raw Key        Plaintext         Ciphertext
 
0000000000     0000000000     00000000         ?
1000101110     0110101110     11010111         ?
1000101110     0110101110     10101010         ?
1111111111     1111111111     10101010         ?
1000101110     0110101110     ?                     11100110
1011101111     0110101110     ?                     01010000
0000000000     0000000000     ?                     10000000
1111111111     1111111111     ?                     10010010
 
3. Cracking SDES and TripleSDES
 
For this part of the project, you will use your SDES imlementation and brute-force search to crack some encoded English messages. This would be quite straightforward if the input text used standard ASCII encodings, because you can test each key to see if it generates output that is purely alphanumeric (almost all of the wrong outputs will contain random ASCII gibberish). To make the problem more interesting, the text in the messages here are encoded using Compact ASCII, or CASCII for short.
 
CASCII characters are 5 bits long, which gives just enough space for the upper case letters and some punctuation: 0 = ’ ’ (space), 1–26 = ’A’–’Z’, 27=’,’, 28 = ’?’, 29=’:’, 30=’.’, 31 = ’‘’. The file CASCII.java  provides a definition of CASCII constants and some useful conversion functions. See the file comments for details. Although it should not affect the code you write for the project, CASCII uses big-endian encodings. For example, the letter ’T’ = 0 is represented by the bit sequence 00101. This may be useful when debugging your programs.
Since SDES and TripleSDES only work on blocks of size 8 bits, when converting a CASCII string it is necessary to pad the bit representation with 0’s to obtain a multiple of 8. See the convert and toString methods in CASCII.java.
 
1). Give the SDES encoding of the following CASCII plaintext using the key 0111001101. (The answer
is 64 bits long.)
CRYPTOGRAPHY
 
2). The message in the file msg1.txt was encoded using SDES. Decrypt it, and find the 10-bit raw
key used for its encryption.
 
3). The mesage in the file msg2.txt was encoded using TripleSDES. Decrypt it, and find the two
10-bit raw keys used for its encryption.

