package ch.pixelframemarketing.webtool.logic.service;

import ch.pixelframemarketing.webtool.data.entity.BrandListing;
import ch.pixelframemarketing.webtool.data.repository.BrandListingRepository;
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
public class BrandListingService {
    
    @Autowired
    private BrandListingRepository brandListingRepository;

    
    public Page<BrandListing> filter(String ownerId, String title, int page, int size) {
        Specification<BrandListing> spec = Specification
                .where(new ProductListingSpecification<BrandListing>().ownerIdEquals(ownerId))
                .and(new ProductListingSpecification<BrandListing>().titleContains(title));
        return brandListingRepository.findAll(spec, PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }
    public Page<BrandListing> getAllBrandListings(int page, int size) {
        return brandListingRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    public BrandListing getBrandListing(String id) {
        return brandListingRepository.findById(id).orElseThrow(() -> new RuntimeException("Game Listing not found"));
    }

    public BrandListing createBrandListing(BrandListing brandListing) {
        brandListing.setCreatedAt(new Date());
        return brandListingRepository.save(brandListing);
    }

    public BrandListing updateBrandListing(BrandListing brandListing) {
        validateBrandListing(brandListing);
        return brandListingRepository.save(brandListing);
    }
    
    private void validateBrandListing(BrandListing brandListing) {
        BrandListing existingBrandListing = brandListing.getId() != null ? brandListingRepository.findById(brandListing.getId()).orElse(null) : null;
        
        // properties that the user may not change
        brandListing.setContracts(existingBrandListing != null ? existingBrandListing.getContracts() : new ArrayList<>());
    }

    public void deleteBrandListing(String id) {
        brandListingRepository.deleteById(id);
    }


}
