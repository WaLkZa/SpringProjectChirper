package org.chirper.service;

import org.chirper.domain.entities.User;
import org.chirper.domain.entities.UserRole;
import org.chirper.repository.RoleRepository;
import org.chirper.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            ModelMapper modelMapper,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private Set<UserRole> getAuthorities(String authority) {
        Set<UserRole> userAuthorities = new HashSet<>();

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
        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

        if (this.userRepository.findAll().isEmpty()) {
            userEntity.setAuthorities(this.getAuthorities("ADMIN"));
        } else {
            userEntity.setAuthorities(this.getAuthorities("USER"));
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
    public boolean promoteUser(String id) {
        User user = this.userRepository
                .findById(id)
                .orElse(null);

        if (user == null) {
            return false;
        }

        String userAuthority = this.getUserAuthority(user.getId());

        switch (userAuthority) {
            case "USER":
                user.setAuthorities(this.getAuthorities("MODERATOR"));
                break;
            case "MODERATOR":
                user.setAuthorities(this.getAuthorities("ADMIN"));
                break;
            default:
                throw new IllegalArgumentException("There is no role, higher than ADMIN");
        }

        this.userRepository.save(user);
        return true;
    }

    @Override
    public boolean demoteUser(String id) {
        User user = this.userRepository
                .findById(id)
                .orElse(null);

        if (user == null) {
            return false;
        }

        String userAuthority = this.getUserAuthority(user.getId());

        switch (userAuthority) {
            case "ADMIN":
                user.setAuthorities(this.getAuthorities("MODERATOR"));
                break;
            case "MODERATOR":
                user.setAuthorities(this.getAuthorities("USER"));
                break;
            default:
                throw new IllegalArgumentException("There is no role, lower than USER");
        }

        this.userRepository.save(user);
        return true;
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
    public List<User> getUserAllFollowers(String userId) {
        User currentLoggedUser = this.getCurrentLoggedUser();

        List<User> allFollowers = currentLoggedUser.getFollowers();

        return allFollowers;
    }

    @Override
    public List<User> getUserAllFollowing(String userId) {
        User currentLoggedUser = this.getCurrentLoggedUser();

        List<User> allFollowing =  currentLoggedUser.getFollowing();

        return allFollowing;
    }
}