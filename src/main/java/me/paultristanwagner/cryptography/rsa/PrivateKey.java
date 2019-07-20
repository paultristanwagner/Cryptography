package me.paultristanwagner.cryptography.rsa;

import me.paultristanwagner.cryptography.util.ArrayUtils;
import me.paultristanwagner.cryptography.util.OAEP;
import me.paultristanwagner.cryptography.util.ProgressBar;
import org.json.JSONObject;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * JavaDoc this file!
 * Created: 06.06.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class PrivateKey {

    private int bits;
    private BigInteger d, p, q, n, dp, dq, qinv;

    public PrivateKey( int bits, BigInteger d, BigInteger p, BigInteger q, BigInteger n ) {
        this.bits = bits;
        this.d = d;
        this.n = n;
        this.p = p;
        this.q = q;
        this.dp = d.mod( p.subtract( BigInteger.ONE ) );
        this.dq = d.mod( q.subtract( BigInteger.ONE ) );
        this.qinv = q.modPow( BigInteger.valueOf( -1 ), p );
    }

    private PrivateKey( int bits, BigInteger d, BigInteger p, BigInteger q, BigInteger n, BigInteger dp, BigInteger dq, BigInteger qinv ) {
        this.bits = bits;
        this.d = d;
        this.p = p;
        this.q = q;
        this.n = n;
        this.dp = dp;
        this.dq = dq;
        this.qinv = qinv;
    }

    public static PrivateKey read( File file ) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new FileReader( file ) );
        JSONObject jsonObject = new JSONObject( bufferedReader.readLine() );
        int bits = jsonObject.getInt( "bits" );
        BigInteger d = jsonObject.getBigInteger( "d" );
        BigInteger p = jsonObject.getBigInteger( "p" );
        BigInteger q = jsonObject.getBigInteger( "q" );
        BigInteger n = jsonObject.getBigInteger( "n" );
        BigInteger dp = jsonObject.getBigInteger( "dp" );
        BigInteger dq = jsonObject.getBigInteger( "dq" );
        BigInteger qinv = jsonObject.getBigInteger( "qinv" );
        return new PrivateKey( bits, d, p, q, n, dp, dq, qinv );
    }

    public String decrypt( String encryptedText ) {
        byte[] bytes = Base64.getDecoder().decode( encryptedText.getBytes() );
        byte[] resultBytes = decrypt( bytes );
        return new String( resultBytes, StandardCharsets.UTF_8 );
    }

    public byte[] decrypt( byte[] bytes ) {
        return decrypt( bytes, null );
    }

    public byte[] decrypt( byte[] bytes, ProgressBar progressBar ) {
        int byteAmount = bits / 8 + 1;
        if ( bytes.length % byteAmount != 0 ) {
            throw new IllegalArgumentException( "Message cannot be decrypted" );
        }
        if ( progressBar != null ) {
            progressBar.start();
        }
        byte[] resultBytes = new byte[0];
        for ( int i = 0; i < bytes.length; i += byteAmount ) {
            if ( progressBar != null ) {
                progressBar.setProgress( (float) i / ( bytes.length - byteAmount ) );
            }
            byte[] subBytes = new byte[byteAmount];
            System.arraycopy( bytes, i, subBytes, 0, byteAmount );
            BigInteger encrypted = new BigInteger( subBytes );
            BigInteger decrypted = decrypt( encrypted ).subtract( BigInteger.valueOf( 255 ) );
            resultBytes = ArrayUtils.concatByteArrays( resultBytes, decrypted.toByteArray() );
        }
        byte[] unpaddedBytes = null;
        try {
            unpaddedBytes = OAEP.unpad( resultBytes, "SHA-256 MGF1" );
            if ( unpaddedBytes == null ) {
                throw new NullPointerException();
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        if ( progressBar != null ) {
            progressBar.sync();
        }
        return unpaddedBytes;
    }

    public BigInteger decrypt( BigInteger encrypted ) {
        //return encrypted.modPow( d, n );
        BigInteger m1 = encrypted.modPow( dp, p );
        BigInteger m2 = encrypted.modPow( dq, q );
        BigInteger h = qinv.multiply( m1.subtract( m2 ) ).mod( p );
        BigInteger m = m2.add( h.multiply( q ) );
        return m;
    }

    public void save( File file ) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter( file ) );
        JSONObject jsonObject = new JSONObject();
        jsonObject.put( "bits", bits );
        jsonObject.put( "d", d );
        jsonObject.put( "p", p );
        jsonObject.put( "q", q );
        jsonObject.put( "n", n );
        jsonObject.put( "dp", dp );
        jsonObject.put( "dq", dq );
        jsonObject.put( "qinv", qinv );
        bufferedWriter.write( jsonObject.toString() );
        bufferedWriter.flush();
    }

    public int getBits() {
        return bits;
    }

    @Override
    public String toString() {
        return "(" + d + ", " + n + ")";
    }
}
