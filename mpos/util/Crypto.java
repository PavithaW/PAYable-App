package com.mpos.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Crypto {
	
	private static SecureRandom random;
	
	static{
		random = new SecureRandom();
	}
	
	public static long generateLongToken(){
		return new BigInteger(64, random).longValue() ;
	}
	
	public static int generateIntToken(){
		return random.nextInt() ;
	}
	
	public static String generateMD5(String s) throws NoSuchAlgorithmException {

		MessageDigest digest = null;
		
		digest = MessageDigest.getInstance("MD5");
		
		digest.reset();
	    byte[] data = digest.digest(s.getBytes());
	    return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));


	}
	
	public static String generateSHA1(String s) {

		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		digest.reset();
	    byte[] data = digest.digest(s.getBytes());
	    return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));


	}
	
	public static String hexToASCII(String hexValue){
	    StringBuilder output = new StringBuilder("");
	    for (int i = 0; i < hexValue.length(); i += 2)
	    {
	        String str = hexValue.substring(i, i + 2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	    return output.toString();
	}
	
	public static String asciiToHex(String asciiValue){
	    char[] chars = asciiValue.toCharArray();
	    StringBuffer hex = new StringBuffer();
	    for (int i = 0; i < chars.length; i++)
	    {
	        hex.append(Integer.toHexString((int) chars[i]));
	    }
	    return hex.toString();
	}
	
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

}
