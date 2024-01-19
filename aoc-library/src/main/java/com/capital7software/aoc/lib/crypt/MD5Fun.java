package com.capital7software.aoc.lib.crypt;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically
 * forward-thinking little girls and boys.
 *
 */
public record MD5Fun() {
    /**
     * For example:
     * <p>
     * If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five
     * zeroes (000001dbbfa...), and it is the lowest such number to do so.
     * If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five
     * zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
     *
     * @param secret The secret text to find a number to join with to produce a hash with the required number of
     *               leading zeros.
     * @param leadingZeros The number of leading zeros the MD5 hash must have.
     * @return The lowest positive number to join with the secret to produce a MD5 hash with the required number
     *               of leading zeros.
     */
    public static long lowestPositiveNumberWithLeadingZeros(@NotNull String secret, int leadingZeros) {
        var startsWith = "0".repeat(leadingZeros);
        long count = 0;
        try {
            var md = MessageDigest.getInstance("MD5");
            var done = false;
            while (!done) {
                var target = secret + count;
                md.update(target.getBytes(StandardCharsets.US_ASCII));

                String hash = hashToString(md);

                if (hash.startsWith(startsWith)) {
                    done = true;
                } else {
                    count++;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return count;
    }

    /**
     * Digests the specified message and produces a hexadecimal string of the resulting hash.
     * <p>
     * @param md THe MessageDigest to digest and convert to a hexadecimal string.
     * @return The MD5 hash as a hexadecimal string.
     */
    @NotNull
    private static String hashToString(@NotNull MessageDigest md) {
        var buffer = md.digest();
        var sb = new StringBuilder();

        for (byte b : buffer) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
