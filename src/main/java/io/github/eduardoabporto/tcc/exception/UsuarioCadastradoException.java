package io.github.eduardoabporto.tcc.exception;

public class UsuarioCadastradoException extends RuntimeException {

    public UsuarioCadastradoException( String login ){
        super("UsuÃ¡rio jÃ¡ cadastrado para o login " + login);
    }
}
