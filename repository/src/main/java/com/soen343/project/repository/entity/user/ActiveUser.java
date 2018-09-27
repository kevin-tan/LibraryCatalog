package com.soen343.project.repository.entity.user;

import lombok.Data;

import java.util.Date;

@Data
public class ActiveUser {

    private Long id;
    private Date timestamp;

    public ActiveUser() {
        this.timestamp = new Date(System.currentTimeMillis());
    }

    public ActiveUser(Long id){
        this.id = id;
        this.timestamp = new Date(System.currentTimeMillis());
    }
}
