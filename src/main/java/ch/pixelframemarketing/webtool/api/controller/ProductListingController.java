package ch.pixelframemarketing.webtool.api.controller;

import ch.pixelframemarketing.webtool.api.dto.ProductListingDTO;
import ch.pixelframemarketing.webtool.api.interceptor.Secure;
import ch.pixelframemarketing.webtool.data.entity.*;
import ch.pixelframemarketing.webtool.general.enums.ProductType;
import ch.pixelframemarketing.webtool.logic.service.BrandListingService;
import ch.pixelframemarketing.webtool.logic.service.GameListingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductListingController {
    private final GameListingService gameListingService;
    private final BrandListingService brandListingService;


    @GetMapping("/{id}")
    @Secure
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ProductListingDTO> getProductListing(@PathVariable(value = "id") String id) {
        ProductListingDTO result;
        GameListing gameListing = gameListingService.getGameListingOrNull(id);
        if (gameListing != null) {
            result = new ProductListingDTO(gameListing, true);
        } else {
            BrandListing brandListing = brandListingService.getBrandListingOrNull(id);
            if (brandListing != null) {
                result = new ProductListingDTO(brandListing, true);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    @Secure
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ProductListingDTO> createProductListing(@RequestBody ProductListingDTO productListingDTO) {
        if (productListingDTO.type == ProductType.GAME) {
            productListingDTO = new ProductListingDTO(gameListingService.createGameListing(productListingDTO.toGameListing()), true);
        } else if (productListingDTO.type == ProductType.BRAND) {
            productListingDTO = new ProductListingDTO(brandListingService.createBrandListing(productListingDTO.toBrandListing()), true);
        } else {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.created(URI.create("/api/products/" + productListingDTO.id)).body(productListingDTO);
    }
    
    @PutMapping("/{id}")
    @Secure
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ProductListingDTO> updateProductListing(@PathVariable(value = "id") String id, @RequestBody ProductListingDTO productListingDTO) {
        productListingDTO.id = id;
        if (productListingDTO.type == ProductType.GAME) {
            productListingDTO = new ProductListingDTO(gameListingService.updateGameListing(productListingDTO.toGameListing()), true);
        } else if (productListingDTO.type == ProductType.BRAND) {
            productListingDTO = new ProductListingDTO(brandListingService.updateBrandListing(productListingDTO.toBrandListing()), true);
        } else {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(productListingDTO);
    }

    @GetMapping
    @Secure
    public ResponseEntity<Page<ProductListingDTO>> fitlerProductListings(
            @RequestParam(value = "type") ProductType type,
            @RequestParam(value = "ownerId") String ownerId,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        Page<? extends ProductListing> entityPage;
        if (type == ProductType.GAME) {
            entityPage = gameListingService.filter(ownerId, title, page, size);
        } else {
            entityPage = brandListingService.filter(ownerId, title, page, size);
        }
        return ResponseEntity.ok().body(entityPage.map(gameListing -> new ProductListingDTO(gameListing, false)));
    }
}
