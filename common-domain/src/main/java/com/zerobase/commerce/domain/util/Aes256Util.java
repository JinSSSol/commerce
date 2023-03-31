package com.zerobase.commerce.domain.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Aes256Util {

	public static String alg = "AES/CBC/PKCS5Padding";
	private static final String KEY = "01234567890123456789012345678901";
	private static final String IV = KEY.substring(0, 16);

	public static String encrypt(String text) {

		try {
			Cipher cipher = Cipher.getInstance(alg);
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

			byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(encrypted);

		} catch (Exception e) {
			return null;
		}

	}

	public static String decrypt(String cipherText) {
		try {
			Cipher cipher = Cipher.getInstance(alg);
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

			byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
			byte[] decrypted = cipher.doFinal(decodedBytes);
			return new String(decrypted, "UTF-8");

		} catch (Exception e) {
			return null;
		}

	}
}
