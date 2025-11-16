package TFC.tekku.services;

import TFC.tekku.entities.RegistroJornada;
import TFC.tekku.entities.Turno;
import TFC.tekku.entities.Usuario;
import TFC.tekku.repositories.RegistroJornadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class RegistroJornadaServiceImplements  implements  RegistroJornadaService{

    @Autowired
    private RegistroJornadaRepository repository;

    @Override
    public RegistroJornada save(RegistroJornada registroJornada) {
        return this.repository.save(registroJornada);
    }

    @Override
    public Optional<RegistroJornada> findByUsuarioAndFecha(Long usuarioId, LocalDate fecha) {
        return this.repository.findByUsuarioIdAndFecha(usuarioId, fecha);
    }

    @Override
    public RegistroJornada registrarHoraExtra(Usuario usuario, LocalDate fecha, int tiempoExtra) {
        Optional<RegistroJornada> registroOpt = repository.findByUsuarioIdAndFecha(usuario.getId(), fecha);

        RegistroJornada registro;
        if (registroOpt.isPresent()) {
            registro = registroOpt.get();
            registro.setHoraExtra(true);
            registro.setTiempo(registro.getTiempo() + tiempoExtra);
        } else {
            registro = new RegistroJornada();
            registro.setUsuario(usuario);
            registro.setTurno(usuario.getTurno());
            registro.setFecha(fecha);
            registro.setHoraExtra(true);
            registro.setTiempo(tiempoExtra);
        }

        return repository.save(registro);
    }

    public void actualizarTurnoDeHoy(Long usuarioId, Turno turno) {
        LocalDate hoy = LocalDate.now();

        Optional<RegistroJornada> jornada = repository.findByUsuarioIdAndFecha(usuarioId, hoy);

        jornada.ifPresent(j -> {
            j.setTurno(turno);
            repository.save(j);
        });
    }
}
