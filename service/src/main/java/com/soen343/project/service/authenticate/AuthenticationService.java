package com.soen343.project.service.authenticate;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.entity.user.types.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    @Autowired
    AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticateAdmin(Long id) {
        User user = userRepository.findById(id);
        return user != null && user.getUserType().equals(UserType.ADMIN);
    }

}
