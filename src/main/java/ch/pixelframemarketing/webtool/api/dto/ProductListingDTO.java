package ch.pixelframemarketing.webtool.api.dto;

import ch.pixelframemarketing.webtool.data.entity.*;
import ch.pixelframemarketing.webtool.general.enums.ProductType;
import ch.pixelframemarketing.webtool.general.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * This class can hold data for both GameListings and BrandListings.
 * This makes it easier to reuse the same components for both on the front end
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductListingDTO {
    
    public ProductType type;

    public String id;
    public String ownerId;
    public Date createdAt;
    
    public String title;
    public Visibility visibility;
    public String[] tags;
    public String ageRestriction;
    public String thumbnailId;
    public String description;
    
    public String pageContent = "";
    public long views = 0;
    public long[] monthlyRevenue = new long[0];
    public ContractDTO[] contracts = new ContractDTO[0];
    
    public ProductListingDTO(ProductListing productListing, boolean fullFetch) {
        if (productListing instanceof GameListing) type = ProductType.GAME;
        else if (productListing instanceof BrandListing) type = ProductType.BRAND;
        else throw new UnsupportedOperationException();
        
        id = productListing.getId();
        ownerId = productListing.getOwner().getId();
        createdAt = productListing.getCreatedAt();
        title = productListing.getTitle();
        visibility = productListing.getVisibility();
        tags = productListing.getTags();
        ageRestriction = productListing.getAgeRestriction();
        thumbnailId = productListing.getThumbnailMetadata().getId();
        description = productListing.getDescription();
        
        if (!fullFetch) return;
        pageContent = productListing.getPage().getContent();
        views = 0; // TODO: not yet implemented
        monthlyRevenue = new long[0]; // TODO: not yet implemented
        
        List<Contract> contractEntites;
        if (productListing instanceof BrandListing brandListing) contractEntites = brandListing.getContracts();
        else if (productListing instanceof GameListing gameListing) contractEntites = gameListing.getContracts();
        else throw new UnsupportedOperationException();
        contracts = contractEntites.stream().map(contract -> new ContractDTO(contract)).toArray(ContractDTO[]::new);
    }
    
    public GameListing toGameListing() {
        ProductListing productListing = toProductListing();
        if (productListing instanceof GameListing gameListing) {
            return gameListing;
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    public BrandListing toBrandListing() {
        ProductListing productListing = toProductListing();
        if (productListing instanceof BrandListing brandListing) {
            return brandListing;
        } else {
            throw new UnsupportedOperationException();
        }
    }
    
    private ProductListing toProductListing() {
        ProductListing productListing;
        switch (type) {
            case GAME:
                productListing = new GameListing();
                break;
            case BRAND:
                productListing = new BrandListing();
                break;
            default:
                throw new UnsupportedOperationException();
        }
        
        productListing.setId(id);
        productListing.setOwner(new User(ownerId));
        productListing.setCreatedAt(createdAt);
        productListing.setTitle(title);
        productListing.setVisibility(visibility);
        productListing.setTags(tags);
        productListing.setAgeRestriction(ageRestriction);
        productListing.setThumbnailMetadata(new ImageMetadata(thumbnailId));
        productListing.setDescription(description);
        productListing.setPage(new Page(pageContent));
        // TODO: set productListing views
        // TODO: set productListing revenue
        // we do not update contracts, they are currently only ever created / updated manually on the db
        
        return productListing;
    }
}