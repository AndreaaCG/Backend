package TFC.tekku.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioTareaId implements Serializable {

    private Long usuarioId;
    private Long tareaId;


    public UsuarioTareaId() {}

    public UsuarioTareaId(Long usuarioId, Long tareaId) {
        this.usuarioId = usuarioId;
        this.tareaId = tareaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getTareaId() {
        return tareaId;
    }

    public void setTareaId(Long tareaId) {
        this.tareaId = tareaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioTareaId)) return false;
        UsuarioTareaId that = (UsuarioTareaId) o;
        return Objects.equals(usuarioId, that.usuarioId) &&
                Objects.equals(tareaId, that.tareaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, tareaId);
    }

}
