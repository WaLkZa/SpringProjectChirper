package org.chirper.service;

import org.chirper.domain.entities.User;
import org.chirper.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {
    boolean createUser(UserServiceModel userServiceModel);

    User getCurrentLoggedUser();

    Set<UserServiceModel> getAll();

    boolean promoteUser(String id);

    boolean demoteUser(String id);
}