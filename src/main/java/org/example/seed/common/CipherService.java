package org.example.seed.common;

/**
 * Created by Ricardo Pina Arellano on 26/11/2016.
 */
public interface CipherService {
    public String encrypt(final String target);

    public String decrypt(final String target);
}
