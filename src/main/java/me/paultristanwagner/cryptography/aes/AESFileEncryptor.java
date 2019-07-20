package me.paultristanwagner.cryptography.aes;

import me.paultristanwagner.cryptography.FileEncryptor;
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
public class AESFileEncryptor implements FileEncryptor {

    private AESKey aesKey;

    public AESFileEncryptor( AESKey aesKey ) {
        this.aesKey = aesKey;
    }

    @Override
    public void encrypt( File file, boolean progressbar ) throws IOException {
        ProgressBar progressBar = progressbar ? new ProgressBar() : null;
        byte[] bytes = Files.readAllBytes( file.toPath() );
        byte[] encrypted = aesKey.encrypt( bytes, progressBar );
        Files.write( file.toPath(), encrypted );
    }
}
