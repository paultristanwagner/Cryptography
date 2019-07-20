package me.paultristanwagner.cryptography.aes;

import me.paultristanwagner.cryptography.FileDecryptor;
import me.paultristanwagner.cryptography.util.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * JavaDoc this file!
 * Created: 16.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class AESFileDecryptor implements FileDecryptor {

    private AESKey aesKey;

    public AESFileDecryptor( AESKey aesKey ) {
        this.aesKey = aesKey;
    }

    @Override
    public void decrypt( File file, boolean progressbar ) throws IOException {
        ProgressBar progressBar = progressbar ? new ProgressBar() : null;
        byte[] bytes = Files.readAllBytes( file.toPath() );
        byte[] decrypted = aesKey.decrypt( bytes, progressBar );
        Files.write( file.toPath(), decrypted );
    }
}
