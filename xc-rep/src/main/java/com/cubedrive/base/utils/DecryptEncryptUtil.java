/**
 * Enterprise Spreadsheet Solutions
 * Copyright(c) FeyaSoft Inc. All right reserved.
 * info@enterpriseSheet.com
 * http://www.enterpriseSheet.com
 * 
 * Licensed under the EnterpriseSheet Commercial License.
 * http://enterprisesheet.com/license.jsp
 * 
 * You need to have a valid license key to access this file.
 */
package com.cubedrive.base.utils;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.cubedrive.base.BaseAppConfiguration;
import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.exception.DecryptEncryptError;
import com.google.common.io.BaseEncoding;

public class DecryptEncryptUtil {
	
	public static boolean needSimpleEncrypt() {
		boolean result = true;
		
		// check app.idEncrypt.level=normal / high
		String clientKey = BaseAppConfiguration.instance().getIdEncryptLevel();
		if (clientKey != null && !clientKey.equalsIgnoreCase(BaseAppConstants.SECURITY_NORMAL)) result = false;
		
		return result;
	}
	
	public static DesEncrypter getDesEncrypter(){
		return new DesEncrypter(BaseAppConstants.DEFAULT_KEY);
	}

	// this is private - need pass username to process
	public static DesEncrypter getDesEncrypterForFile(String username) {
		if (needSimpleEncrypt()) return new DesEncrypter(BaseAppConstants.MY_FILE_KEY);
		else return new DesEncrypter(BaseAppConstants.MY_FILE_KEY + username);
	}

	public static DesEncrypter getDesEncrypterForFile(){
        return new DesEncrypter(BaseAppConstants.MY_FILE_KEY);
	}


    public static DesEncrypter getDesEncrypterForAttachment(){
        return new DesEncrypter(BaseAppConstants.ATTACHMENT_KEY);
    }
	
	private static final int iterationCount = 19;

	private static final byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8,
			(byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

	

	// this is for link send to email when regiser
	

	public static class DesEncrypter {

		private final Cipher ecipher;

		private final Cipher dcipher;

		private DesEncrypter(String passPhrase){
			try {
				KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(),
						salt, iterationCount);
				SecretKey key = SecretKeyFactory
						.getInstance("PBEWithMD5AndDES")
						.generateSecret(keySpec);
				ecipher = Cipher.getInstance(key.getAlgorithm());
				dcipher = Cipher.getInstance(key.getAlgorithm());

				// Prepare the parameter to the ciphers
				AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
						iterationCount);

				// Create the ciphers
				ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
				dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			} catch (Exception e) {
				throw new DecryptEncryptError(e);
			}
		}

		public String doEncrypt(String plainText){
			byte[] plainBytes = plainText.getBytes(BaseAppConstants.utf8);
            try{
                byte[] encBytes = ecipher.doFinal(plainBytes);
                return BaseEncoding.base64().encode(encBytes);
            }catch (Exception e){
                throw new DecryptEncryptError(e);
            }
		}

		public String doDecrypt(String base64EncryptedText){
            try{
            	byte[] encBytes = BaseEncoding.base64().decode(base64EncryptedText);
			    byte[] plainBytes = dcipher.doFinal(encBytes);
			    return new String(plainBytes, BaseAppConstants.utf8);
            }catch (Exception e){
                throw new DecryptEncryptError(e);
            }
		}

		public String doEncryptUrl(String plainUrl) {
			String base64EncryptedUrl = doEncrypt(plainUrl);
			return base64EncryptedUrl.replace("+", "-").replace("=", "_").replace("/", "*");
		}

		public String doDecryptUrl(String encryptedUrl){
			return doDecrypt(encryptedUrl.replace("_", "=").replace("-", "+").replace("*", "/"));
		}

	}

}
