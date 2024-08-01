package ch.pixelframemarketing.webtool.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "game_listing_id", nullable = false)
    private GameListing gameListing;

    @ManyToOne
    @JoinColumn(name = "brand_listing_id", nullable = false)
    private BrandListing brandListing;
}
