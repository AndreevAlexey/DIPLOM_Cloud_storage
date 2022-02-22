package cloud_storage.repository;

import cloud_storage.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCrudRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String username);
}
