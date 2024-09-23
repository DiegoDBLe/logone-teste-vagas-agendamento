package com.teste.pratico.repository;

import com.teste.pratico.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findByInicioLessThanEqualAndFimGreaterThanEqual(LocalDate data, LocalDate data1);

    @Query("SELECT SUM(v.quantidade) FROM Vaga v WHERE v.inicio <= :dataFim AND v.fim >= :dataInicio")
    Integer findTotalQuantidadeByPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
