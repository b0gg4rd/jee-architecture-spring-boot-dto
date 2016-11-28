package org.example.seed.dto;

import java.io.Serializable;

/**
 * Created by Ricardo Pina Arellano on 26/11/2016.
 */
public interface IDto extends Serializable {
    String getKey();

    void setKey(final String key);
}
