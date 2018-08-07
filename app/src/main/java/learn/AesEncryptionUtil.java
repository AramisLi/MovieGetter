package learn;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptionUtil {

    public static String byte2hex(byte[] paramArrayOfByte)
    {
        StringBuffer localStringBuffer = new StringBuffer(paramArrayOfByte.length * 2);
        int i = 0;
        while (i < paramArrayOfByte.length)
        {
            String str = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
            if (str.length() == 1) {
                localStringBuffer.append("0");
            }
            localStringBuffer.append(str);
            i += 1;
        }
        return localStringBuffer.toString().toUpperCase();
    }


    private static IvParameterSpec createIV(String paramString) {
        String str = paramString;
        if (paramString == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder(16);
        sb.append(str);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            byte[] result = sb.toString().getBytes("UTF-8");
            return new IvParameterSpec(result);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static SecretKeySpec createKey(String paramString) {
        String str = paramString;
        if (paramString == null) {
            str = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(str);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        try {
            byte[] result = sb.toString().getBytes("UTF-8");
            return new SecretKeySpec(result, "AES");
        } catch (UnsupportedEncodingException ex) {
            for (; ; ) {
                ex.printStackTrace();
            }
        }
    }

    public static String decrypt(String paramString1, String paramString2, String paramString3) {
        byte[] result1;
        byte[] result2 = new byte[0];
        result1 = hex2byte(paramString1);
        result2 = decrypt(result1, paramString2, paramString3);
        try {
            return new String(result2, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static byte[] decrypt(byte[] paramArrayOfByte, String paramString1, String paramString2) {
        try {
            SecretKeySpec spec = createKey(paramString1);
            Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            localCipher.init(2, spec, createIV(paramString2));
            paramArrayOfByte = localCipher.doFinal(paramArrayOfByte);
            return paramArrayOfByte;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String paramString1, String paramString2, String paramString3)
    {
        String localObject = null;
        try
        {
            byte[] result = paramString1.getBytes("UTF-8");
            return byte2hex(encrypt(result, paramString2, paramString3));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return localObject;
    }


    public static byte[] encrypt(byte[] paramArrayOfByte, String paramString1, String paramString2) {
        try {
            SecretKeySpec spec = createKey(paramString1);
            Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            localCipher.init(1, spec, createIV(paramString2));
            paramArrayOfByte = localCipher.doFinal(paramArrayOfByte);
            return paramArrayOfByte;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static byte[] hex2byte(String paramString) {
        if ((paramString == null) || (paramString.length() < 2)) {
            return new byte[0];
        }
        String str = paramString.toLowerCase();
        int j = str.length() / 2;
        byte[] arrayOfByte = new byte[j];
        byte[] result;
        int i = 0;
        for (; ; ) {
            result = arrayOfByte;
            if (i >= j) {
                break;
            }
            arrayOfByte[i] = ((byte) (Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16) & 0xFF));
            i += 1;
        }
        return result;
    }
}
