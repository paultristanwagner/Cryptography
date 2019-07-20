package me.paultristanwagner.cryptography.rsa;

import me.paultristanwagner.cryptography.util.Tuple;

import java.math.BigInteger;
import java.util.Random;

/**
 * JavaDoc this file!
 * Created: 10.06.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class RSAKeyGenerator {

    private Random random;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public void generate( Random random, int bits ) {
        this.random = random;
        int primeLength = bits / 2;
        BigInteger prime0 = generatePrime( primeLength, null );
        BigInteger prime1 = generatePrime( primeLength, prime0 );
        BigInteger n = prime0.multiply( prime1 );
        BigInteger phi = prime0.subtract( BigInteger.ONE ).multiply( prime1.subtract( BigInteger.ONE ) );
        Tuple<BigInteger, BigInteger> exponents = generateExponents( phi );
        publicKey = new PublicKey( bits, exponents.getA(), n );
        privateKey = new PrivateKey( bits, exponents.getB(), prime0, prime1, n );
    }

    private Tuple<BigInteger, BigInteger> generateExponents( BigInteger phi ) {
        BigInteger e;
        BigInteger d;
        BigInteger x = BigInteger.valueOf( 65537 );
        while ( true ) {
            if ( phi.gcd( x ).equals( BigInteger.ONE ) ) {
                e = x;
                d = extgcd( e, phi ).getX();
                if ( d.signum() == 1 ) {
                    return new Tuple<>( e, d );
                }
            }
            x = x.add( BigInteger.ONE );
        }
    }

    private BigInteger generatePrime( int bit, BigInteger ignore ) {
        while ( true ) {
            BigInteger bigInteger = BigInteger.probablePrime( bit, random );
            if ( bigInteger.equals( ignore ) ) {
                continue;
            }
            return bigInteger;
        }
    }

    private BigIntEuclidean extgcd( BigInteger a, BigInteger b ) {
        BigInteger x = BigInteger.ZERO;
        BigInteger lastx = BigInteger.ONE;
        BigInteger y = BigInteger.ONE;
        BigInteger lasty = BigInteger.ZERO;
        while ( !b.equals( BigInteger.ZERO ) ) {
            BigInteger[] quotientAndRemainder = a.divideAndRemainder( b );
            BigInteger quotient = quotientAndRemainder[0];

            BigInteger temp;
            a = b;
            b = quotientAndRemainder[1];

            temp = x;
            x = lastx.subtract( quotient.multiply( x ) );
            lastx = temp;

            temp = y;
            y = lasty.subtract( quotient.multiply( y ) );
            lasty = temp;
        }

        return new BigIntEuclidean( lastx, lasty, a );
    }

    public PublicKey getPublicKey() {
        if ( publicKey == null ) {
            throw new IllegalStateException( "Key pair has not been generated yet" );
        }
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        if ( privateKey == null ) {
            throw new IllegalStateException( "Key pair has not been generated yet" );
        }
        return privateKey;
    }
}
