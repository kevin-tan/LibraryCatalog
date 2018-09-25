package com.soen343.project.repository.dao.user;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Kevin Tan 2018-09-23
 */

@Component
public interface Repository<E> {

    void save(E entity);

    void saveAll(E... entities);

    void delete(E entity);

    void deleteAll();

    void update(E entity);

    E findById(Long id);

    List<E> findAll();

}
