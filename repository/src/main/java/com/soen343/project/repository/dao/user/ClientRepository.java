package com.soen343.project.repository.dao.user;

import com.soen343.project.repository.entity.user.Client;

import java.util.List;

/**
 * Created by Kevin Tan 2018-09-23
 */

public class ClientRepository implements Repository<Client> {

    @Override
    public synchronized void save(Client entity) {

    }

    @Override
    public synchronized void saveAll(Client... entities) {

    }

    @Override
    public synchronized void delete(Client entity) {

    }

    @Override
    public synchronized void deleteAll() {

    }

    @Override
    public synchronized void update(Client entity) {

    }

    @Override
    public Client findById(Long id) {
        return null;
    }

    @Override
    public List<Client> findAll() {
        return null;
    }

}
