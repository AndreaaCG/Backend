
package TFC.tekku.services;

import TFC.tekku.entities.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;

public interface UsuarioService {
    List<Usuario> findAll();

    Optional<Usuario> findById(@NonNull Long id);

    Usuario save(Usuario usuario);

    void deleteById(Long id);

}
