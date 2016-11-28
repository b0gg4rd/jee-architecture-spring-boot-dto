package org.example.seed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ricardo Pina Arellano on 26/11/2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> implements Serializable {

    private static final long serialVersionUID = 4492649902088364778L;

    private int totalPages;
    private long totalElements;
    private List<T> content;
}
