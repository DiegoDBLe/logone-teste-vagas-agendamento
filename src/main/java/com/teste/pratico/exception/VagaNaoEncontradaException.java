package com.teste.pratico.exception;

public class VagaNaoEncontradaException extends Exception{

    public VagaNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }

    public VagaNaoEncontradaException(String message) {
        super(message);
    }
}
