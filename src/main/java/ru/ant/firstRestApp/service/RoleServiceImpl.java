package ru.ant.firstRestApp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ant.firstRestApp.model.Role;
import ru.ant.firstRestApp.repository.RoleRepository;
import ru.ant.firstRestApp.service.RoleService;


import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleById(Integer id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    @Override
    public void addRole(Role role) {
        roleRepository.save(role);
    }
}