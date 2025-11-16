package TFC.tekku.repositories;

import TFC.tekku.entities.RegistroJornada;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RegistroJornadaRepository extends CrudRepository<RegistroJornada, Long> {

    Optional<RegistroJornada> findByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);
}
