package me.paultristanwagner.cryptography.util;

/**
 * JavaDoc this file!
 * Created: 10.06.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class ArrayUtils {

    public static byte[] concatByteArrays( byte[] a, byte[] b ) {
        if(b == null)
            return a;
        byte[] result = new byte[a.length + b.length];
        System.arraycopy( a, 0, result, 0, a.length );
        System.arraycopy( b, 0, result, a.length, b.length );
        return result;
    }
}
