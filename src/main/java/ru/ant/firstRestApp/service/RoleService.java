package ru.ant.firstRestApp.service;

import org.springframework.stereotype.Service;
import ru.ant.firstRestApp.model.Role;


import java.util.List;

@Service
public interface RoleService {
    Role findRoleById(Integer id);

    List<Role> getAllRoles();

    void addRole(Role role);
}