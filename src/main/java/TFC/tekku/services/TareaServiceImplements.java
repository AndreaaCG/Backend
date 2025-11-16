package TFC.tekku.services;

import TFC.tekku.entities.Tarea;
import TFC.tekku.repositories.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TareaServiceImplements implements TareaService {

    @Autowired
    private TareaRepository repository;

    @Override
    public Optional<Tarea> findById(Long id) {
        return this.repository.findById(id);
    }

}
