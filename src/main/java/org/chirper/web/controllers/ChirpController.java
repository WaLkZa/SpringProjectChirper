package org.chirper.web.controllers;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.domain.models.binding.ChirpCreateBindingModel;
import org.chirper.domain.models.binding.ChirpEditBindingModel;
import org.chirper.repository.ChirpRepository;
import org.chirper.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ChirpController extends BaseController {

    private final UserRepository userRepository;

    private final ChirpRepository chirpRepository;

    @Autowired
    public ChirpController(UserRepository userRepository, ChirpRepository chirpRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.chirpRepository = chirpRepository;
    }


    @PostMapping("/chirp/create")
    public ModelAndView createChirpConfirm(ChirpCreateBindingModel chirpCreateBindingModel) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User author = this.userRepository.findByUsername(userDetails.getUsername()).get();
        author.incrementChirpsCounter();

        Chirp chirp = new Chirp(
                chirpCreateBindingModel.getContent(),
                LocalDateTime.now(),
                author);

        this.chirpRepository.saveAndFlush(chirp);
        this.userRepository.saveAndFlush(author);

        return this.redirect("/profile");
    }

    @GetMapping("/chirp/edit/{id}")
    public ModelAndView editChirp(@PathVariable(name = "id") String id,
                                  Authentication authentication,
                                  ModelAndView modelAndView) {

        if (!this.chirpRepository.existsById(id)) {
            return redirect("/profile");
        }

        Optional<Chirp> chirp = this.chirpRepository.findById(id);

        modelAndView.addObject("loggedUsername", authentication.getName());
        modelAndView.addObject("chirp", chirp);

        return this.view("chirp/edit", modelAndView);
    }

    @PostMapping("/chirp/edit/{id}")
    public ModelAndView editChirpConfirm(@PathVariable(name = "id") String id,
                                         ChirpEditBindingModel chirpEditBindingModel,
                                  ModelAndView modelAndView) {

        Chirp chirp = this.chirpRepository.findById(id).get();

        chirp.setContent(chirpEditBindingModel.getContent());
        chirp.setDateAdded(LocalDateTime.now());

        this.chirpRepository.saveAndFlush(chirp);

        return redirect(id);
    }

    @GetMapping("/chirp/delete/{id}")
    public ModelAndView deleteChirpConfirm(@PathVariable(name = "id") String id) {
        if (!this.chirpRepository.existsById(id)) {
            return redirect("/profile");
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User author = this.userRepository.findByUsername(userDetails.getUsername()).get();
        author.decrementChirpsCounter();
        this.userRepository.saveAndFlush(author);

        this.chirpRepository.deleteById(id);

        return this.redirect("/profile");
    }
}
