package com.teste.pratico.repository;

import com.teste.pratico.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findByInicioLessThanEqualAndFimGreaterThanEqual(LocalDate data, LocalDate data1);
}
