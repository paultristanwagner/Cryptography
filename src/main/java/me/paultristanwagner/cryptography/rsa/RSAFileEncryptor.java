package me.paultristanwagner.cryptography.rsa;

import me.paultristanwagner.cryptography.FileEncryptor;
import me.paultristanwagner.cryptography.util.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * JavaDoc this file!
 * Created: 10.06.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class RSAFileEncryptor implements FileEncryptor {

    private PublicKey publicKey;

    public RSAFileEncryptor( PublicKey publicKey ) {
        this.publicKey = publicKey;
    }

    public void encrypt( File file, boolean progressbar ) throws IOException {
        ProgressBar progressBar = progressbar ? new ProgressBar() : null;
        byte[] bytes = Files.readAllBytes( file.toPath() );
        byte[] encrypted = publicKey.encrypt( bytes, progressBar );
        Files.write( file.toPath(), encrypted );
    }
}
