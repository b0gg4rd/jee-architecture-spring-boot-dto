package org.example.seed.mapper.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.dozer.DozerBeanMapper;
import org.example.seed.common.CipherService;
import org.example.seed.dto.PageDto;
import org.example.seed.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by Ricardo Pina Arellano on 26/11/2016.
 */
public abstract class MapperImpl<F, T extends Serializable> implements Mapper<F, T> {

    private final Class<F> fromClass;
    private final Class<T> toClass;
    private final String[] children;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private CipherService cipherService;

    public MapperImpl(final Class<F> fromClass, final Class<T> toClass) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.children = null;
    }

    public MapperImpl(final Class<F> fromClass, final Class<T> toClass, final String[] children) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.children = children;
    }

    @Override
    public T map(final F from) {
        try {
            final String key = BeanUtils.getProperty(from, "key");

            if (key != null && !key.isEmpty()) {
                BeanUtils.setProperty(from, "key", this.cipherService.decrypt(key));
            }

            if (this.children != null) {
                for (final String element : this.children) {
                    final String keyChild = (PropertyUtils.getProperty(from, element) != null) ? BeanUtils.getProperty(from, element + ".key") : null;

                    if (keyChild != null && !keyChild.isEmpty()) {
                        BeanUtils.setProperty(from, element + ".key", this.cipherService.decrypt(keyChild));
                    }
                }
            }
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }

        return this.dozerBeanMapper.map(from, this.toClass);
    }

    @Override
    public F map(final T from) {
        final F to = this.dozerBeanMapper.map(from, this.fromClass);

        try {
            final String id = BeanUtils.getProperty(from, "id");

            if (id != null && !id.isEmpty()) {
                BeanUtils.setProperty(to, "key", this.cipherService.encrypt(id));
            }

            if (this.children != null) {
                for (final String element : this.children) {
                    final String idChild = (PropertyUtils.getProperty(from, element) != null) ? BeanUtils.getProperty(from, element + ".id") : null;

                    if (idChild != null && !idChild.isEmpty()) {
                        BeanUtils.setProperty(to, element + ".key", this.cipherService.encrypt(idChild));
                    }
                }
            }
        } catch (final Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return to;
    }

    @Override
    public Future<F> map(final Future<T> from) {
        if (from == null) {
            return null;
        }

        final F to;

        try {
            to = this.dozerBeanMapper.map(from.get(), this.fromClass);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        }

        return new AsyncResult<>(to);
    }

    @Override
    public List<T> mapList(final List<F> from) {
        ArrayList<T> resultList = null;

        if (from != null) {
            resultList = new ArrayList<>();

            for (final F objectSource : from) {
                resultList.add(this.map(objectSource));
            }
        }

        return resultList;
    }

    @Override
    public Future<List<T>> mapList(final Future<List<F>> from) {
        ArrayList<T> resultList = null;

        if (from != null) {
            resultList = new ArrayList<>();

            try {
                for (final F objectSource : from.get()) {
                    resultList.add(this.map(objectSource));
                }
            } catch (final InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return new AsyncResult<>(resultList);
    }

    @Override
    public List<F> mapListReverse(final List<T> from) {
        ArrayList<F> resultList = null;

        if (from != null) {
            resultList = new ArrayList<>();

            for (final T objectSource : from) {
                resultList.add(this.map(objectSource));
            }
        }

        return resultList;
    }

    @Override
    public Future<List<F>> mapListReverse(final Future<List<T>> from) {
        ArrayList<F> resultList = null;

        if (from != null) {
            resultList = new ArrayList<>();

            try {
                for (final T objectSource : from.get()) {
                    resultList.add(this.map(objectSource));
                }
            } catch (final InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return new AsyncResult<>(resultList);
    }

    @Override
    public Future<PageDto<F>> mapPageReverse(final Future<Page<T>> from) {
        PageDto<F> resultList = null;

        if (from != null) {
            final List<F> contentList = new ArrayList<>();
            resultList = new PageDto<F>();

            try {
                final Page<T> pageFrom = from.get();

                contentList.addAll(pageFrom.getContent().stream().map(this::map).collect(Collectors.toList()));

                resultList.setContent(contentList);
                resultList.setTotalElements(pageFrom.getTotalElements());
                resultList.setTotalPages(pageFrom.getTotalPages());
            } catch (final InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return new AsyncResult<>(resultList);
    }
}
