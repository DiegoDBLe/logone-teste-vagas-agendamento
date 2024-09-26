package com.teste.pratico.controller;


import com.teste.pratico.exception.LimiteAgendamentosExcedidoException;
import com.teste.pratico.exception.VagaNaoEncontradaException;
import com.teste.pratico.model.Agendamento;
import com.teste.pratico.model.Solicitante;
import com.teste.pratico.model.Vaga;
import com.teste.pratico.repository.SolicitanteRepository;
import com.teste.pratico.service.AgendamentoService;
import com.teste.pratico.service.VagaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@ViewScoped
@Data
public class AgendamentoBean {

    private final SolicitanteBean solicitanteBean;
    private final AgendamentoService service;
    private final VagaService vagaService;
    private Agendamento agendamentoSelecionado = new Agendamento();
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer vagasDisponiveis;
    private Long solicitanteId;
    private boolean isValido;
    private List<Agendamento> agendamentos;
    private List<Solicitante> listaSolicitantes;

    @Autowired
    private SolicitanteRepository solicitanteRepository;


    public AgendamentoBean(SolicitanteBean solicitanteBean, AgendamentoService service, VagaService vagaService) {
        this.solicitanteBean = solicitanteBean;
        this.service = service;
        this.vagaService = vagaService;
    }

    @PostConstruct
    public void init() {
        buscarTodosSolicitantes();
    }

    public void buscarTodosSolicitantes() {
        listaSolicitantes = solicitanteBean.buscarTodos();
    }

    private Long getSolicitanteIdPeloNome(String nome) {
        return solicitanteRepository.findByNome(nome)
                .map(Solicitante::getId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitante não encontrado com o nome: " + nome));
    }

    public List<Agendamento> buscarAgendamentosPorPeriodoESolicitante() {
        if (validarDatas()) {

            if (solicitanteId == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Escolha um solicitante", null));
                throw new IllegalArgumentException("Solicitante não pode ser nulo.");
            }

            // Inicializa agendamentos se for nulo
            if (agendamentos == null) {
                agendamentos = new ArrayList<>();
            }

            agendamentos = service.buscarAgendamentosPorPeriodoESolicitante(dataInicio, dataFim, solicitanteId);

            if (agendamentos.isEmpty()) {
                String mensagemErro = String.format("Nenhum agendamento encontrado entre as datas: %s e %s",
                        dataInicio.toString(), dataFim.toString());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagemErro, null));
            }
        }
        return agendamentos;
    }

    public List<Agendamento> buscarAgendamentoPorPeriodo() {

        if (validarDatas()) {
            agendamentos = service.buscarAgendamentosPorPeriodo(dataInicio, dataFim);
            if (agendamentos.isEmpty()){
                String mensagemErro = String.format("Nenhum agendamento encontrado entre as datas: %s e %s",
                        dataInicio.toString(), dataFim.toString());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagemErro, null));
            }
        }
        return agendamentos;
    }

    public void salvarAgendamento() {

        if (agendamentoSelecionado.getNumero() == null || agendamentoSelecionado.getNumero().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo Número é obrigatório.", ""));
            return;
        }
        if (agendamentoSelecionado.getMotivo() == null || agendamentoSelecionado.getMotivo().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O campo Motivo é obrigatório.", ""));
            return;
        }

        try {

            Agendamento agendamento = mapearAgendamento();
            service.salvarAgendamento(agendamento);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Agendamento criado com sucesso!", ""));
            limparFormulario();
        } catch (VagaNaoEncontradaException | LimiteAgendamentosExcedidoException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
        }
    }


    private Agendamento mapearAgendamento() {
        Agendamento agendamento = new Agendamento();
        agendamento.setMotivo(agendamentoSelecionado.getMotivo());
        agendamento.setNumero(agendamentoSelecionado.getNumero());
        agendamento.setData(agendamentoSelecionado.getData());
        agendamento.setSolicitante(buscaSolicitantePeloId());
        return agendamento;
    }

    private void limparFormulario() {
        PrimeFaces.current().executeScript("document.getElementById('form:dataAgendamento_input').value = '';");
        PrimeFaces.current().executeScript("document.getElementById('form:motivoAgendamento').value = '';");
        solicitanteId = null;
        agendamentoSelecionado.setNumero(null);
    }

    private boolean validarDatas() {
        if(dataFim.isBefore(dataInicio)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A data final precisa ser maior que a data inicial.", ""));
            isValido = false;
        } else {
            isValido = true;
        }
        return isValido;
    }

    public void atualizarVagasDisponiveis(SelectEvent event) {
        LocalDate dataSelecionada = (LocalDate) event.getObject();
        List<Vaga> vagasIntersectadas = vagaService.buscaQuantidadeVagasPorPeriodo(dataSelecionada, dataSelecionada);

        if (vagasIntersectadas == null || vagasIntersectadas.isEmpty()) {
            vagasDisponiveis = 0;
        } else {
            vagasDisponiveis = vagasIntersectadas.stream().mapToInt(Vaga::getQuantidade).sum();
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Número de vagas para a data: " + vagasDisponiveis, ""));
    }

    private Solicitante buscaSolicitantePeloId() {
        return service.findById(solicitanteId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitante não encontrado com o ID: " + solicitanteId));
    }

}
