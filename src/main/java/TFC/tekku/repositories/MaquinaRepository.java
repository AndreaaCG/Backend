package TFC.tekku.repositories;

import java.util.List;
import java.util.Optional;

import TFC.tekku.entities.Maquina;
import TFC.tekku.entities.NivelFisico;
import org.springframework.data.repository.CrudRepository;

public interface MaquinaRepository extends CrudRepository<Maquina,Long> {

    List<Maquina> findByNivelFisico(NivelFisico nivelFisico);

    List<Maquina> findByTareaId(Long tareaId);
}
