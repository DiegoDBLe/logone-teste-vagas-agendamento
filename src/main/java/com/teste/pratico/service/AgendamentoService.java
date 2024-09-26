package com.teste.pratico.service;

import com.teste.pratico.exception.LimiteAgendamentosExcedidoException;
import com.teste.pratico.exception.VagaNaoEncontradaException;
import com.teste.pratico.model.Agendamento;
import com.teste.pratico.model.Solicitante;
import com.teste.pratico.model.Vaga;
import com.teste.pratico.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final VagaService vagaService;

    private final SolicitanteService solicitanteService;

    AgendamentoService(AgendamentoRepository repository, VagaService vagaService, SolicitanteService solicitanteService) {
        this.agendamentoRepository = repository;
        this.vagaService = vagaService;
        this.solicitanteService = solicitanteService;
    }

    @Transactional
    public void salvarAgendamento(Agendamento agendamento) throws VagaNaoEncontradaException {
        List<Vaga> vagasDisponiveis = vagaService.buscaQuantidadeVagasPorPeriodo(agendamento.getData(), agendamento.getData());

        if (vagasDisponiveis.isEmpty() || vagasDisponiveis.stream().noneMatch(vaga -> vaga.getQuantidade() > 0)) {
            throw new VagaNaoEncontradaException("Não há vagas disponíveis para a data indicada.");
        }

        //Calcula o total de vagas disponiveis no periodo
        int totalVagas = vagasDisponiveis.stream().mapToInt(Vaga::getQuantidade).sum();

        //busca o numero de agendamentos feito por solicitante no periodo
        List<Agendamento> agendamentosDoSolicitante = agendamentoRepository.buscarAgendamentosPorPeriodoESolicitante(
                agendamento.getData(),
                agendamento.getData(),
                agendamento.getSolicitante().getId()
        );

        // Calculo para limitar a quantidade de agendamentos para 25% das vagas dispoiniveis
        int limiteAgendamentosPorSolicitante = (int) Math.ceil(totalVagas * 0.25);

        if (agendamentosDoSolicitante.size() >= limiteAgendamentosPorSolicitante){
            throw new LimiteAgendamentosExcedidoException("Você já atingiu o limite de 25% das vagas disponíveis para o periodo.");
        }

        agendamentoRepository.save(agendamento);
        atualizaNumeroVagasAposAgendamento(vagasDisponiveis);
    }

    private List<Vaga> buscaQuantidadedeVagasPorPeriodo(Agendamento agendamento) {
        return vagaService.buscaQuantidadeVagasPorPeriodo(agendamento.getData(), agendamento.getData());
    }

    public boolean verificaDisponibilidadeVagas(List<Vaga> vagasDisponiveis) {
        return !vagasDisponiveis.isEmpty();
    }

    private static void atualizaNumeroVagasAposAgendamento(List<Vaga> vagasDisponiveis) {
        vagasDisponiveis.stream()
                .filter(vaga -> vaga.getQuantidade() > 0)
                .forEach(vaga -> vaga.setQuantidade(vaga.getQuantidade() - 1));
    }

    public List<Agendamento> buscarAgendamentosPorPeriodoESolicitante(LocalDate dataInicio, LocalDate dataFim, Long solicitanteId) {
        return agendamentoRepository.buscarAgendamentosPorPeriodoESolicitante(dataInicio, dataFim, solicitanteId);
    }


    public List<Agendamento> buscarAgendamentosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {

        return agendamentoRepository.buscarAgendamentosPorPeriodo(dataInicio, dataFim).stream()
                .peek(agendamento -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String dataFormatada = agendamento.getData().format(formatter);
                    agendamento.setData(LocalDate.parse(dataFormatada, formatter));
                })
                .toList();
    }

    public Optional<Solicitante> findById(Long solicitanteId) {
        return solicitanteService.encontrarPorId(solicitanteId);
    }

}
