package TFC.tekku.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;


@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;


    private String nombre;


    private String apellido1;


    private String apellido2;


    private String email;


    private int edad;

    @Enumerated(EnumType.STRING)
    private NivelFisico nivelFisico;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioTarea> usuarioTareas = new ArrayList<>();


    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    @ManyToOne
    @JoinTable(
            name = "usuarios_turnos",
            joinColumns = {@JoinColumn(name = "usuarios_id")},
            inverseJoinColumns =  {@JoinColumn(name = "turnos_id")}
    )
    private  Turno turno;

    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuarios_maquinas",
            joinColumns = {@JoinColumn(name = "id_usuarios")},
            inverseJoinColumns = {@JoinColumn(name = "id_maquinas")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"id_usuarios", "id_maquinas"})}

    )
    private List<Maquina> maquinas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public NivelFisico getNivelFisico() {
        return nivelFisico;
    }

    public void setNivelFisico(NivelFisico nivelFisico) {
        this.nivelFisico = nivelFisico;
    }

    public List<UsuarioTarea> getUsuarioTareas() {
        return usuarioTareas;
    }

    public void setUsuarioTareas(List<UsuarioTarea> usuarioTareas) {
        this.usuarioTareas = usuarioTareas;
    }

    public List<Maquina> getMaquinas() {
        return maquinas;
    }

    public void setMaquinas(List<Maquina> maquinas) {
        this.maquinas = maquinas;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
}

