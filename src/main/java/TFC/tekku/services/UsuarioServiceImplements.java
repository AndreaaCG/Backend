

package TFC.tekku.services;

import TFC.tekku.entities.Usuario;
import TFC.tekku.repositories.TareaRepository;
import TFC.tekku.repositories.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImplements implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Transactional(
            readOnly = true
    )
    public List<Usuario> findAll() {
        return (List)this.repository.findAll();
    }

    @Transactional(
            readOnly = true
    )
    public Optional<Usuario> findById(Long id) {
        return this.repository.findById(id);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        return (Usuario)this.repository.save(usuario);
    }

    @Transactional
    public void deleteById(Long id) {
        this.repository.deleteById(id);
    }

}
