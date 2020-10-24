package vartas.reddit;

import org.apache.http.client.HttpResponseException;

import javax.annotation.Nonnull;

public class ServerException extends ServerExceptionTOP {
    public ServerException(){
        super();
    }

    public ServerException(int errorCode, @Nonnull String explanation){
        this();
        initCause(new HttpResponseException(errorCode, explanation));
        setErrorCode(errorCode);
        setExplanation(explanation);
    }
}