package dim.application.service.api;

import dim.application.model.entity.Rooster;

import java.util.List;

public interface RoosterService {

    List<Rooster> getAll();

    Rooster getById(Long id);

    Rooster save(Rooster rooster);

    void clearStatistics();
}