package org.chirper.service;

import org.chirper.domain.entities.Chirp;
import org.chirper.domain.entities.User;
import org.chirper.domain.models.binding.ChirpCreateBindingModel;
import org.chirper.domain.models.binding.ChirpEditBindingModel;
import org.chirper.domain.models.view.UserAllLikesViewModel;
import org.chirper.error.ChirpNotFoundException;
import org.chirper.repository.ChirpRepository;
import org.chirper.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChirpServiceImpl implements ChirpService {
    private final UserService userService;

    private final UserRepository userRepository;

    private final ChirpRepository chirpRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ChirpServiceImpl(UserService userService, UserRepository userRepository, ChirpRepository chirpRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.chirpRepository = chirpRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void likeAndUnlikeAChirp(String chirpId) {
        Chirp currentChirp = this.chirpRepository.findById(chirpId)
                .orElseThrow(() -> new ChirpNotFoundException("Chirp with the given id was not found!"));

        User currentLoggedUser = this.userService.getCurrentLoggedUser();

        boolean isChirpLikeExist = currentLoggedUser.isChirpLikeExist(currentChirp);

        if (isChirpLikeExist) {
            currentLoggedUser.removeChirpLike(currentChirp);
        } else {
            currentLoggedUser.addChirpLike(currentChirp);
        }

        this.userRepository.saveAndFlush(currentLoggedUser);
        this.chirpRepository.saveAndFlush(currentChirp);
    }

    @Override
    public List<UserAllLikesViewModel> getChirpLikes(String chirpId) {
        Chirp currentChirp = this.chirpRepository
                .findById(chirpId)
                .orElseThrow(() -> new ChirpNotFoundException("Chirp with the given id was not found!"));

        List<UserAllLikesViewModel> likesUsers = currentChirp.getUserLikes()
                .stream()
                .map(user -> this.modelMapper.map(user, UserAllLikesViewModel.class))
                .collect(Collectors.toList());

        return likesUsers;
    }

    @Override
    public List<Chirp> getAllByFollowingUsers(String currentUserId) {
        List<Chirp> allChirps = this.chirpRepository.findAllByFollowedUsers(currentUserId);

        return allChirps;
    }

    @Override
    public void createChirp(ChirpCreateBindingModel chirpCreateBindingModel) {
        User author = this.userService.getCurrentLoggedUser();

        Chirp chirp = new Chirp(
                chirpCreateBindingModel.getContent(),
                LocalDateTime.now(),
                author);

        this.chirpRepository.saveAndFlush(chirp);
        this.userRepository.saveAndFlush(author);
    }

    @Override
    public boolean deleteChirp(String chirpId) {
        Chirp chirp = this.chirpRepository.findById(chirpId)
                .orElseThrow(() -> new ChirpNotFoundException("Chirp with the given id was not found!"));

//        User author = this.userService.getCurrentLoggedUser();

//        if (!author.isAuthor(chirp)) {
//            return false;
//        }

        List<User> chirpUserLikes = chirp.getUserLikes();

        for (User user : chirpUserLikes) {
            user.removeChirpLike(chirp);
        }

        this.chirpRepository.delete(chirp);

        return true;
    }

    @Override
    public boolean editChirp(String chirpId, @ModelAttribute(name = "model") ChirpEditBindingModel chirpEditBindingModel) {
        Chirp chirp = this.chirpRepository.findById(chirpId)
                .orElseThrow(() -> new ChirpNotFoundException("Chirp with the given id was not found!"));

//        if (!author.isAuthor(chirp)) {
//            return false;
//        }

        chirp.setContent(chirpEditBindingModel.getContent());
        chirp.setDateAdded(LocalDateTime.now());

        this.chirpRepository.saveAndFlush(chirp);

        return true;
    }
}
