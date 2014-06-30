package com;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PassHash {
	
	private static final int HASH_BYTE_SIZE = 24;
	private static final int SALT_BYTE_SIZE = 24;
	private static final int ITERATIONS = 1000;
	private String clearTextPassword;
	
	
	public PassHash(){
		
	}
	
	/**
	 * Method for CLASS EXTENDER to generate hash password
	 * @param clearTextPassword
	 * @return	secure hashed password
	 */
	public String getHashedPassword(String clearTextPassword){
		this.clearTextPassword = clearTextPassword;
		return createHash();
	}
	
	/**
	 * Method for CLASS EXTENDER to validate the password
	 * @param password
	 * @param storedHash
	 * @return true	if password matches, else false
	 */
	public boolean validatePassword(String password, String storedHash){
		return authenticate(password, storedHash);
	}
	
	/**
	 * This createHash() method generates hashed password string 
	 * Can be used by CLASS CREATOR 
	 * @return
	 */
	private String createHash() {
		// TODO Auto-generated method stub
		try {
			SecureRandom rand = new SecureRandom();//.getInstance("SHA1PRNG", "SUN");
			byte[] salt = new byte[SALT_BYTE_SIZE];
			rand.nextBytes(salt);
			
			//takes a password, salt, iteration count, and to-be-derived key length for generating PBEKey of variable-key-size PBE ciphers
			PBEKeySpec keyspec = new PBEKeySpec(clearTextPassword.toCharArray(), salt, ITERATIONS, HASH_BYTE_SIZE*8);
			
			SecretKeyFactory sf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = sf.generateSecret(keyspec).getEncoded();
			
			//Encoded string
			String hashString = ITERATIONS + ":" + byteToHex(salt) + ":" + byteToHex(hash);
			return hashString;
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This authenticate() method checks the authenticity of password
	 * Can be used by CLASS CREATOR 
	 * @param password
	 * @param storedHash
	 * @return
	 */
	private boolean authenticate(String password, String storedHash) {
		// TODO Auto-generated method stub
		char[] pass = password.toCharArray();
		
		//Decode the storedHash string
		String[] keys = storedHash.split(":");
		
		int iteration = Integer.parseInt(keys[0]);
		byte[] salt = hexTobyte(keys[1]);
		byte[] hash = hexTobyte(keys[2]);
		//System.out.println("Stored hash: "+storedHash);
		
		try{
			PBEKeySpec spec = new PBEKeySpec(pass, salt, iteration, hash.length * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                
			byte[] genhash =  skf.generateSecret(spec).getEncoded();
        
		
			System.out.println("Gen:"+genhash);
			System.out.println("Orig:"+hash);
			boolean res = Arrays.equals(hash, genhash);
			return res;
		
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * This hexToString() method converts hex string to byte
	 * Can be used by CLASS CREATOR 
	 * @param hex
	 * @return
	 */
	private byte[] hexTobyte (String hex){
		//byte[] binary = new BigInteger(hex, 16).toByteArray();
		//return binary;
		byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
	}
	
	/**
	 * This byteToHex() method converts hex string to bytes
	 * Can be used by CLASS CREATOR 
	 * @param binary
	 * @return
	 */
	private String byteToHex (byte[] binary){
		BigInteger bi = new BigInteger(1, binary);
        String hex = bi.toString(16);
        int padlen = (binary.length * 2) - hex.length();
        //length should be even else pad it 
        if(padlen > 0)
            return String.format("%0" + padlen + "d", 0) + hex;
        else
            return hex;
	}
	
	/**
	 * Testing the basic functionalities
	 * @param args -not using
	 */
	public static void main(String args[]){
		PassHash ph = new PassHash();
		String x = ph.getHashedPassword("parseit");
		System.out.println(x);
		System.out.println(ph.validatePassword("parseit", x));
	}
}
