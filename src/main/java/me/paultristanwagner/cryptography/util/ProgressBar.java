package me.paultristanwagner.cryptography.util;

/**
 * JavaDoc this file!
 * Created: 16.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class ProgressBar {

    private static final int BAR_LENGTH = 50;
    private float progress = 0;
    private long start;
    private int messageLength = 0;
    private Thread thread;

    public void start() {
        start = System.currentTimeMillis();
        thread = new Thread( () -> {
            while ( progress < 1 ) {
                updateProgress( progress, true );
                try {
                    Thread.sleep( 50 );
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
            }
            finish();
        } );
        thread.start();
    }

    public void sync() {
        if ( thread != null ) {
            try {
                thread.join();
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }

    public void setProgress( float progress ) {
        this.progress = progress;
    }

    private void updateProgress( float progress, boolean displayTime ) {
        clearLatestMessage();
        int bars = Math.round( progress * BAR_LENGTH );
        long lasted = System.currentTimeMillis() - start;
        long estimatedEnd = start + Math.round( (float) lasted / progress );
        long estimatedTotal = estimatedEnd - start;
        String lastedFormatted = TimeFormatter.format( lasted );
        String estimatedTotalFormatted = TimeFormatter.format( estimatedTotal );
        StringBuilder builder = new StringBuilder( "[" );
        for ( int j = 0; j < bars; j++ ) {
            if ( progress != 1 && j == bars - 1 ) {
                builder.append( ">" );
            } else {
                builder.append( "=" );
            }
        }
        for ( int k = bars; k < BAR_LENGTH; k++ ) {
            builder.append( " " );
        }
        int percent = Math.round( progress * 100 );
        builder.append( "] - " ).append( percent ).append( "%" );
        if ( displayTime ) {
            builder.append( " (" ).append( lastedFormatted ).append( " / " )
                    .append( estimatedTotalFormatted ).append( ")" );
        }
        builder.append( "\r" );
        messageLength = builder.length();
        System.out.print( builder.toString() );
    }

    private void clearLatestMessage() {
        for ( int i = 0; i < messageLength; i++ ) {
            System.out.print( " " );
        }
        System.out.print( "\r" );
    }

    private void finish() {
        clearLatestMessage();
        updateProgress( 1, false );
        System.out.print( "\n" );
    }
}
