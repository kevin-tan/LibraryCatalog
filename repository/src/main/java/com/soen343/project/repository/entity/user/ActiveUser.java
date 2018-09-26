package com.soen343.project.repository.entity.user;

import lombok.Data;

@Data
public class ActiveUser {

    private Long id;
    private Long timestamp;

    public ActiveUser() {
        this.timestamp = System.currentTimeMillis();
    }

    public ActiveUser(Long id){
        this.id = id;
        this.timestamp = System.currentTimeMillis();
    }
}
