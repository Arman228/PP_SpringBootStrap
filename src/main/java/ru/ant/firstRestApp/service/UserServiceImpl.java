package ru.ant.firstRestApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ant.firstRestApp.model.Role;
import ru.ant.firstRestApp.model.User;
import ru.ant.firstRestApp.repository.RoleRepository;
import ru.ant.firstRestApp.repository.UserRepository;
import ru.ant.firstRestApp.service.UserService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        userRepository.delete(userRepository.findById(id).get());
    }

    @Override
    @Transactional
    public void updateUser(Integer id, User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        user.setFirstName(user.getFirstName());
        user.setLastName(user.getLastName());
        user.setAge(user.getAge());
        user.setRole(user.getRoles());
        userRepository.save(user);
    }

    @Override
    public User showUser(Integer id) {
        return userRepository.findById(id).get();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);


        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    @Override
    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

}