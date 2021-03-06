package com.soen343.project.repository.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by Kevin Tan 2018-09-23
 */

public interface Gateway<E> {

    void save(E entity);

    void saveAll(E... entities);

    void delete(E entity);

    E findById(Long id);

    List<E> findByAttribute(Map<String, String> attributeValue);

    List<E> findAll();

    void update(E entity);
}
