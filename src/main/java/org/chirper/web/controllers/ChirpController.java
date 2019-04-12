package org.chirper.web.controllers;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.domain.models.binding.ChirpCreateBindingModel;
import org.chirper.domain.models.binding.ChirpEditBindingModel;
import org.chirper.repository.ChirpRepository;
import org.chirper.repository.UserRepository;
import org.chirper.service.ChirpService;
import org.chirper.service.UserService;
import org.chirper.validation.chirp.ChirpCreateValidator;
import org.chirper.web.annotations.PageTitle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
public class ChirpController extends BaseController {
    private final UserService userService;

    private final ChirpService chirpService;

    private final UserRepository userRepository;

    private final ChirpRepository chirpRepository;

    private final ChirpCreateValidator createValidator;

    @Autowired
    public ChirpController(UserRepository userRepository, ChirpRepository chirpRepository, ModelMapper modelMapper, UserService userService, ChirpService chirpService, ChirpCreateValidator createValidator) {
        this.userRepository = userRepository;
        this.chirpRepository = chirpRepository;
        this.userService = userService;
        this.chirpService = chirpService;
        this.createValidator = createValidator;
    }

    @PostMapping("/chirp/create")
    public ModelAndView createChirpConfirm(ModelAndView modelAndView, @ModelAttribute(name = "model") ChirpCreateBindingModel chirpCreateBindingModel, BindingResult bindingResult) {
        this.createValidator.validate(chirpCreateBindingModel, bindingResult);

        if (bindingResult.hasErrors()) {
            User currentLoggedUser = this.userService.getCurrentLoggedUser();

            modelAndView.addObject("currentLoggedUser", currentLoggedUser);
            modelAndView.addObject("model", chirpCreateBindingModel);

            return super.view("profile", modelAndView);
        }

        this.chirpService.createChirp(chirpCreateBindingModel);

        return super.redirect("/profile");
    }

    @GetMapping("/chirp/edit/{id}")
    @PageTitle("Edit chirp")
    public ModelAndView editChirp(@PathVariable(name = "id") String id,
                                  Authentication authentication,
                                  ModelAndView modelAndView,
                                  @ModelAttribute(name = "model") ChirpEditBindingModel chirpEditBindingModel) {

        if (!this.chirpRepository.existsById(id)) {
            return super.redirect("/profile");
        }

        User author = this.userService.getCurrentLoggedUser();
        Chirp chirp = this.chirpRepository.findById(id).get();

//        if (!author.isAuthor(chirp)) {
//            return super.redirect("/profile");
//        }

        modelAndView.addObject("currentLoggedUser", author);
        modelAndView.addObject("chirp", chirp);
//        modelAndView.addObject("model", chirpEditBindingModel);

        return super.view("chirp/edit", modelAndView);
    }

    @PostMapping("/chirp/edit/{id}")
    public ModelAndView editChirpConfirm(@PathVariable(name = "id") String id,
                                         Authentication authentication,
                                         @ModelAttribute(name = "model") ChirpEditBindingModel chirpEditBindingModel,
                                  ModelAndView modelAndView, BindingResult bindingResult) {

//        this.editValidator.validate(chirpEditBindingModel, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            User currentLoggedUser = this.userService.getCurrentLoggedUser();
//
//            modelAndView.addObject("currentLoggedUser", currentLoggedUser);
//            modelAndView.addObject("model", chirpEditBindingModel);
//
//            return super.view("chirp/edit", modelAndView);
//        }

        //        User author = this.userService.getCurrentLoggedUser();
        Chirp chirp = this.chirpRepository.findById(id).get();

//        if (!author.isAuthor(chirp)) {
//            return super.redirect("/profile");
//        }

        chirp.setContent(chirpEditBindingModel.getContent());
        chirp.setDateAdded(LocalDateTime.now());

        this.chirpRepository.saveAndFlush(chirp);

        return super.redirect(id);
    }

    @GetMapping("/chirp/delete/{id}")
    public ModelAndView deleteChirpConfirm(@PathVariable(name = "id") String id,
                                           HttpServletRequest request) {

        if (!this.chirpRepository.existsById(id)) {
            return super.redirect("/profile");
        }

//        User author = this.userService.getCurrentLoggedUser();
//        Chirp chirp = this.chirpRepository.findById(id).get();
//
//        if (!author.isAuthor(chirp)) {
//            return super.redirect("/profile");
//        }

//        this.userRepository.saveAndFlush(author);

        this.chirpRepository.deleteById(id);

        return super.redirect(request.getHeader("Referer"));
    }
}
