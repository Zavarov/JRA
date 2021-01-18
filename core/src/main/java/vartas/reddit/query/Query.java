package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.exceptions.HttpException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A query is request with additional arguments. The query acts as a request builder, in order to avoid overloading the
 * call methods and to handle default values.
 * @param <T> The expected return type.
 */
public abstract class Query <T, Q extends Query<T,Q>> {
    /**
     * The client is required for building and executing the query.
     */
    protected final Client client;
    /**
     * The endpoint the query is directed to.
     */
    protected final Endpoint endpoint;
    /**
     * Arguments for potential wildcards in the endpoint.<p>
     * E.g the endpoint {@link Endpoint#GET_SUBREDDIT_ABOUT} with the argument {@code RedditDev} results in the
     * qualified endpoint {@code /r/RedditDev/about}.
     */
    protected final Object[] args;
    /**
     * A map containing all customizable parameters of the query. Those are attached to the qualified endpoint as
     * additional arguments <p>
     * E.g. {@link Endpoint#GET_TOP} specifies a time period as an optional parameters, such that {@code /top?t=all} is
     * a valid endpoint.
     */
    protected final Map<String,Object> params = new HashMap<>();

    /**
     * Provides a reference to the explicit query type. In fashion of the builder pattern, potential setter methods
     * for the parameters return a reference to the query. This method is required to ensure that all of those instances
     * are of the same type.
     * @return {@code this}
     */
    protected abstract Q getRealThis();

    /**
     *
     * @param client The client executing the query.
     * @param endpoint The endpoint the query is directed to.
     * @param args Potential arguments quantifying wildcards in the endpoint.
     */
    public Query(Client client, Endpoint endpoint, Object... args){
        this.client = client;
        this.endpoint = endpoint;
        this.args = args;
    }

    /**
     * Executes the request and extracts the expected data type from the JSON response.
     * @return An instance of the requested data.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     */
    public abstract T query() throws IOException, HttpException, InterruptedException;

    public enum GeoLocation {
        GLOBAL,
        US, AR, AU, BG, CA, CL,
        CO, HR, CZ, FI, FR, DE,
        GR, HU, IS, IN, IE, IT,
        JP, MY, MX, NZ, PH, PL,
        PT, PR, RO, RS, SG, ES,
        SE, TW, TH, TR, GB,
        US_WA, US_DE, US_DC, US_WI, US_WV,
        US_HI, US_FL, US_WY, US_NH, US_NJ,
        US_NM, US_TX, US_LA, US_NC, US_ND,
        US_NE, US_TN, US_NY, US_PA, US_CA,
        US_NV, US_VA, US_CO, US_AK, US_AL,
        US_AR, US_VT, US_IL, US_GA, US_IN,
        US_IA, US_OK, US_AZ, US_ID, US_CT,
        US_ME, US_MD, US_MA, US_OH, US_UT,
        US_MO, US_MN, US_MI, US_RI, US_KS,
        US_MT, US_MS, US_SC, US_KY, US_OR,
        US_SD;

        @Override
        public String toString(){
            return name();
        }
    }

    /**
     * Is used to limit the number of requested things by age.
     */
    public enum TimePeriod{
        /**
         * Only requests things made within the last hour.
         */
        HOUR,
        /**
         * Only requests things made within the last day.
         */
        DAY,
        /**
         * Only requests things made within the last week.
         */
        WEEK,
        /**
         * Only requests things made within the last month.
         */
        MONTH,
        /**
         * Only requests things made within the last year.
         */
        YEAR,
        /**
         * Accept all things, no matter their age.
         */
        ALL;

        public final String key = name().toLowerCase(Locale.ENGLISH);
    }
}
