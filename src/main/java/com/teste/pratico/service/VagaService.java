package com.teste.pratico.service;

import com.teste.pratico.model.Vaga;
import com.teste.pratico.repository.VagaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VagaService {

    private final VagaRepository repository;

    VagaService(VagaRepository repository) {
        this.repository = repository;
    }

    public void salvarVaga(Vaga vaga) {
        repository.save(vaga);
    }

    public List<Vaga> buscaQuantidadeVagasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return repository.findByInicioLessThanEqualAndFimGreaterThanEqual(dataInicio, dataFim);
    }

}
