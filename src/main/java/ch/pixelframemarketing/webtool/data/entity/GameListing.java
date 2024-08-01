package ch.pixelframemarketing.webtool.data.entity;

import ch.pixelframemarketing.webtool.general.enums.Visibility;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity(name = "game_listing")
@Getter
@Setter
@NoArgsConstructor
public class GameListing extends ProductListing {

    @OneToMany(mappedBy = "gameListing", fetch = FetchType.EAGER)
    private List<Contract> contracts;

    public GameListing(String id, User owner, Date createdAt, String title, Visibility visibility, String[] tags, String ageRestriction, ImageMetadata thumbnailMetadata, String description, Page page, List<Contract> contracts) {
        super(id, owner, createdAt, title, visibility, tags, ageRestriction, thumbnailMetadata, description, page);
        this.contracts = contracts;
    }

    public GameListing(String id) {
        setId(id);
    }
    
}
