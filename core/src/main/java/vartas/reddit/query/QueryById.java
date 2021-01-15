package vartas.reddit.query;

import de.se_rwth.commons.Joiners;
import vartas.reddit.Link;
import vartas.reddit.Query;

import javax.annotation.Nonnull;
import java.util.List;

public class QueryById extends Query<Link, QueryById> {
    /**
     * A comma-separated list of {@link Link} fullnames.
     */
    private static final String NAMES = "names";

    @Override
    protected QueryById getRealThis() {
        return this;
    }

    public QueryById setNames(@Nonnull String... names){
        args.put(NAMES, Joiners.COMMA.join(names));
        return this;
    }

    @Override
    public List<Link> query() {
        throw new UnsupportedOperationException();
    }
}
