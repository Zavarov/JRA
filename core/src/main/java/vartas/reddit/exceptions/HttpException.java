package vartas.reddit.exceptions;

import javax.annotation.Nonnull;

public class HttpException extends HttpExceptionTOP{
    public HttpException(){
        super();
    }

    public HttpException(int errorCode, @Nonnull String explanation){
        this();
        setErrorCode(errorCode);
        setExplanation(explanation);
    }

    @Override
    public HttpException getRealThis() {
        return this;
    }
}
