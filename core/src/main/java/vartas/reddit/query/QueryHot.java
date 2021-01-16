package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class QueryHot<T> extends QueryMany<T,QueryHot<T>> {
    protected static final String REGION = "g";
    public QueryHot(Function<Thing, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(mapper, client, endpoint, args);
    }

    @Override
    protected QueryHot<T> getRealThis() {
        return this;
    }

    public QueryHot<T> setRegion(@Nonnull Region region){
        params.put(REGION, region);
        return this;
    }

    public enum Region{
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
}
