package vartas.jra;

import okhttp3.Response;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public class RateLimiter extends RateLimiterTOP {
    @Nonnull
    private static final String USED = "x-ratelimit-used";
    @Nonnull
    private static final String REMAINING = "x-ratelimit-remaining";
    @Nonnull
    private static final String RESET = "x-ratelimit-reset";

    private LocalDateTime lastResponse;

    @Override
    public void update(Response response) {
        lastResponse = LocalDateTime.now();
        String value = response.header(USED);

        if(value != null)
            setUsed((long)Double.parseDouble(value));

        value = response.header(REMAINING);
        if(value != null)
            setRemaining((long)Double.parseDouble(value));

        value = response.header(RESET);
        if(value != null)
            //In case of fractional seconds, round up
            setReset((long)Math.ceil(Double.parseDouble(value)));
    }

    @Override
    public void acquire() throws InterruptedException {
        //Out of available requests?
        if(getRemaining() <= 0) {
            LocalDateTime now = LocalDateTime.now();
            //Wait until the start of the next period
            if(SECONDS.between(lastResponse, now) < getReset())
                Thread.sleep(getReset() - SECONDS.between(lastResponse, now));
        }
    }

    @Override
    public RateLimiter getRealThis() {
        return this;
    }
}
