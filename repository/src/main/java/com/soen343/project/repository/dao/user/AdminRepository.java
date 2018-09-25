package com.soen343.project.repository.dao.user;

import com.soen343.project.repository.entity.user.Admin;

import java.util.List;

/**
 * Created by Kevin Tan 2018-09-23
 */

public class AdminRepository implements Repository<Admin>{

    @Override
    public synchronized void save(Admin entity) {
        //Code
    }

    @Override
    public synchronized void saveAll(Admin... entities) {

    }

    @Override
    public synchronized void delete(Admin entity) {

    }

    @Override
    public synchronized void deleteAll() {

    }

    @Override
    public synchronized void update(Admin entity) {

    }

    @Override
    public Admin findById(Long id) {
        return null;
    }

    @Override
    public List<Admin> findAll() {
        return null;
    }
}
