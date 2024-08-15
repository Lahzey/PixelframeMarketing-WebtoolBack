package ch.pixelframemarketing.webtool.logic.service;

import ch.pixelframemarketing.webtool.data.entity.GameListing;
import ch.pixelframemarketing.webtool.data.entity.User;
import ch.pixelframemarketing.webtool.data.repository.GameListingRepository;
import ch.pixelframemarketing.webtool.data.repository.ProductListingSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class GameListingService {

    @Autowired
    private UserService userService;
    
    @Autowired
    private GameListingRepository gameListingRepository;
    

    public Page<GameListing> filter(String ownerId, String title, int page, int size) {
        User user = userService.getCurrentUser();
        Specification<GameListing> spec = Specification
                .where(new ProductListingSpecification<GameListing>().ownerIdEquals(ownerId))
                .and(new ProductListingSpecification<GameListing>().titleContains(title))
                .and(new ProductListingSpecification<GameListing>().publicOrOwned(user.getId(), user.getRole() == User.Role.ADMIN));
        return gameListingRepository.findAll(spec, PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    public GameListing getGameListingOrNull(String id) {
        return gameListingRepository.findById(id).orElse(null);
    }

    public GameListing createGameListing(GameListing gameListing) {
        validateGameListing(gameListing);
        return gameListingRepository.save(gameListing);
    }

    public GameListing updateGameListing(GameListing gameListing) {
        validateGameListing(gameListing);
        return gameListingRepository.save(gameListing);
    }
    
    private void validateGameListing(GameListing gameListing) {
        GameListing existingGameListing = gameListing.getId() != null ? gameListingRepository.findById(gameListing.getId()).orElse(null) : null;
        gameListing.validate(existingGameListing);
        gameListing.setContracts(existingGameListing != null ? existingGameListing.getContracts() : new ArrayList<>());
    }

    public void deleteGameListing(String id) {
        gameListingRepository.deleteById(id);
    }


}
