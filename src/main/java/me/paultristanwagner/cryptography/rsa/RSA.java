package me.paultristanwagner.cryptography.rsa;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * JavaDoc this file!
 * Created: 05.06.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class RSA {

    public static void main( String[] args ) throws IOException {
        if ( args.length > 0 ) {
            if ( args[0].equals( "generate" ) ) {
                if ( args.length == 4 ) {
                    int bits = Integer.parseInt( args[1] );
                    String publicKeyPath = args[2];
                    String privateKeyPath = args[3];
                    generate( bits, publicKeyPath, privateKeyPath );
                    return;
                }
            } else if ( args[0].equals( "encrypt" ) ) {
                if ( args.length == 3 ) {
                    String publicKeyPath = args[1];
                    String filePath = args[2];
                    encrypt( publicKeyPath, filePath, true );
                    return;
                }
            } else if ( args[0].equals( "decrypt" ) ) {
                if ( args.length == 3 ) {
                    String privateKeyPath = args[1];
                    String filePath = args[2];
                    decrypt( privateKeyPath, filePath, true );
                    return;
                }
            }
        }
        System.out.println( "Syntax:" );
        System.out.println( "rsa generate <bit> <publicKeyFile> <privateKeyFile>" );
        System.out.println( "rsa encrypt <publicKeyFile> <file>" );
        System.out.println( "rsa decrypt <privateKeyFile> <file>" );
    }

    private static void generate( int bits, String publicKeyPath, String privateKeyPath ) throws IOException {
        System.out.println( "Generating " + bits + " bit rsa key pair..." );
        RSAKeyGenerator keyGenerator = new RSAKeyGenerator();
        keyGenerator.generate( new Random(), bits );
        PublicKey publicKey = keyGenerator.getPublicKey();
        PrivateKey privateKey = keyGenerator.getPrivateKey();
        File publicKeyFile = createFile( publicKeyPath );
        File privateKeyFile = createFile( privateKeyPath );
        publicKey.save( publicKeyFile );
        privateKey.save( privateKeyFile );
        System.out.println( "Key pair generated." );
    }

    private static void encrypt( String publicKeyPath, String filePath, boolean progressbar ) throws IOException {
        System.out.println( "Encrypting file..." );
        File publicKeyFile = new File( publicKeyPath );
        PublicKey publicKey = PublicKey.read( publicKeyFile );
        File file = new File( filePath );
        new RSAFileEncryptor( publicKey ).encrypt( file, progressbar );
        System.out.println( "File encrypted." );
    }

    private static void decrypt( String privateKeyPath, String filePath, boolean progressbar ) throws IOException {
        System.out.println( "Decrypting file..." );
        File privateKeyFile = new File( privateKeyPath );
        PrivateKey privateKey = PrivateKey.read( privateKeyFile );
        File file = new File( filePath );
        new RSAFileDecryptor( privateKey ).decrypt( file, progressbar );
        System.out.println( "File decrypted." );
    }

    private static File createFile( String path ) throws IOException {
        File file = new File( path );
        if ( !file.exists() ) {
            file.createNewFile();
        }
        return file;
    }
}