package vartas.reddit;

public class UnsuccessfulRequestException extends UnsuccessfulRequestExceptionTOP{
    public UnsuccessfulRequestException(){
        super();
    }

    public UnsuccessfulRequestException(Throwable cause){
        this();
        initCause(cause);
    }
}
