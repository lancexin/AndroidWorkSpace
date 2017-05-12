package com.hostxin.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MD5 {
	
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };

	private static String getHex(byte tmp[]){
		int k = 0;
		char str[] = new char[16 * 2];
		for (int i = 0; i < 16; i++) {

			byte byte0 = tmp[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];

			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str).toUpperCase(Locale.getDefault());
	}
	
	public static String getMD5(byte[] b) {
		String s = null;

		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(b);
			byte tmp[] = md.digest();

			s = getHex(tmp);

		} catch (Exception e) {

		}
		return s;
	}
	
	public static String getMD5_30(String instr){
		String md5 = getMD5(instr);
		if(md5 == null || md5.length() != 32){
			return null;
		}
		return md5.substring(1, md5.length()-1);
	}

	public static String getMD5(String instr) {
		String s = null;
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(instr.getBytes());
			byte tmp[] = md.digest();

			s = getHex(tmp);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public static String getMD5(File file) {
		String s = null;
		FileInputStream fileInputStream = null;
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			byte tmp[] = md.digest();
			s =  getHex(tmp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return s;
		}
	}

}
