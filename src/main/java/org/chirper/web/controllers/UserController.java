package org.chirper.web.controllers;

import org.chirper.domain.entities.User;
import org.chirper.domain.models.binding.UserRegisterBindingModel;
import org.chirper.domain.models.view.UserAllViewModel;
import org.chirper.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController extends BaseController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return this.view("login");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return this.view("register");
    }

    @PostMapping("/register")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel) {
        if (!userRegisterBindingModel.getPassword()
                .equals(userRegisterBindingModel.getConfirmPassword())) {
            return this.view("register");
        }

        this.userService.createUser(this.modelMapper.map(userRegisterBindingModel, User.class));

        return this.redirect("/login");
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        User currentLoggedUser = this.userService.getCurrentLoggedUser();
        modelAndView.addObject("currentLoggedUser", currentLoggedUser);

        List<UserAllViewModel> users = this.userService.getAll()
                .stream()
                .map(u -> {
                    UserAllViewModel user = this.modelMapper.map(u, UserAllViewModel.class);
                    user.setAuthorities(
                            u.getAuthorities()
                            .stream()
                            .map(a -> a.getAuthority())
                            .collect(Collectors.toSet())
                    );

                    return user;
                })
                .collect(Collectors.toList());

        modelAndView.addObject("users", users);

        return super.view("manage_roles", modelAndView);
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUser(@PathVariable String id) {
        this.userService.setUserRole(id, "user");

        return super.redirect("/roles");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdmin(@PathVariable String id) {
        this.userService.setUserRole(id, "admin");

        return super.redirect("/roles");
    }
}