package TFC.tekku.services;

import TFC.tekku.entities.Maquina;
import TFC.tekku.entities.NivelFisico;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface MaquinaService {
    Optional<Maquina> findById(@NonNull Long id);
    List<Maquina> findByNivelFisico(NivelFisico nivelFisico);
    List<Maquina> findByTareaId(Long tareaId);
}
