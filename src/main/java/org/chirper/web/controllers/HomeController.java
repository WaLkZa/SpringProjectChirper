package org.chirper.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {
    @GetMapping("/")
    public ModelAndView index(Authentication authentication) {
            return this.redirect("login");
    }

    @GetMapping("/feed")
    public ModelAndView home(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("username", authentication.getName());

//        if (this.getPrincipalAuthority(authentication) != null
//                && this.getPrincipalAuthority(authentication).equals("ADMIN")){
//            return this.view("admin-home", modelAndView);
//        }

        return this.view("feed", modelAndView);
    }

    @GetMapping("/discover")
    public ModelAndView discover(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("username", authentication.getName());

        return this.view("discover", modelAndView);
    }

    @GetMapping("/profile")
    public ModelAndView profile(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("username", authentication.getName());

        return this.view("profile", modelAndView);
    }
}
