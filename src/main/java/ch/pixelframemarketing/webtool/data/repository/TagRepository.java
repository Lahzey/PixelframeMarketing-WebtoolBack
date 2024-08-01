package ch.pixelframemarketing.webtool.data.repository;

import ch.pixelframemarketing.webtool.data.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    
}
