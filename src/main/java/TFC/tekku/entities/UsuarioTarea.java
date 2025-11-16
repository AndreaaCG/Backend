package TFC.tekku.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios_tareas")
public class UsuarioTarea {

    @EmbeddedId
    private UsuarioTareaId id = new UsuarioTareaId();

    @ManyToOne
    @MapsId("usuarioId")
    @JsonIgnore
    @JoinColumn(name = "usuarios_id")
    private Usuario usuario;

    @ManyToOne
    @MapsId("tareaId")
    @JoinColumn(name = "tareas_id")
    private Tarea tarea;

    private boolean completado = false;

    public UsuarioTareaId getId() {
        return id;
    }

    public void setId(UsuarioTareaId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
