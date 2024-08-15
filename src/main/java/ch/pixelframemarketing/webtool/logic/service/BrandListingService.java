package ch.pixelframemarketing.webtool.logic.service;

import ch.pixelframemarketing.webtool.data.entity.BrandListing;
import ch.pixelframemarketing.webtool.data.entity.GameListing;
import ch.pixelframemarketing.webtool.data.entity.User;
import ch.pixelframemarketing.webtool.data.repository.BrandListingRepository;
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
public class BrandListingService {

    @Autowired
    private UserService userService;
    
    @Autowired
    private BrandListingRepository brandListingRepository;

    
    public Page<BrandListing> filter(String ownerId, String title, int page, int size) {
        User user = userService.getCurrentUser();
        Specification<BrandListing> spec = Specification
                .where(new ProductListingSpecification<BrandListing>().ownerIdEquals(ownerId))
                .and(new ProductListingSpecification<BrandListing>().titleContains(title))
                .and(new ProductListingSpecification<BrandListing>().publicOrOwned(user.getId(), user.getRole() == User.Role.ADMIN));
        return brandListingRepository.findAll(spec, PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }
    public Page<BrandListing> getAllBrandListings(int page, int size) {
        return brandListingRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    public BrandListing getBrandListingOrNull(String id) {
        return brandListingRepository.findById(id).orElse(null);
    }

    public BrandListing createBrandListing(BrandListing brandListing) {
        validateBrandListing(brandListing);
        return brandListingRepository.save(brandListing);
    }

    public BrandListing updateBrandListing(BrandListing brandListing) {
        validateBrandListing(brandListing);
        return brandListingRepository.save(brandListing);
    }
    
    private void validateBrandListing(BrandListing brandListing) {
        BrandListing existingBrandListing = brandListing.getId() != null ? brandListingRepository.findById(brandListing.getId()).orElse(null) : null;
        brandListing.validate(existingBrandListing);
        brandListing.setContracts(existingBrandListing != null ? existingBrandListing.getContracts() : new ArrayList<>());
    }

    public void deleteBrandListing(String id) {
        brandListingRepository.deleteById(id);
    }


}
