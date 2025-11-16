package TFC.tekku.entities;

import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "maquinas")
public class Maquina {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private NivelFisico nivelFisico;

    @ManyToOne
    @JoinColumn(name = "id_tarea")
    private Tarea tarea;

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public NivelFisico getNivelFisico() {
        return nivelFisico;
    }

    public void setNivelFisico(NivelFisico nivelFisico) {
        this.nivelFisico = nivelFisico;
    }



}
