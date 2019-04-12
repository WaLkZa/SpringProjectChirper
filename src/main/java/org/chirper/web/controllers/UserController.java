package org.chirper.web.controllers;

import org.chirper.domain.entities.User;
import org.chirper.domain.models.binding.UserRegisterBindingModel;
import org.chirper.domain.models.view.UserAllViewModel;
import org.chirper.service.UserService;
import org.chirper.validation.user.UserRegisterValidator;
import org.chirper.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

    private final UserRegisterValidator userRegisterValidator;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, UserRegisterValidator userRegisterValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userRegisterValidator = userRegisterValidator;
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public ModelAndView login() {
        return this.view("login");
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public ModelAndView register(ModelAndView modelAndView,
                                 @ModelAttribute(name = "model") UserRegisterBindingModel model) {
        modelAndView.addObject("model", model);

        return this.view("register", modelAndView);
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(
            ModelAndView modelAndView, @ModelAttribute(name = "model") UserRegisterBindingModel userRegisterBindingModel,
            BindingResult bindingResult) {

        this.userRegisterValidator.validate(userRegisterBindingModel, bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("model", userRegisterBindingModel);

            return this.view("register", modelAndView);
        }

        this.userService.createUser(this.modelMapper.map(userRegisterBindingModel, User.class));

        return this.redirect("/login");
    }

    @GetMapping("/discover")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Discover users")
    public ModelAndView discover(ModelAndView modelAndView) {
        User currentLoggedUser = this.userService.getCurrentLoggedUser();

        List<User> allUsers = this.userService.getAll();

        modelAndView.addObject("allUsers", allUsers);
        modelAndView.addObject("currentLoggedUser", currentLoggedUser);

        return super.view("discover", modelAndView);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("My profile")
    public ModelAndView profile(ModelAndView modelAndView) {
        User currentLoggedUser = this.userService.getCurrentLoggedUser();

        modelAndView.addObject("currentLoggedUser", currentLoggedUser);

        return super.view("profile", modelAndView);
    }

    @GetMapping("/profile/{usernameArg}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Foreign profile")
    public ModelAndView profile(@PathVariable("usernameArg") String usernameArg,
                                ModelAndView modelAndView) {

        User currentLoggedUser = this.userService.getCurrentLoggedUser();
        modelAndView.addObject("currentLoggedUser", currentLoggedUser);

        User user = (User) this.userService.loadUserByUsername(usernameArg);

        if (currentLoggedUser.getId().equals(user.getId())) {
            return super.view("profile", modelAndView);
        }

        modelAndView.addObject("user", user);

        return super.view("foreign_profile", modelAndView);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Users roles")
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