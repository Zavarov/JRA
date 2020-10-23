package vartas.reddit;

public class ServerException extends ServerExceptionTOP {
    public ServerException() {
        super();
    }

    public ServerException(Throwable cause) {
        super();
        initCause(cause);
    }
}