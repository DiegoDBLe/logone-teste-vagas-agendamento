package com.teste.pratico.repository;

import com.teste.pratico.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("SELECT a FROM Agendamento a WHERE a.data >= :dataInicio AND a.data <= :dataFim")
    List<Agendamento> buscarAgendamentosPorPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT a FROM Agendamento a WHERE a.data >= :dataInicio AND a.data <= :dataFim AND a.solicitante.id = :solicitanteId")
    List<Agendamento> buscarAgendamentosPorPeriodoESolicitante(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("solicitanteId") Long solicitanteId
    );
}
