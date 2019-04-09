package org.chirper.service;

import org.chirper.domain.entities.User;
import org.chirper.domain.entities.Role;
import org.chirper.domain.models.view.UserAllFollowersViewModel;
import org.chirper.domain.models.view.UserAllFollowingViewModel;
import org.chirper.repository.RoleRepository;
import org.chirper.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            RoleService roleService, ModelMapper modelMapper,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private Set<Role> getAuthorities(String authority) {
        Set<Role> userAuthorities = new LinkedHashSet<>();

        userAuthorities.add(this.roleRepository.getByAuthority(authority));

        return userAuthorities;
    }

    private String getUserAuthority(String userId) {
        return this
                .userRepository
                .findById(userId)
                .get()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();
    }

    @Override
    public boolean createUser(User userEntity) {
        this.roleService.seedRolesInDb();

        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

        if (this.userRepository.count() == 0) {
            userEntity.setAuthorities(this.roleService.findAllRoles());
        } else {
            userEntity.setAuthorities(this.getAuthorities("ROLE_USER"));
        }

        try {
            this.userRepository.save(userEntity);
        } catch (Exception ignored) {
            //TODO: Fix this when discover exception type.
            return false;
        }

        return true;
    }

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public void setUserRole(String id, String role) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id!"));

        user.getAuthorities().clear();

        switch (role) {
            case "user":
                user.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                break;
            case "admin":
                user.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                user.getAuthorities().add(this.roleService.findByAuthority("ROLE_ADMIN"));
                break;
        }

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("No such user.");
        }

        return user;
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        User user = this.userRepository
                .findById(id)
                .orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("No such user.");
        }

        return user;
    }

    @Override
    public User getCurrentLoggedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return (User) loadUserByUsername(userDetails.getUsername());
    }

    @Override
    public void followUser(String toFollowId) {
        User toFollowUser = this.userRepository.findById(toFollowId).get();
        User currentLoggedUser = this.getCurrentLoggedUser();
        currentLoggedUser.addFollowing(toFollowUser);
        toFollowUser.addFollower(currentLoggedUser);

        this.userRepository.saveAndFlush(currentLoggedUser);
        this.userRepository.saveAndFlush(toFollowUser);
    }

    @Override
    public void unfollowUser(String followedId) {
        User followedUser = this.userRepository.findById(followedId).get();
        User currentLoggedUser = this.getCurrentLoggedUser();
        currentLoggedUser.removeFollowing(followedUser);
        followedUser.removeFollower(currentLoggedUser);

        this.userRepository.saveAndFlush(currentLoggedUser);
        this.userRepository.saveAndFlush(followedUser);
    }

    @Override
    public List<UserAllFollowersViewModel> getUserAllFollowers(String userId) {
        User currentUser = (User) this.loadUserById(userId);

        List<UserAllFollowersViewModel> allFollowers = currentUser.getFollowers()
                .stream()
                .map(user -> this.modelMapper.map(user, UserAllFollowersViewModel.class))
                .collect(Collectors.toList());

        return allFollowers;
    }

    @Override
    public List<UserAllFollowingViewModel> getUserAllFollowing(String userId) {
        User currentUser = (User) this.loadUserById(userId);

        List<UserAllFollowingViewModel> allFollowing = currentUser.getFollowing()
                .stream()
                .map(user -> this.modelMapper.map(user, UserAllFollowingViewModel.class))
                .collect(Collectors.toList());

        return allFollowing;
    }
}