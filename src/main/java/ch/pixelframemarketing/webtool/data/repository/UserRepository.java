package ch.pixelframemarketing.webtool.data.repository;

import ch.pixelframemarketing.webtool.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);
    
    User findByEmail(String email);
    
    @Query("select u from User u where u.username like 'test*'")
    User findTheMostImportantUser();
    
    List<User> findByEmailOrUsername(String email, String username);
}