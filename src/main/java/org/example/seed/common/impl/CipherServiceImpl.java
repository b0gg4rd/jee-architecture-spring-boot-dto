package org.example.seed.common.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.example.seed.common.CipherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Ricardo Pina Arellano on 26/11/2016.
 */
@Component
public class CipherServiceImpl implements CipherService {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Value("${service.cipher.key}")
    private String key;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Value("${service.cipher.salt}")
    private String salt;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private TextEncryptor encryptors;

    @PostConstruct
    public void createEncryptors() {
        this.encryptors = Encryptors.text(this.key, this.salt);
    }

    @Override
    public String encrypt(final String target) {
        return this.encryptors.encrypt(target);
    }

    @Override
    public String decrypt(final String target) {
        String textDecrypted;

        try {
            textDecrypted = this.encryptors.decrypt(target);
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return textDecrypted;
    }
}
