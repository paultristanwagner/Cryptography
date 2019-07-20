package me.paultristanwagner.cryptography.rsa;

import me.paultristanwagner.cryptography.util.ArrayUtils;
import me.paultristanwagner.cryptography.util.OAEP;
import me.paultristanwagner.cryptography.util.ProgressBar;
import org.json.JSONObject;

import java.io.*;
import java.math.BigInteger;
import java.util.Base64;

/**
 * JavaDoc this file!
 * Created: 06.06.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class PublicKey {

    private int bits;
    private BigInteger e, n;

    public PublicKey( int bits, BigInteger e, BigInteger n ) {
        this.bits = bits;
        this.e = e;
        this.n = n;
    }

    public static PublicKey read( File file ) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new FileReader( file ) );
        JSONObject jsonObject = new JSONObject( bufferedReader.readLine() );
        int bits = jsonObject.getInt( "bits" );
        BigInteger e = jsonObject.getBigInteger( "e" );
        BigInteger n = jsonObject.getBigInteger( "n" );
        return new PublicKey( bits, e, n );
    }

    public String encrypt( String text ) {
        byte[] bytes = text.getBytes();
        byte[] resultBytes = encrypt( bytes );
        return Base64.getEncoder().encodeToString( resultBytes );
    }

    public byte[] encrypt( byte[] bytes ) {
        return encrypt( bytes, null );
    }

    public byte[] encrypt( byte[] bytes, ProgressBar progressBar ) {
        byte[] paddedBytes;
        try {
            paddedBytes = OAEP.pad( bytes, "SHA-256 MGF1", bytes.length + 32 + 32 + 1 );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            return null;
        }
        if ( progressBar != null ) {
            progressBar.start();
        }
        byte[] resultBytes = new byte[0];
        int byteAmount = bits / 8 + 1;
        for ( int i = 0; i < paddedBytes.length; i++ ) {
            byte someByte = paddedBytes[i];
            if ( progressBar != null ) {
                progressBar.setProgress( (float) i / ( paddedBytes.length - 1 ) );
            }
            BigInteger bigInteger = new BigInteger( new byte[]{ someByte } ).add( BigInteger.valueOf( 255 ) );
            BigInteger encrypted = encrypt( bigInteger );
            byte[] encryptedBytes = encrypted.toByteArray();
            while ( encryptedBytes.length < byteAmount ) {
                encryptedBytes = ArrayUtils.concatByteArrays( new byte[]{ 0x00 }, encryptedBytes );
            }
            resultBytes = ArrayUtils.concatByteArrays( resultBytes, encryptedBytes );
        }
        if ( progressBar != null ) {
            progressBar.sync();
        }
        return resultBytes;
    }

    public BigInteger encrypt( BigInteger clear ) {
        return clear.modPow( e, n );
    }

    public void save( File file ) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter( file ) );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put( "bits", bits );
        jsonObject.put( "e", e );
        jsonObject.put( "n", n );
        bufferedWriter.write( jsonObject.toString() );
        bufferedWriter.flush();
    }

    public int getBits() {
        return bits;
    }

    @Override
    public String toString() {
        return "(" + e + ", " + n + ")";
    }
}
