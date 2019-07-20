package me.paultristanwagner.cryptography;

import me.paultristanwagner.cryptography.aes.AES;
import me.paultristanwagner.cryptography.rsa.RSA;

import java.io.IOException;

/**
 * JavaDoc this file!
 * Created: 16.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class Starter {

    public static void main( String[] args ) throws IOException {
        if ( args.length != 0 ) {
            if ( args[0].equals( "aes" ) ) {
                String[] reducedArgs = new String[args.length - 1];
                System.arraycopy( args, 1, reducedArgs, 0, args.length - 1 );
                AES.main( reducedArgs );
                return;
            } else if ( args[0].equals( "rsa" ) ) {
                String[] reducedArgs = new String[args.length - 1];
                System.arraycopy( args, 1, reducedArgs, 0, args.length - 1 );
                RSA.main( reducedArgs );
                return;
            }
        }
        System.out.println( "Syntax: <aes/rsa> ..." );
    }
}
