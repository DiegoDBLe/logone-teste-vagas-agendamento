package com.teste.pratico.controller;

import com.teste.pratico.model.Agendamento;
import com.teste.pratico.model.Vaga;
import com.teste.pratico.repository.AgendamentoRepository;
import com.teste.pratico.service.VagaService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@ViewScoped
public class VagaBean {

    private final VagaService service;
    private boolean isValido;
    private Vaga vagaSelecionada = new Vaga();

    private LocalDate dataInicioBusca;
    private LocalDate dataFimBusca;
    private List<Vaga> vagasEncontradas = new ArrayList<>();
    private Integer totalQuantidade;
    private int totalAgendamentosFeitos;
    private int vagasDisponiveis;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public VagaBean(VagaService service) {
        this.service = service;
    }

    public void salvarVaga() {
        if (validarDatas()) {
            Vaga vaga = mapearVaga();

            service.salvarVaga(vaga);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Vagas criadas com sucesso!", ""));
            limparFormulario();
        }
    }

    public void buscarVagasPorPeriodo() {
        if (dataInicioBusca != null && dataFimBusca != null && validarDatasBusca()) {

            vagasEncontradas = service.buscaQuantidadeVagasPorPeriodo(dataInicioBusca, dataFimBusca);

            totalQuantidade = 0;

            for (Vaga vaga : vagasEncontradas) {
                totalQuantidade += vaga.getQuantidade();
            }

            if (vagasEncontradas.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhuma vaga encontrada para o período informado.", ""));
            } else {
                List<Agendamento> agendamentosFeitos = agendamentoRepository.buscarAgendamentosPorPeriodo(dataInicioBusca, dataFimBusca);

                totalAgendamentosFeitos = agendamentosFeitos.size();

                vagasDisponiveis = totalQuantidade - totalAgendamentosFeitos;

                if (vagasDisponiveis < 0) {
                    vagasDisponiveis = 0; // Evitar mostrar números negativos de vagas
                }

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Total de Vagas no Período: " + totalQuantidade +
                                        " | Total de Agendamentos Feitos: " + totalAgendamentosFeitos +
                                        " | Vagas Disponíveis: " + vagasDisponiveis, ""));
            }
        }
    }

    private boolean validarDatasBusca() {
        if (dataFimBusca.isBefore(dataInicioBusca)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A data final precisa ser maior que a data inicial.", ""));
            return false;
        }
        return true;
    }

    private Vaga mapearVaga() {
        Vaga vaga = new Vaga();
        vaga.setInicio(vagaSelecionada.getInicio());
        vaga.setFim(vagaSelecionada.getFim());
        vaga.setQuantidade(vagaSelecionada.getQuantidade());
        return vaga;
    }

    private boolean validarDatas() {
        if (vagaSelecionada.getFim().isBefore(vagaSelecionada.getInicio())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A data final precisa ser maior que a data inicial.", ""));
            isValido = false;
        } else {
            isValido = true;
        }
        return isValido;
    }

    private void limparFormulario() {
        PrimeFaces.current().executeScript("document.getElementById('form:dataInicio_input').value = '';");
        PrimeFaces.current().executeScript("document.getElementById('form:dataFim_input').value = '';");
        vagaSelecionada.setQuantidade(null);
    }
}
