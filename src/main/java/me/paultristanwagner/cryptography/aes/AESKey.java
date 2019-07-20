package me.paultristanwagner.cryptography.aes;

import me.paultristanwagner.cryptography.util.ArrayUtils;
import me.paultristanwagner.cryptography.util.ProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * JavaDoc this file!
 * Created: 16.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class AESKey {

    private byte[] aesKeyEncoded;
    private byte[] initVector;

    public AESKey( byte[] aesKeyEncoded, byte[] initVector ) {
        this.aesKeyEncoded = aesKeyEncoded;
        this.initVector = initVector;
    }

    public byte[] encrypt( byte[] bytes, ProgressBar progressBar ) {
        if ( progressBar != null ) {
            progressBar.start();
        }
        try {
            IvParameterSpec iv = new IvParameterSpec( initVector );
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
            SecretKeySpec secretKeySpec = new SecretKeySpec( aesKeyEncoded, "AES" );
            cipher.init( Cipher.ENCRYPT_MODE, secretKeySpec, iv );
            byte[] encrypted = new byte[0];
            for ( int i = 0; i < bytes.length; ) {
                int remaining = bytes.length - i;
                int chunkSize = Math.min( remaining, 1000000 );
                byte[] chunk = new byte[chunkSize];
                System.arraycopy( bytes, i, chunk, 0, chunkSize );
                i += chunkSize;
                byte[] result = cipher.update( chunk );
                encrypted = ArrayUtils.concatByteArrays( encrypted, result );
                if ( progressBar != null ) {
                    progressBar.setProgress( (float) i / ( bytes.length - 1 ) );
                }
            }
            byte[] encryptedFinal = cipher.doFinal();
            encrypted = ArrayUtils.concatByteArrays( encrypted, encryptedFinal );
            if ( progressBar != null ) {
                progressBar.setProgress( 1 );
                progressBar.sync();
            }
            return encrypted;
        } catch ( NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException |
                IllegalBlockSizeException | InvalidAlgorithmParameterException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decrypt( byte[] encrypted, ProgressBar progressBar ) {
        if ( progressBar != null ) {
            progressBar.start();
        }
        try {
            IvParameterSpec iv = new IvParameterSpec( initVector );
            Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
            SecretKeySpec secretKeySpec = new SecretKeySpec( aesKeyEncoded, "AES" );
            cipher.init( Cipher.DECRYPT_MODE, secretKeySpec, iv );
            byte[] decrypted = new byte[0];
            for ( int i = 0; i < encrypted.length; ) {
                int remaining = encrypted.length - i;
                int chunkSize = Math.min( remaining, 1000000 );
                byte[] chunk = new byte[chunkSize];
                System.arraycopy( encrypted, i, chunk, 0, chunkSize );
                i += chunkSize;
                byte[] result = cipher.update( chunk );
                decrypted = ArrayUtils.concatByteArrays( decrypted, result );
                if ( progressBar != null ) {
                    progressBar.setProgress( (float) i / ( encrypted.length - 1 ) );
                }
            }
            byte[] decryptedFinal = cipher.doFinal();
            decrypted = ArrayUtils.concatByteArrays( decrypted, decryptedFinal );
            if ( progressBar != null ) {
                progressBar.setProgress( 1 );
                progressBar.sync();
            }
            return decrypted;
        } catch ( NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public static AESKey read( File file ) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new FileReader( file ) );
        JSONObject jsonObject = new JSONObject( bufferedReader.readLine() );
        JSONArray aesKeyBytes = jsonObject.getJSONArray( "aesKeyEncoded" );
        byte[] aesKey = new byte[aesKeyBytes.length()];
        for ( int i = 0; i < aesKeyBytes.length(); i++ ) {
            aesKey[i] = (byte) aesKeyBytes.getInt( i );
        }
        JSONArray initVectorBytes = jsonObject.getJSONArray( "initVector" );
        byte[] initVector = new byte[initVectorBytes.length()];
        for ( int i = 0; i < initVectorBytes.length(); i++ ) {
            initVector[i] = (byte) initVectorBytes.getInt( i );
        }
        return new AESKey( aesKey, initVector );
    }

    public void save( File file ) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter( file ) );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put( "aesKeyEncoded", aesKeyEncoded );
        jsonObject.put( "initVector", initVector );
        bufferedWriter.write( jsonObject.toString() );
        bufferedWriter.flush();
    }
}
