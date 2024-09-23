package com.teste.pratico.repository;

import com.teste.pratico.model.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Long> {

    Optional<Solicitante> findByNome(String nome);
}
