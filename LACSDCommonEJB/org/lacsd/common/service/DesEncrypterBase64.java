package org.lacsd.common.service;

/******************************************************************************
//* Copyright (c) 2008 Sanitation Districts of Los Angeles. All Rights Reserved.
//* Filename:       DesEncrypterBase64
//* Revision:       
//* Author:         hho 
//* Created On:     07-15-2008
//* Modified By:
//* Modified On:
//*                 
//* Description:	64 bits encrypt and decrypt String
//* Modify from  free source code downloaded from The Java Developers Almanac
/******************************************************************************/

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;

public class DesEncrypterBase64 {
	private Cipher encryptCipher; 
	private Cipher decryptCipher; 

	private sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
	private sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 


	public DesEncrypterBase64() throws SecurityException { 

		java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE()); 
		char[] pass = "CHANGE THIS TO A BUNCH OF RANDOM CHARACTERS".toCharArray(); 
		byte[] salt = { 
		(byte) 0xa3, (byte) 0x21, (byte) 0x24, (byte) 0x2c, 
		(byte) 0xf2, (byte) 0xd2, (byte) 0x3e, (byte) 0x19}; 
		int iterations = 3; 
		init(pass, salt, iterations); 
	} 

	public void init(char[] pass, byte[] salt, int iterations) throws SecurityException { 
		try { 
			PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20); 
			SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES"); 
			SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(pass)); 
			encryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding"); 
			encryptCipher.init(Cipher.ENCRYPT_MODE, k, ps); 
			decryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding"); 
			decryptCipher.init(Cipher.DECRYPT_MODE, k, ps); 
		} 
		catch (Exception e) { 
			throw new SecurityException("Could not initialize CryptoLibrary: " + e.getMessage()); 
		} 
	} 

	/** 
	* convenience method for encrypting a string. 
	* @param str Description of the Parameter 
	* @return String the encrypted string. 
	* @exception SecurityException Description of the Exception 
	*/ 

	public synchronized String encrypt(String str) throws SecurityException { 
		try { 
			byte[] utf8 = str.getBytes("UTF8"); 
			byte[] enc = encryptCipher.doFinal(utf8); 
			return encoder.encode(enc); 
		} catch (Exception e) { 
			throw new SecurityException("Could not encrypt: " + e.getMessage()); 
		} 
	} 

	/** 
	* convenience method for encrypting a string. 
	* @param str Description of the Parameter 
	* @return String the encrypted string. 
	* @exception SecurityException Description of the Exception 
	*/ 

	public synchronized String decrypt(String str) throws SecurityException { 
		try { 
			byte[] dec = decoder.decodeBuffer(str); 
			byte[] utf8 = decryptCipher.doFinal(dec); 
			return new String(utf8, "UTF8"); 
		}catch (Exception e) { 
			throw new SecurityException("Could not decrypt: " + e.getMessage()); 
		} 
	} 


    public static void main(String args[]) {
		try{

		   // DesEncrypterBase64 encrypter = new DesEncrypterBase64();
			/*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String encrypted = "";
			String orgInput = "";
			System.out.print("\n\n***************************************");
			while (true)  {
				System.out.print("\n\nENTER A STRING or PRESS ENTER TO EXIT: ");
				orgInput = br.readLine();
				if (orgInput.trim().length() == 0 || orgInput.charAt(0) == '\n') {
					break;
				}else {
					encrypted = encrypter.encrypt(orgInput);
					if (encrypted == null) {
						System.out.println("THE INPUT STRING CANNOT BE ENCRYPTED.  PLEASE TRY AGAIN.");
					} else {
						System.out.println("\n\nTHE ENCRYPTED STRING IS: " + encrypted);
					}
				}
			}*/
	    } catch (Exception e) {
	    	e.printStackTrace();
  		}

	}

}
