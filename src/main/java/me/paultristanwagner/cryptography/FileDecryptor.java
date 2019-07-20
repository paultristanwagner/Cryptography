package me.paultristanwagner.cryptography;

import java.io.File;
import java.io.IOException;

/**
 * JavaDoc this file!
 * Created: 16.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public interface FileDecryptor {

    void decrypt( File file, boolean progressbar ) throws IOException;
}
