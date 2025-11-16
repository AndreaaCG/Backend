package TFC.tekku.services;

import TFC.tekku.entities.Turno;
import TFC.tekku.repositories.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TurnoServiceImplements implements  TurnoService{

    @Autowired
    private TurnoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Turno> findById(Long id) {
        return this.repository.findById(id);
    }


}
