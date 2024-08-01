package ch.pixelframemarketing.webtool.data.repository;

import ch.pixelframemarketing.webtool.data.entity.BrandListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandListingRepository extends JpaRepository<BrandListing, String>, JpaSpecificationExecutor<BrandListing> {
    Optional<BrandListing> findById(String id);
}
