package vartas.reddit;

public class ClientException extends ClientExceptionTOP {
    public ClientException() {
        super();
    }

    public ClientException(Throwable cause) {
        super();
        initCause(cause);
    }
}