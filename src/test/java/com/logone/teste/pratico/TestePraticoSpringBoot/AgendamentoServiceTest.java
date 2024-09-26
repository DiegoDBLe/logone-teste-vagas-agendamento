package com.logone.teste.pratico.TestePraticoSpringBoot;

import com.teste.pratico.exception.VagaNaoEncontradaException;
import com.teste.pratico.model.Agendamento;
import com.teste.pratico.model.Solicitante;
import com.teste.pratico.model.Vaga;
import com.teste.pratico.repository.AgendamentoRepository;
import com.teste.pratico.service.AgendamentoService;
import com.teste.pratico.service.SolicitanteService;
import com.teste.pratico.service.VagaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AgendamentoServiceTest {

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private VagaService vagaService;

    @Mock
    SolicitanteService solicitanteService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveLancarExcecaoQuandoSolicitanteUltrapassarLimiteDeAgendamentos(){

        Agendamento agendamento = new Agendamento();
        agendamento.setData(LocalDate.now());
        Solicitante solicitante = new Solicitante();
        solicitante.setId(1L);
        agendamento.setSolicitante(solicitante);

        //Simular a disponibilidade de vagas (ex: 16 vagas)
        Vaga vaga = new Vaga();
        vaga.setQuantidade(16);
        List<Vaga> vagasDisponiveis = Arrays.asList(vaga);

        //Simula os agendamentos já realizados (4agendamentos para um solicitsante ou seja 25%)
        Agendamento agendamentoExistente = new Agendamento();
        List<Agendamento> agendamentosExistentes = Arrays.asList(agendamentoExistente, agendamentoExistente, agendamentoExistente, agendamentoExistente);

        when(vagaService.buscaQuantidadeVagasPorPeriodo(any(LocalDate.class), any(LocalDate.class))).thenReturn(vagasDisponiveis);

        when(agendamentoRepository.buscarAgendamentosPorPeriodoESolicitante(any(LocalDate.class), any(LocalDate.class), anyLong()))
                .thenReturn(agendamentosExistentes);

        assertThrows(RuntimeException.class, () -> agendamentoService.salvarAgendamento(agendamento));

        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }

    @Test
    public void deveSAlvarAgendamentoQuandoSolicitanteNaoUltrapassarLimite() throws VagaNaoEncontradaException{

        Agendamento agendamento = new Agendamento();
        agendamento.setData(LocalDate.now());
        Solicitante solicitante = new Solicitante();
        solicitante.setId(1L);
        agendamento.setSolicitante(solicitante);

        // Simular a disponibilidade de vagas (16vagas)
        Vaga vaga = new Vaga();
        vaga.setQuantidade(16);
        List<Vaga> vagasDisponiveis = Arrays.asList(vaga);

        // Simula nenhum agendamento
        List<Agendamento> agendamentosExistentes = Collections.emptyList();

        when(vagaService.buscaQuantidadeVagasPorPeriodo(any(LocalDate.class), any(LocalDate.class))).thenReturn(vagasDisponiveis);

        when(agendamentoRepository.buscarAgendamentosPorPeriodoESolicitante(any(LocalDate.class), any(LocalDate.class), anyLong()))
                .thenReturn(agendamentosExistentes);

        agendamentoService.salvarAgendamento(agendamento);

        // verificar se o método de salvar foi chamado
        verify(agendamentoRepository, times(1)).save(agendamento);
    }
}
