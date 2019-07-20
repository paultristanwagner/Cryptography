package me.paultristanwagner.cryptography.util;

/**
 * JavaDoc this file!
 * Created: 16.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class TimeFormatter {

    public static String format( long remainingTime ) {
        long hours = remainingTime / ( 60 * 60 * 1000 );
        remainingTime %= 60 * 60 * 1000;
        long minutes = remainingTime / ( 60 * 1000 );
        remainingTime %= 60 * 1000;
        long seconds = remainingTime / 1000;
        return String.format( "%d:%02d:%02d", hours, minutes, seconds );
    }
}
