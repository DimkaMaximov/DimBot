package dim.application.repository;

import dim.application.model.entity.Rooster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoosterRepository extends JpaRepository<Rooster, Long> {
}
