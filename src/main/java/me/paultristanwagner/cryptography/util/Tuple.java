package me.paultristanwagner.cryptography.util;

/**
 * JavaDoc this file!
 * Created: 06.06.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class Tuple<A, B> {

    private A a;
    private B b;

    public Tuple( A a, B b ) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
