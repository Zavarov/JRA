package vartas.reddit;

import org.apache.http.client.HttpResponseException;

import javax.annotation.Nonnull;

public class ClientException extends ClientExceptionTOP {
    public ClientException(){
        super();
    }

    public ClientException(int errorCode, @Nonnull String explanation){
        this();
        initCause(new HttpResponseException(errorCode, explanation));
        setErrorCode(errorCode);
        setExplanation(explanation);
    }
}