package org.example.seed.domain;

import java.io.Serializable;

/**
 * Created by Ricardo Pina Arellano on 26/11/2016.
 */
public interface IDomain extends Serializable {
    Long getId();

    void setId(final Long id);
}
