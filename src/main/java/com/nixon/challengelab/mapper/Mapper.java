package com.nixon.challengelab.mapper;

import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public abstract class Mapper<E,D> {
    public abstract D toDto(E e);

    public List<D> toDtoList(Collection<E> e){
        return e.stream().map(this::toDto).toList();
    }

    public Page<D> toDtoPage(Page<E> page){
        return page.map(this::toDto);
    }
}
