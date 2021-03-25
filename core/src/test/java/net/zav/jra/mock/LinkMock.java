package net.zav.jra.mock;

import net.zav.jra.models.AbstractLink;
import net.zav.jra.models.Kind;
import net.zav.jra.models.Thing;
import net.zav.jra.models._json.JSONAbstractLink;

public class LinkMock extends AbstractLink {
    public static LinkMock from(Thing thing){
        assert thing.getKind() == Kind.Link;

        LinkMock target = new LinkMock();
        JSONAbstractLink.fromJson(target, thing.getData().toString());
        return target;
    }
}
