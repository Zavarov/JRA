package proj.zav.jra.mock;

import proj.zav.jra.models.AbstractLink;
import proj.zav.jra.models.Kind;
import proj.zav.jra.models.Thing;
import proj.zav.jra.models._json.JSONAbstractLink;

public class LinkMock extends AbstractLink {
    public static LinkMock from(Thing thing){
        assert thing.getKind() == Kind.Link;

        LinkMock target = new LinkMock();
        JSONAbstractLink.fromJson(target, thing.getData().toString());
        return target;
    }
}
