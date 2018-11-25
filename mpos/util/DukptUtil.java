package com.mpos.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DukptUtil {
	
	
	
	public static byte[] generateIPEK(byte[] ksn, byte[] bdk){
		byte[] result;
		byte[] temp, temp2, keyTemp;

		result = new byte[16];
		temp = new byte[8];
		keyTemp = new byte[16];

		// Array.Copy(bdk, keyTemp, 16);
		System.arraycopy(bdk, 0, keyTemp, 0, 16); // Array.Copy(bdk, keyTemp,
													// 16);
		// Array.Copy(ksn, temp, 8);
		System.arraycopy(ksn, 0, temp, 0, 8); // Array.Copy(ksn, temp, 8);
		temp[7] &= 0xE0;
		// TDES_Enc(temp, keyTemp, out temp2);
		temp2 = triDesEncryption(keyTemp, temp); // TDES_Enc(temp, keyTemp, out
													// temp2);temp
		// Array.Copy(temp2, result, 8);
		System.arraycopy(temp2, 0, result, 0, 8); // Array.Copy(temp2, result,
													// 8);
		keyTemp[0] ^= 0xC0;
		keyTemp[1] ^= 0xC0;
		keyTemp[2] ^= 0xC0;
		keyTemp[3] ^= 0xC0;
		keyTemp[8] ^= 0xC0;
		keyTemp[9] ^= 0xC0;
		keyTemp[10] ^= 0xC0;
		keyTemp[11] ^= 0xC0;
		// TDES_Enc(temp, keyTemp, out temp2);
		temp2 = triDesEncryption(keyTemp, temp); // TDES_Enc(temp, keyTemp, out
													// temp2);
		// Array.Copy(temp2, 0, result, 8, 8);
		System.arraycopy(temp2, 0, result, 8, 8); // Array.Copy(temp2, 0,
													// result, 8, 8);
		return result;
		
	}
	
	public static byte[] getDataKey(byte[] ksn, byte[] ipek) {
		byte[] temp1 = getDataKeyVariant(ksn, ipek);
		byte[] temp2 = temp1;

		byte[] key = triDesEncryption(temp2, temp1);

		return key;
	}
	
	public static byte[] getDataKeyVariant(byte[] ksn, byte[] ipek) {
		byte[] key;

		key = getDUKPTKey(ksn, ipek);
		key[5] ^= 0xFF;
		key[13] ^= 0xFF;

		return key;
	}
	
	public static byte[] getDUKPTKey(byte[] ksn, byte[] ipek){
		byte[] key;
		byte[] cnt;
		byte[] temp;
		// byte shift;
		int shift;

		key = new byte[16];
		// Array.Copy(ipek, key, 16);
		System.arraycopy(ipek, 0, key, 0, 16);

		temp = new byte[8];
		cnt = new byte[3];
		cnt[0] = (byte) (ksn[7] & 0x1F);
		cnt[1] = ksn[8];
		cnt[2] = ksn[9];
		// Array.Copy(ksn, 2, temp, 0, 6);
		System.arraycopy(ksn, 2, temp, 0, 6);
		temp[5] &= 0xE0;

		shift = 0x10;
		while (shift > 0) {
			if ((cnt[0] & shift) > 0) {
				// System.out.println("**********");
				temp[5] |= shift;
				_nrkgp(key, temp);
			}
			shift >>= 1;
		}
		shift = 0x80;
		while (shift > 0) {
			if ((cnt[1] & shift) > 0) {
				// System.out.println("&&&&&&&&&&");
				temp[6] |= shift;
				_nrkgp(key, temp);
			}
			shift >>= 1;
		}
		shift = 0x80;
		while (shift > 0) {
			if ((cnt[2] & shift) > 0) {
				// System.out.println("^^^^^^^^^^");
				temp[7] |= shift;
				_nrkgp(key, temp);
			}
			shift >>= 1;
		}

		return key;
	}
	
	private static void _nrkgp(byte[] key, byte[] ksn) {

		byte[] temp, key_l, key_r, key_temp;
		int i;

		temp = new byte[8];
		key_l = new byte[8];
		key_r = new byte[8];
		key_temp = new byte[8];

		// Console.Write("");

		// Array.Copy(key, key_temp, 8);
		System.arraycopy(key, 0, key_temp, 0, 8);
		for (i = 0; i < 8; i++)
			temp[i] = (byte) (ksn[i] ^ key[8 + i]);
		// DES_Enc(temp, key_temp, out key_r);
		key_r = triDesEncryption(key_temp, temp);
		for (i = 0; i < 8; i++)
			key_r[i] ^= key[8 + i];

		key_temp[0] ^= 0xC0;
		key_temp[1] ^= 0xC0;
		key_temp[2] ^= 0xC0;
		key_temp[3] ^= 0xC0;
		key[8] ^= 0xC0;
		key[9] ^= 0xC0;
		key[10] ^= 0xC0;
		key[11] ^= 0xC0;

		for (i = 0; i < 8; i++)
			temp[i] = (byte) (ksn[i] ^ key[8 + i]);
		// DES_Enc(temp, key_temp, out key_l);
		key_l = triDesEncryption(key_temp, temp);
		for (i = 0; i < 8; i++)
			key[i] = (byte) (key_l[i] ^ key[8 + i]);
		// Array.Copy(key_r, 0, key, 8, 8);
		System.arraycopy(key_r, 0, key, 8, 8);
	}
	
	
	public static byte[] triDesEncryption(byte[] byteKey, byte[] dec) {

		try {
			byte[] en_key = new byte[24];
			if (byteKey.length == 16) {
				System.arraycopy(byteKey, 0, en_key, 0, 16);
				System.arraycopy(byteKey, 0, en_key, 16, 8);
			} else if (byteKey.length == 8) {
				System.arraycopy(byteKey, 0, en_key, 0, 8);
				System.arraycopy(byteKey, 0, en_key, 8, 8);
				System.arraycopy(byteKey, 0, en_key, 16, 8);
			} else {
				en_key = byteKey;
			}
			SecretKeySpec key = new SecretKeySpec(en_key, "DESede");

			Cipher ecipher = Cipher.getInstance("DESede/ECB/NoPadding");
			ecipher.init(Cipher.ENCRYPT_MODE, key);

			// Encrypt
			byte[] en_b = ecipher.doFinal(dec);

			// String en_txt = parseByte2HexStr(en_b);
			// String en_txt =byte2hex(en_b);
			return en_b;
		} catch (Exception e) {
			//log.error("Err in triDesEncryption:" + e.toString());
		}
		return null;
	}
	
	
	public static byte[] TriDesEncryptionCBC(byte[] byteKey, byte[] dec){
		byte[] en_key = new byte[24];
		if (byteKey.length == 16) {
			System.arraycopy(byteKey, 0, en_key, 0, 16);
			System.arraycopy(byteKey, 0, en_key, 16, 8);
		} else if (byteKey.length == 8) {
			System.arraycopy(byteKey, 0, en_key, 0, 8);
			System.arraycopy(byteKey, 0, en_key, 8, 8);
			System.arraycopy(byteKey, 0, en_key, 16, 8);
		} else {
			en_key = byteKey;
		}
		
		try {
			Key deskey = null;
			byte[] keyiv = new byte[8];
			DESedeKeySpec spec = new DESedeKeySpec(en_key);
			SecretKeyFactory keyfactory = SecretKeyFactory
					.getInstance("desede");
			deskey = keyfactory.generateSecret(spec);

			Cipher cipher = Cipher.getInstance("desede" + "/CBC/NoPadding");
			IvParameterSpec ips = new IvParameterSpec(keyiv);

			cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);

			byte[] de_b = cipher.doFinal(dec);

			return de_b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
