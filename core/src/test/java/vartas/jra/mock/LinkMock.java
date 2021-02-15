package vartas.jra.mock;

import vartas.jra.models.AbstractLink;
import vartas.jra.models.Kind;
import vartas.jra.models.Thing;
import vartas.jra.models._json.JSONAbstractLink;

public class LinkMock extends AbstractLink {
    public static LinkMock from(Thing thing){
        assert thing.getKind() == Kind.Link;

        LinkMock target = new LinkMock();
        JSONAbstractLink.fromJson(target, thing.getData().toString());
        return target;
    }
}
