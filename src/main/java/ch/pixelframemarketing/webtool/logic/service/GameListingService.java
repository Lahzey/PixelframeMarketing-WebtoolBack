package ch.pixelframemarketing.webtool.logic.service;

import ch.pixelframemarketing.webtool.data.entity.GameListing;
import ch.pixelframemarketing.webtool.data.entity.ProductListing;
import ch.pixelframemarketing.webtool.data.repository.GameListingRepository;
import ch.pixelframemarketing.webtool.data.repository.ProductListingSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameListingService {
    
    @Autowired
    private GameListingRepository gameListingRepository;
    

    public Page<GameListing> filter(String ownerId, String title, int page, int size) {
        Specification<GameListing> spec = Specification
                .where(new ProductListingSpecification<GameListing>().ownerIdEquals(ownerId))
                .and(new ProductListingSpecification<GameListing>().titleContains(title));
        return gameListingRepository.findAll(spec, PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    public GameListing getGameListing(String id) {
        return gameListingRepository.findById(id).orElseThrow(() -> new RuntimeException("Game Listing not found"));
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
        validateProductListing(gameListing, existingGameListing);
        gameListing.setContracts(existingGameListing != null ? existingGameListing.getContracts() : new ArrayList<>());
    }

    private void validateProductListing(ProductListing productLsting, ProductListing existingProductListing) {
        boolean exists = existingProductListing != null;
        
        // properties that the user may not change
        productLsting.setCreatedAt(exists ? existingProductListing.getCreatedAt() : new Date());
    }

    public void deleteGameListing(String id) {
        gameListingRepository.deleteById(id);
    }


}
