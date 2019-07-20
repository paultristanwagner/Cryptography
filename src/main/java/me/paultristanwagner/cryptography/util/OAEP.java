package me.paultristanwagner.cryptography.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class OAEP {

    public static final SecureRandom random = new SecureRandom();

    public static void main( String[] args ) throws Exception {
        byte[] myMessage = "I wonder if this will work".getBytes( "UTF-8" );
        byte[] padded = pad( myMessage, "SHA-256 MGF1", myMessage.length + 32 + 32 + 1 );
        StringBuilder sb = new StringBuilder();
        for ( byte b : padded ) {
            sb.append( String.format( "%02X", b ) );
        }
        System.out.println( sb.toString() );
        byte[] unpadded = unpad( padded, "SHA-256 MGF1" );
        System.out.println( new String( unpadded, "UTF-8" ) );
    }

    public static byte[] SHA256(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input);
    }

    public static byte[] MGF1(byte[] seed, int seedOffset, int seedLength, int desiredLength) throws NoSuchAlgorithmException {
        int hLen = 32;
        int offset = 0;
        int i = 0;
        byte[] mask = new byte[desiredLength];
        byte[] temp = new byte[seedLength + 4];
        System.arraycopy(seed, seedOffset, temp, 4, seedLength);
        while (offset < desiredLength) {
            temp[0] = (byte) (i >>> 24);
            temp[1] = (byte) (i >>> 16);
            temp[2] = (byte) (i >>> 8);
            temp[3] = (byte) i;
            int remaining = desiredLength - offset;
            System.arraycopy(SHA256(temp), 0, mask, offset, remaining < hLen ? remaining : hLen);
            offset = offset + hLen;
            i = i + 1;
        }
        return mask;
    }

    public static byte[] unpad(byte[] message, String params) throws Exception {
        String[] tokens = params.split(" ");
        if (tokens.length != 2 || !tokens[0].equals("SHA-256") || !tokens[1].equals("MGF1")) {
            return null;
        }
        int mLen = message.length;
        int hLen = 32;
        if (mLen < (hLen << 1) + 1) {
            return null;
        }
        byte[] copy = new byte[mLen];
        System.arraycopy(message, 0, copy, 0, mLen);
        byte[] seedMask = MGF1(copy, hLen, mLen - hLen, hLen);
        for (int i = 0; i < hLen; i++) {
            copy[i] ^= seedMask[i];
        }
        byte[] paramsHash = SHA256(params.getBytes("UTF-8"));
        byte[] dataBlockMask = MGF1(copy, 0, hLen, mLen - hLen);
        int index = -1;
        for (int i = hLen; i < mLen; i++) {
            copy[i] ^= dataBlockMask[i - hLen];
            if (i < (hLen << 1)) {
                if (copy[i] != paramsHash[i - hLen]) {
                    return null;
                }
            } else if (index == -1) {
                if (copy[i] == 1) {
                    index = i + 1;
                }
            }
        }
        if (index == -1 || index == mLen) {
            return null;
        }
        byte[] unpadded = new byte[mLen - index];
        System.arraycopy(copy, index, unpadded, 0, mLen - index);
        return unpadded;
    }

    public static byte[] pad(byte[] message, String params, int length) throws Exception {
        String[] tokens = params.split(" ");
        if (tokens.length != 2 || !tokens[0].equals("SHA-256") || !tokens[1].equals("MGF1")) {
            return null;
        }
        int mLen = message.length;
        int hLen = 32;
        if (mLen > length - (hLen << 1) - 1) {
            return null;
        }
        int zeroPad = length - mLen - (hLen << 1) - 1;
        byte[] dataBlock = new byte[length - hLen];
        System.arraycopy(SHA256(params.getBytes("UTF-8")), 0, dataBlock, 0, hLen);
        System.arraycopy(message, 0, dataBlock, hLen + zeroPad + 1, mLen);
        dataBlock[hLen + zeroPad] = 1;
        byte[] seed = new byte[hLen];
        random.nextBytes(seed);
        byte[] dataBlockMask = MGF1(seed, 0, hLen, length - hLen);
        for (int i = 0; i < length - hLen; i++) {
            dataBlock[i] ^= dataBlockMask[i];
        }
        byte[] seedMask = MGF1(dataBlock, 0, length - hLen, hLen);
        for (int i = 0; i < hLen; i++) {
            seed[i] ^= seedMask[i];
        }
        byte[] padded = new byte[length];
        System.arraycopy(seed, 0, padded, 0, hLen);
        System.arraycopy(dataBlock, 0, padded, hLen, length - hLen);
        return padded;
    }
}
