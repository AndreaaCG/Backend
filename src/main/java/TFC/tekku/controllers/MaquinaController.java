package TFC.tekku.controllers;

import TFC.tekku.entities.Maquina;
import TFC.tekku.entities.Tarea;
import TFC.tekku.entities.Usuario;
import TFC.tekku.services.MaquinaService;
import TFC.tekku.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "{http://localhost:4200}")
@RestController
@RequestMapping({"/maquinas"})
public class MaquinaController {

    @Autowired
    private MaquinaService maquinaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> maquinasUsuario(@PathVariable  Long id){

        Optional<Usuario> usuarioOptional = usuarioService.findById(id);

        if(usuarioOptional.isEmpty()){
            return ResponseEntity.badRequest().body("El usuario no existe");
        }

        List<Maquina> maquinas = usuarioOptional.get().getMaquinas();

        return ResponseEntity.ok(maquinas);

    }

    @PostMapping("/poner-maquina/{maquinaId}/usuario/{usuarioId}")
    public ResponseEntity<?> ponerMaquina(@PathVariable Long maquinaId, @PathVariable Long usuarioId){

        Optional<Usuario> usuarioOptional = usuarioService.findById(usuarioId);
        Optional<Maquina> maquinaOptional = maquinaService.findById(maquinaId);

        if(usuarioOptional.isEmpty() || maquinaOptional.isEmpty()){
            return  ResponseEntity.badRequest().body("El usuario o la máquina no existe");
        }

        Usuario usuario = usuarioOptional.get();
        Maquina maquina = maquinaOptional.get();

        Tarea tareaMaquina = maquina.getTarea();

        boolean tieneTarea = usuario.getUsuarioTareas().stream().anyMatch(ut -> ut.getTarea().getId().equals(tareaMaquina.getId()));

        if(tieneTarea){

            usuario.getMaquinas().add(maquina);
            return ResponseEntity.ok(usuarioService.save(usuario));
        }

        return ResponseEntity.badRequest().body("El usuario no tiene asignada ninguna tarea para esa maquina");

    }

}
