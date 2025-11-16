package TFC.tekku.services;

import TFC.tekku.entities.Turno;

import java.util.Optional;

public interface TurnoService {

    Optional<Turno> findById(Long id);
}
