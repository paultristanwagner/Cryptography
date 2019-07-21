package me.paultristanwagner.cryptography;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JavaDoc this file!
 * Created: 21.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
class SHA256Test {

    private MessageDigest messageDigest;
    private byte[] testBytes;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        int length = 64;
        testBytes = new byte[length];
        new Random().nextBytes( testBytes );
        messageDigest = MessageDigest.getInstance( "SHA-256" );
    }

    @Test
    void hash() {
        byte[] targetResult = messageDigest.digest( testBytes );
        byte[] actualResult = SHA256.hash( testBytes );
        assertArrayEquals( targetResult, actualResult );
    }
}