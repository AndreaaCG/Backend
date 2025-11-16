package TFC.tekku.controllers;

import TFC.tekku.entities.RegistroJornada;
import TFC.tekku.entities.Turno;
import TFC.tekku.entities.Usuario;
import TFC.tekku.services.RegistroJornadaService;
import TFC.tekku.services.TurnoService;
import TFC.tekku.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({"/jornada"})
public class RegistroJornadaController {

    @Autowired
    private RegistroJornadaService jornadaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TurnoService turnoService;

    @PostMapping("/usuario/{usuarioId}/turno/{turnoId}")
    public ResponseEntity<?> registrarJornada(@PathVariable Long usuarioId, @PathVariable Long turnoId, @RequestBody RegistroJornada registroJornada){

        Optional<Usuario>usuarioOptional = usuarioService.findById(usuarioId);
        Optional<Turno>turnoOptional = turnoService.findById(turnoId);

        if (usuarioOptional.isPresent() && turnoOptional.isPresent()){
            LocalDate fecha = registroJornada.getFecha();
            Optional<RegistroJornada> jornadaExistente = jornadaService.findByUsuarioAndFecha(usuarioId, fecha);

            if (jornadaExistente.isPresent()){
                return ResponseEntity.badRequest().body("Ya existe un registro para este trabajador");
            }

            RegistroJornada nuevaJornada = new RegistroJornada();
            nuevaJornada.setUsuario(usuarioOptional.get());
            nuevaJornada.setTurno(turnoOptional.get());
            nuevaJornada.setFecha(fecha);
            nuevaJornada.setHoraExtra(false);
            nuevaJornada.setTiempo(0);

           jornadaService.save(nuevaJornada);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaJornada);

        }
        return ResponseEntity.badRequest().body("Usuario o turno no encontrado");

    }

    @GetMapping("/usuario/{usuarioId}/fecha/{fecha}")
    public ResponseEntity<?> obtenerJornadaFecha(@PathVariable Long usuarioId, @PathVariable String fecha){

        LocalDate fechaBuscada = LocalDate.parse(fecha);
        Optional<RegistroJornada> jornada = jornadaService.findByUsuarioAndFecha(usuarioId, fechaBuscada);

        if (jornada.isPresent()){
            return ResponseEntity.ok(jornada.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró jornada para el usuario en esa fecha");

    }

    @PostMapping("/usuarios/{usuarioId}/hora-extra")
    public ResponseEntity<?> registrarHoraExtra(@PathVariable Long usuarioId, @RequestParam LocalDate fecha, @RequestParam int tiempoExtra) {

        Optional<Usuario> usuarioOptional = usuarioService.findById(usuarioId);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con ID: " + usuarioId);
        }

        Usuario usuario = usuarioOptional.get();
        RegistroJornada jornada = jornadaService.registrarHoraExtra(usuario, fecha, tiempoExtra);

        return ResponseEntity.ok(jornada);
    }


}
