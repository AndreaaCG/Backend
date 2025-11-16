package TFC.tekku.controllers;

import TFC.tekku.entities.Tarea;
import TFC.tekku.entities.Usuario;
import TFC.tekku.entities.UsuarioTarea;
import TFC.tekku.services.TareaService;
import TFC.tekku.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({"/tareas"})
public class TareaController {

     @Autowired
     private TareaService service;

     @Autowired
     private UsuarioService usuarioService;

     @PutMapping("/completado/{usuarioId}/{tareaId}")
     public ResponseEntity<?> tareaCompletada(@PathVariable Long usuarioId, @PathVariable Long tareaId){

         Optional<Usuario> usuarioOptional = usuarioService.findById(usuarioId);
         Optional<Tarea> tareaOptional = service.findById(tareaId);

         if(usuarioOptional.isEmpty() || tareaOptional.isEmpty()){
             return  ResponseEntity.badRequest().body("No existe el usuario o la tarea");
         }

         Usuario usuario = usuarioOptional.get();
         Tarea tarea = tareaOptional.get();

         //Decimos de la lista de tareas filtra por aquella tareas que tengan el mismo id y devuelveme el primer registro.
         UsuarioTarea tareaAsiganda =  usuario.getUsuarioTareas().stream().filter(ut -> ut.getTarea().getId().equals(tarea.getId())).findFirst().orElse(null);

         if(tareaAsiganda == null){
            return ResponseEntity.badRequest().body("No se puede completar una tarea que no esta asignada al usuario");
         }

         tareaAsiganda.setCompletado(true);
         usuarioService.save(usuario);
         return  ResponseEntity.ok().build();

     }



}
