package org.example.seed.mapper;

import org.example.seed.dto.PageDto;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Ricardo Pina Arellano on 26/11/2016.
 */
public interface Mapper<F, T extends Serializable> {

    T map(final F from);

    F map(final T from);

    Future<F> map(final Future<T> from);

    List<T> mapList(final List<F> from);

    List<F> mapListReverse(final List<T> from);

    Future<List<T>> mapList(final Future<List<F>> from);

    Future<List<F>> mapListReverse(final Future<List<T>> from);

    Future<PageDto<F>> mapPageReverse(final Future<Page<T>> from);
}
