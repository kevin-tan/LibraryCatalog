package com.soen343.project.repository.dao.user;

/**
 * Created by Kevin Tan 2018-09-23
 */

public interface Repository<E> {

    void save(E entity);

    void saveAll(E... entities);

    void delete(E entity);

    void deleteAll();

    E find(Long id);

    void update(E entity);
}
