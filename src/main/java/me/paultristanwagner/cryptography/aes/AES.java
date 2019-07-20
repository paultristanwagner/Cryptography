package me.paultristanwagner.cryptography.aes;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * JavaDoc this file!
 * Created: 16.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class AES {

    public static void main( String[] args ) throws IOException {
        if ( args.length != 0 ) {
            if ( args[0].equals( "generate" ) ) {
                if ( args.length == 3 ) {
                    int bits = Integer.parseInt( args[1] );
                    String publicKeyPath = args[2];
                    generate( bits, publicKeyPath );
                    return;
                }
            } else if ( args[0].equals( "encrypt" ) ) {
                if ( args.length == 3 ) {
                    String aesKeyPath = args[1];
                    String filePath = args[2];
                    encrypt( aesKeyPath, filePath, true );
                    return;
                }
            } else if ( args[0].equals( "decrypt" ) ) {
                if ( args.length == 3 ) {
                    String aesKeyPath = args[1];
                    String filePath = args[2];
                    decrypt( aesKeyPath, filePath, true );
                    return;
                }
            }
        }
        System.out.println( "Syntax:" );
        System.out.println( "aes generate <bit> <file>" );
        System.out.println( "aes encrypt <keyFile> <file>" );
        System.out.println( "aes decrypt <keyFile> <file>" );
    }

    private static void generate( int bits, String keyPath ) throws IOException {
        System.out.println( "Generating " + bits + " bit aes key..." );
        AESKeyGenerator keyGenerator = new AESKeyGenerator();
        keyGenerator.generate( new SecureRandom(), bits );
        AESKey aesKey = keyGenerator.getAESKey();
        File aesKeyFile = createFile( keyPath );
        aesKey.save( aesKeyFile );
        System.out.println( "Key generated." );
    }

    private static void encrypt( String keyPath, String filePath, boolean progressbar ) throws IOException {
        System.out.println( "Encrypting file..." );
        File aesKeyFile = new File( keyPath );
        AESKey aesKey = AESKey.read( aesKeyFile );
        File file = new File( filePath );
        new AESFileEncryptor( aesKey ).encrypt( file, progressbar );
        System.out.println( "File encrypted." );
    }

    private static void decrypt( String keyPath, String filePath, boolean progressbar ) throws IOException {
        System.out.println( "Decrypting file..." );
        File aesKeyFile = new File( keyPath );
        AESKey aesKey = AESKey.read( aesKeyFile );
        File file = new File( filePath );
        new AESFileDecryptor( aesKey ).decrypt( file, progressbar );
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
