package TFC.tekku.services;

import TFC.tekku.entities.Maquina;
import TFC.tekku.entities.NivelFisico;
import TFC.tekku.repositories.MaquinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaquinaServiceImplements implements MaquinaService {

    @Autowired
    private MaquinaRepository repository;


    @Override
    public Optional<Maquina> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public List<Maquina> findByNivelFisico(NivelFisico nivelFisico) {
        return  (List)this.repository.findByNivelFisico(nivelFisico);
    }

    @Override
    public List<Maquina> findByTareaId(Long tareaId) {
        return (List)this.repository.findByTareaId(tareaId);
    }
}
