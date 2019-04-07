package org.chirper.service;

import org.chirper.domain.entities.Role;

import java.util.Set;

public interface RoleService {

    void seedRolesInDb();

    Set<Role> findAllRoles();

    Role findByAuthority(String authority);
}