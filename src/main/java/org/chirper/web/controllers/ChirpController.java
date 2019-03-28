package org.chirper.web.controllers;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.domain.models.binding.ChirpCreateBindingModel;
import org.chirper.domain.models.binding.ChirpEditBindingModel;
import org.chirper.repository.ChirpRepository;
import org.chirper.repository.UserRepository;
import org.chirper.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
public class ChirpController extends BaseController {
    private final UserService userService;

    private final UserRepository userRepository;

    private final ChirpRepository chirpRepository;

    @Autowired
    public ChirpController(UserRepository userRepository, ChirpRepository chirpRepository, ModelMapper modelMapper, UserService userService) {
        this.userRepository = userRepository;
        this.chirpRepository = chirpRepository;
        this.userService = userService;
    }

    @PostMapping("/chirp/create")
    public ModelAndView createChirpConfirm(ChirpCreateBindingModel chirpCreateBindingModel) {

        User author = this.userService.getCurrentLoggedUser();

        Chirp chirp = new Chirp(
                chirpCreateBindingModel.getContent(),
                LocalDateTime.now(),
                author);

        this.chirpRepository.saveAndFlush(chirp);
        this.userRepository.saveAndFlush(author);

        return super.redirect("/profile");
    }

    @GetMapping("/chirp/edit/{id}")
    public ModelAndView editChirp(@PathVariable(name = "id") String id,
                                  Authentication authentication,
                                  ModelAndView modelAndView) {

        if (!this.chirpRepository.existsById(id)) {
            return super.redirect("/profile");
        }

        User author = this.userService.getCurrentLoggedUser();
        Chirp chirp = this.chirpRepository.findById(id).get();

        if (!author.isAuthor(chirp)) {
            return super.redirect("/profile");
        }

        modelAndView.addObject("loggedUsername", authentication.getName());
        modelAndView.addObject("chirp", chirp);

        return super.view("chirp/edit", modelAndView);
    }

    @PostMapping("/chirp/edit/{id}")
    public ModelAndView editChirpConfirm(@PathVariable(name = "id") String id,
                                         ChirpEditBindingModel chirpEditBindingModel,
                                  ModelAndView modelAndView) {

        User author = this.userService.getCurrentLoggedUser();
        Chirp chirp = this.chirpRepository.findById(id).get();

        if (!author.isAuthor(chirp)) {
            return super.redirect("/profile");
        }

        chirp.setContent(chirpEditBindingModel.getContent());
        chirp.setDateAdded(LocalDateTime.now());

        this.chirpRepository.saveAndFlush(chirp);

        return super.redirect(id);
    }

    @GetMapping("/chirp/delete/{id}")
    public ModelAndView deleteChirpConfirm(@PathVariable(name = "id") String id) {
        if (!this.chirpRepository.existsById(id)) {
            return super.redirect("/profile");
        }

        User author = this.userService.getCurrentLoggedUser();
        Chirp chirp = this.chirpRepository.findById(id).get();

        if (!author.isAuthor(chirp)) {
            return super.redirect("/profile");
        }

        this.userRepository.saveAndFlush(author);

        this.chirpRepository.deleteById(id);

        return super.redirect("/profile");
    }
}
