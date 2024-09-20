package com.logone.teste.pratico.TestePraticoSpringBoot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.teste.pratico.model.Agendamento;
import com.teste.pratico.repository.AgendamentoRepository;
import com.teste.pratico.service.AgendamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AgendamentoRepositoryTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @InjectMocks
    private AgendamentoService agendamentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuscarAgendamentosPorPeriodo() {
        // Dados de teste
        LocalDate dataInicio = LocalDate.of(2023, 1, 1);
        LocalDate dataFim = LocalDate.of(2023, 1, 31);

        Agendamento agendamento1 = new Agendamento();
        agendamento1.setId(1L);
        agendamento1.setData(LocalDate.of(2023, 1, 15));
        agendamento1.setNumero("001");
        agendamento1.setMotivo("Consulta");

        Agendamento agendamento2 = new Agendamento();
        agendamento2.setId(2L);
        agendamento2.setData(LocalDate.of(2023, 1, 20));
        agendamento2.setNumero("002");
        agendamento2.setMotivo("Exame");

        List<Agendamento> agendamentos = Arrays.asList(agendamento1, agendamento2);

        // Configurar o comportamento do repositório simulado
        when(agendamentoRepository.buscarAgendamentosPorPeriodo(dataInicio, dataFim)).thenReturn(agendamentos);

        // Chamar o método e verificar o resultado
        List<Agendamento> result = agendamentoService.buscarAgendamentosPorPeriodo(dataInicio, dataFim);

        assertThat(result).hasSize(2);
        assertThat(result).contains(agendamento1, agendamento2);

        // Verificar se o método do repositório foi chamado
        verify(agendamentoRepository, times(1)).buscarAgendamentosPorPeriodo(dataInicio, dataFim);
    }

}
