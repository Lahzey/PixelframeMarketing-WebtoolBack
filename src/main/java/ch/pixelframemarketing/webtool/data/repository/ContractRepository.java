package ch.pixelframemarketing.webtool.data.repository;

import ch.pixelframemarketing.webtool.data.entity.Contract;
import ch.pixelframemarketing.webtool.data.entity.GameListing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, String> {
    List<Contract> findByGameListing(GameListing gameListing);
}
