package ch.pixelframemarketing.webtool.api.dto;

import ch.pixelframemarketing.webtool.data.entity.Contract;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractDTO {
    
    public String id;

    public String gameId;
    public String brandId;

    public ContractDTO(Contract contract) {
        id = contract.getId();
        gameId = contract.getGameListing().getId();
        brandId = contract.getBrandListing().getId();
    }
}
