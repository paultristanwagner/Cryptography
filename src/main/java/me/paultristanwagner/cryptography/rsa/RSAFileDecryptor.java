package me.paultristanwagner.cryptography.rsa;

import me.paultristanwagner.cryptography.FileDecryptor;
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
public class RSAFileDecryptor implements FileDecryptor {

    private PrivateKey privateKey;

    public RSAFileDecryptor( PrivateKey privateKey ) {
        this.privateKey = privateKey;
    }

    @Override
    public void decrypt( File file, boolean progressbar ) throws IOException {
        ProgressBar progressBar = progressbar ? new ProgressBar() : null;
        byte[] bytes = Files.readAllBytes( file.toPath() );
        byte[] decrypted = privateKey.decrypt( bytes, progressBar );
        Files.write( file.toPath(), decrypted );
    }
}
