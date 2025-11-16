package TFC.tekku.controllers;

import TFC.tekku.entities.*;
import TFC.tekku.services.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({"/usuarios"})
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private TareaService tareaService;

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private MaquinaService maquinaService;

    @Autowired
    private RegistroJornadaService jornadaService;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return this.service.findAll();
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<?> perfil(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = this.service.findById(id);
        return usuarioOptional.isPresent() ? ResponseEntity.status(HttpStatus.OK).body((Usuario)usuarioOptional.orElseThrow()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "El usuario no se encontró por el id: " + id));
    }

    //Creamos el usuario
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid Usuario usuario, BindingResult result) {
        return result.hasErrors() ? this.validation(result) : ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(usuario));
    }

    //Updatemos el usuario
    @PutMapping({"/{id}"})
    public ResponseEntity<?> update(@RequestBody @Valid Usuario usuario, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return this.validation(result);
        } else {
            Optional<Usuario> usuarioOptional = this.service.findById(id);
            if (usuarioOptional.isPresent()) {
                Usuario usuarioDb = (Usuario)usuarioOptional.get();
                usuarioDb.setNombre(usuario.getNombre());
                usuarioDb.setApellido1(usuario.getApellido1());
                usuarioDb.setApellido2(usuario.getApellido2());
                usuarioDb.setEmail(usuario.getEmail());
                usuarioDb.setEdad(usuario.getEdad());
                usuarioDb.setNivelFisico(usuario.getNivelFisico());
                return ResponseEntity.ok(this.service.save(usuarioDb));
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    //Borramos el usuario
    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = this.service.findById(id);
        if (usuarioOptional.isPresent()) {
            this.service.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Se elimino correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Creamos la tarea
    @PostMapping("/{usuarioId}/poner-tareas/{tareaId}")
    public ResponseEntity<?> ponerTarea(@PathVariable Long usuarioId, @PathVariable Long tareaId){

        Optional<Usuario> usuarioOptional = this.service.findById(usuarioId);
        Optional<Tarea> tareaOptional = this.tareaService.findById(tareaId);

        if(usuarioOptional.isPresent() && tareaOptional.isPresent()){

            boolean aniadir_tarea = validarDificultad(usuarioOptional, tareaOptional);
            if(aniadir_tarea == true){
                ResponseEntity<?> respuesta = validarHoras(tareaOptional, usuarioOptional);

                if(respuesta.getStatusCode() == HttpStatus.OK){

                    boolean existe = usuarioOptional.get().getUsuarioTareas().stream().anyMatch(ut -> ut.getTarea().getId().equals(tareaId));

                    if(existe){
                        return ResponseEntity.badRequest().body("Esta tarea ya la tiene asignada el usuario");
                    }
                    UsuarioTarea usuarioTarea = new UsuarioTarea();
                    usuarioTarea.setTarea(tareaOptional.get());
                    usuarioTarea.setUsuario(usuarioOptional.get());

                    usuarioOptional.get().getUsuarioTareas().add(usuarioTarea);

                    return  ResponseEntity.ok(this.service.save(usuarioOptional.get()));
                }
                return  respuesta;
            }

            return  ResponseEntity.badRequest().body("No hay una máquina compatible con tu nivel fisico");

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Borramos la tarea
    @DeleteMapping("/{usuarioId}/borrar-tareas/{tareaId}")
    public ResponseEntity<?> borrarTarea (@PathVariable Long usuarioId, @PathVariable Long tareaId) {

            Optional<Usuario> usuarioOptional = this.service.findById(usuarioId);
            Optional<Tarea> tareaOptional = this.tareaService.findById(tareaId);
            if (usuarioOptional.isPresent() && tareaOptional.isPresent()) {

                usuarioOptional.get().getUsuarioTareas().removeIf(ut -> ut.getTarea().getId().equals(tareaId));
                return ResponseEntity.ok(this.service.save(usuarioOptional.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
    }

    @PutMapping("/{usuarioId}/gestion-turno/{turnoId}")
    public ResponseEntity<?> gestionTurno(@PathVariable Long usuarioId, @PathVariable Long turnoId) {

        Optional<Usuario> usuarioOptional = this.service.findById(usuarioId);
        Optional<Turno> turnoOptional = this.turnoService.findById(turnoId);

        if (usuarioOptional.isPresent() && turnoOptional.isPresent()) {

            Usuario usuarioDb = usuarioOptional.get();
            Turno nuevoTurno = turnoOptional.get();

            usuarioDb.setTurno(nuevoTurno);

            Usuario usuarioGuardado = this.service.save(usuarioDb);
            jornadaService.actualizarTurnoDeHoy(usuarioId, nuevoTurno);

            return ResponseEntity.ok(usuarioGuardado);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{usuarioId}/tareas")
    public ResponseEntity<?> obtenerTareas(@PathVariable Long usuarioId){

        Optional<Usuario> usuarioOptional = this.service.findById(usuarioId);

        if(usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();

            List<UsuarioTarea> tareas = usuario.getUsuarioTareas();

            return ResponseEntity.ok(tareas);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no econtrado");
    }

    private int convertirNivel(NivelFisico nivel){
        return switch (nivel){
            case Bajo  -> 1;
            case Medio -> 2;
            case Alto -> 3;
            default -> 0;
        };
    }


    private boolean validarDificultad(Optional<Usuario> usuarioOptional, Optional<Tarea> tareaOptional){
        List <Maquina> maquinasAsociadas = maquinaService.findByTareaId(tareaOptional.get().getId());
        if(maquinasAsociadas.isEmpty()){
            return false;
        }
        int nivelUsuario = convertirNivel(usuarioOptional.get().getNivelFisico());
        boolean puedeUsar = maquinasAsociadas.stream().anyMatch(maquina ->  convertirNivel(maquina.getNivelFisico()) <= nivelUsuario );
        return puedeUsar;
    }


    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap();
        result.getFieldErrors().forEach((error) -> {
            String var10001 = error.getField();
            String var10002 = error.getField();
            errors.put(var10001, "El campo " + var10002 + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    private ResponseEntity<?> validarHoras(Optional<Tarea> tareaOptional, Optional<Usuario> usuarioOptional){
        Tarea tarea = (Tarea)tareaOptional.get();
        Usuario usuario = usuarioOptional.get();

        int maximo_horas = 480;
        double horas =  0.016666666666667;

        Optional<RegistroJornada> jornadaOptional = jornadaService.findByUsuarioAndFecha(usuario.getId(), LocalDate.now());

        if(jornadaOptional.isEmpty()){
            return ResponseEntity.badRequest().body("No hay jornada declara para el dia de hoy: " + LocalDate.now());
        }

        RegistroJornada jornada = jornadaOptional.get();
        if(jornada.isHoraExtra()){
            maximo_horas += jornada.getTiempo();
        }

        int totalTareas = usuario.getUsuarioTareas().stream().mapToInt(ut -> ut.getTarea().getDuracion()).sum();
        int totalNuevo = totalTareas + tarea.getDuracion();

        if(totalNuevo > maximo_horas){
            return ResponseEntity.badRequest().body("No se puede asignar la tarea, supera el limite de horas: " + (int)(maximo_horas * horas) +" incluyendo las horas extras" );
        }

        return ResponseEntity.ok().build();
    }

}
