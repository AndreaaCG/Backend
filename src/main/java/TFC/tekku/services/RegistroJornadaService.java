package TFC.tekku.services;

import TFC.tekku.entities.RegistroJornada;
import TFC.tekku.entities.Turno;
import TFC.tekku.entities.Usuario;

import java.time.LocalDate;
import java.util.Optional;

public interface RegistroJornadaService {

    RegistroJornada save (RegistroJornada registroJornada);
    Optional<RegistroJornada> findByUsuarioAndFecha(Long usuarioId, LocalDate fecha);
    RegistroJornada registrarHoraExtra(Usuario usuario, LocalDate fecha, int tiempoExtra);
    void actualizarTurnoDeHoy(Long usuarioId, Turno turno);

}
