package me.paultristanwagner.cryptography.rsa;

import java.math.BigInteger;

/**
 * JavaDoc this file!
 * Created: 06.06.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class BigIntEuclidean {

    private BigInteger x, y, gcd;

    public BigIntEuclidean( BigInteger x, BigInteger y, BigInteger gcd ) {
        this.x = x;
        this.y = y;
        this.gcd = gcd;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public BigInteger getGcd() {
        return gcd;
    }
}
