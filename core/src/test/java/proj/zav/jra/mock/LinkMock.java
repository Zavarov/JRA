package zav.jra.mock;

import zav.jra.models.AbstractLink;
import zav.jra.models.Kind;
import zav.jra.models.Thing;
import zav.jra.models._json.JSONAbstractLink;

public class LinkMock extends AbstractLink {
    public static LinkMock from(Thing thing){
        assert thing.getKind() == Kind.Link;

        LinkMock target = new LinkMock();
        JSONAbstractLink.fromJson(target, thing.getData().toString());
        return target;
    }
}
