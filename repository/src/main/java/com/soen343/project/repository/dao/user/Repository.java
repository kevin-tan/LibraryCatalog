package com.soen343.project.repository.dao.user;

import org.springframework.stereotype.Component;

/**
 * Created by Kevin Tan 2018-09-23
 */

@Component
public interface Repository<E> {

    void save(E entity);

    void saveAll(E... entities);

    void delete(E entity);

    void deleteAll();

    E find(Long id);

    void update(E entity);
}
