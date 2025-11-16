package TFC.tekku.services;

import TFC.tekku.entities.Tarea;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface TareaService {


    Optional<Tarea> findById(@NonNull Long id);

}
