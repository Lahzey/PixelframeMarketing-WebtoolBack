package ch.pixelframemarketing.webtool.data.repository;

import ch.pixelframemarketing.webtool.data.entity.ProductListing;
import ch.pixelframemarketing.webtool.general.enums.Visibility;
import org.springframework.data.jpa.domain.Specification;

public class ProductListingSpecification<T extends ProductListing> {
    
    public Specification<T> ownerIdEquals(String ownerId) {
        return (root, query, criteriaBuilder) -> {
            if (ownerId == null || ownerId.isEmpty()) {
                return criteriaBuilder.conjunction(); // Always true
            }
            return criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
        };
    }

    public Specification<T> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction(); // Always true
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get("title")), "%" + title.toUpperCase() + "%");
        };
    }

    public Specification<T> publicOrOwned(String userId, boolean isAdmin) {
        return (root, query, criteriaBuilder) -> {
            if (isAdmin) {
                return criteriaBuilder.conjunction(); // Always true
            }
            if (userId == null || userId.isEmpty()) {
                return criteriaBuilder.disjunction(); // Always false
            }
            return criteriaBuilder.or(criteriaBuilder.equal(root.get("owner").get("id"), userId), criteriaBuilder.equal(root.get("visibility"), Visibility.PUBLIC));
        };
    }
    
}
