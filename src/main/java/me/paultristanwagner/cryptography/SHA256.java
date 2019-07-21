package me.paultristanwagner.cryptography;

import java.nio.ByteBuffer;

/**
 * Created: 20.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class SHA256 {

    private SHA256() { }

    private static final int[] INIT = new int[]{ 0x6a09e667, 0xbb67ae85, 0x3c6ef372,
            0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19 };

    private static final int[] K = { 0x428a2f98, 0x71374491, 0xb5c0fbcf,
            0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
            0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74,
            0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1, 0xefbe4786,
            0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc,
            0x76f988da, 0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
            0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 0x27b70a85,
            0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb,
            0x81c2c92e, 0x92722c85, 0xa2bfe8a1, 0xa81a664b, 0xc24b8b70,
            0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3,
            0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f,
            0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7,
            0xc67178f2
    };

    private static int[] h = new int[8];

    public static byte[] hash( byte[] bytes ) {
        byte[] padded = pad( bytes );
        int n = padded.length / 64;
        int[] paddedInts = toIntArray( padded );
        for ( int i = 0; i < n; i++ ) {
            int[] words = new int[64];
            System.arraycopy( paddedInts, i * 16, words, 0, 16 );
            for ( int k = 16; k < 64; k++ ) {
                words[k] = sigma1( words[k - 2] ) + words[k - 7] + sigma0( words[k - 15] ) + words[k - 16];
            }

            int[] temp;
            if ( i == 0 ) {
                temp = INIT.clone();
                h = INIT.clone();
            } else {
                temp = h.clone();
            }
            for ( int j = 0; j < 64; j++ ) {
                int t1 = temp[7] + sum1( temp[4] ) + ch( temp[4], temp[5], temp[6] ) + K[j] + words[j];
                int t2 = sum0( temp[0] ) + maj( temp[0], temp[1], temp[2] );
                temp[7] = temp[6];
                temp[6] = temp[5];
                temp[5] = temp[4];
                temp[4] = temp[3] + t1;
                temp[3] = temp[2];
                temp[2] = temp[1];
                temp[1] = temp[0];
                temp[0] = t1 + t2;
            }
            for ( int j = 0; j < 8; j++ ) {
                h[j] += temp[j];
            }
        }
        return toByteArray( h );
    }

    private static byte[] toByteArray( int[] ints ) {
        ByteBuffer buf = ByteBuffer.allocate( ints.length * Integer.BYTES );
        for ( int anInt : ints ) {
            buf.putInt( anInt );
        }
        return buf.array();
    }

    private static int[] toIntArray( byte[] bytes ) {
        if ( bytes.length % Integer.BYTES != 0 ) {
            throw new IllegalArgumentException( "byte array length" );
        }

        ByteBuffer buf = ByteBuffer.wrap( bytes );
        int[] result = new int[bytes.length / Integer.BYTES];
        for ( int i = 0; i < result.length; ++i ) {
            result[i] = buf.getInt();
        }
        return result;
    }

    private static int ch( int x, int y, int z ) {
        return ( x & y ) | ( ( ~x ) & z );
    }

    private static int maj( int x, int y, int z ) {
        return ( x & y ) | ( x & z ) | ( y & z );
    }

    private static int sum0( int x ) {
        return Integer.rotateRight( x, 2 ) ^ Integer.rotateRight( x, 13 )
                ^ Integer.rotateRight( x, 22 );
    }

    private static int sum1( int x ) {
        return Integer.rotateRight( x, 6 ) ^ Integer.rotateRight( x, 11 )
                ^ Integer.rotateRight( x, 25 );
    }

    private static int sigma0( int x ) {
        return Integer.rotateRight( x, 7 ) ^ Integer.rotateRight( x, 18 )
                ^ ( x >>> 3 );
    }

    private static int sigma1( int x ) {
        return Integer.rotateRight( x, 17 ) ^ Integer.rotateRight( x, 19 )
                ^ ( x >>> 10 );
    }

    private static byte[] pad( byte[] bytes ) {
        int messageBits = bytes.length * 8;
        int spacingBits = Math.floorMod( 447 - messageBits, 512 );
        int zeroBytes = spacingBits / 8;
        byte[] padded = new byte[bytes.length + zeroBytes + 9];
        System.arraycopy( bytes, 0, padded, 0, bytes.length );
        padded[bytes.length] = (byte) 10000000;
        for ( int i = 1; i <= zeroBytes; i++ ) {
            padded[bytes.length + i] = 0x0;
        }
        byte suffix0 = (byte) ( messageBits >> 8 & 0xff );
        byte suffix1 = (byte) ( messageBits & 0xff );
        padded[padded.length - 2] = suffix0;
        padded[padded.length - 1] = suffix1;
        return padded;
    }
}
