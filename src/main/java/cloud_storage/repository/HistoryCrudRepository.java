package cloud_storage.repository;

import cloud_storage.model.history.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryCrudRepository extends JpaRepository<History, Long> {
}
