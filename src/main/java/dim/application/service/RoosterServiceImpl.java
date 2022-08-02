package dim.application.service;

import dim.application.model.entity.Rooster;
import dim.application.repository.RoosterRepository;
import dim.application.service.api.RoosterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoosterServiceImpl implements RoosterService {

    private final RoosterRepository repository;

    public RoosterServiceImpl(RoosterRepository repository) {
        this.repository = repository;
    }

    public List<Rooster> getAll(){
        return repository.findAll();
    }

    @Override
    public Rooster getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Rooster save(Rooster rooster) {
        return repository.save(rooster);
    }

    public void clearStatistics (){
        repository.saveAll(repository.findAll().stream().peek(rooster -> rooster.setMonthCount(0L)).collect(Collectors.toList()));
    }
}