package com.teste.pratico.controller;

import com.teste.pratico.model.Solicitante;
import com.teste.pratico.service.SolicitanteService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ViewScoped
@Data
public class SolicitanteBean {

    private final SolicitanteService service;

    private String solicitanteNome;

    public SolicitanteBean(SolicitanteService service) {
        this.service = service;
    }

    public List<Solicitante> buscarTodos(){
        return service.buscarTodos();
    }

    public void salvarSolicitante(){
        Solicitante solicitante = new Solicitante();
        solicitante.setNome(solicitanteNome);

        service.salvarSolicitante(solicitante);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitante salvo com sucesso!", ""));
        limparCampos();
    }


    private void limparCampos() {
        solicitanteNome = null;
    }

}
