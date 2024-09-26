package com.teste.pratico.exception;

public class LimiteAgendamentosExcedidoException extends RuntimeException{
    public LimiteAgendamentosExcedidoException(String mensagem){
        super(mensagem);
    }
}
