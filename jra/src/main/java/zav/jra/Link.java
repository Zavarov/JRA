package zav.jra;

import zav.jra.models.Kind;
import zav.jra.models.Thing;
import zav.jra.models._json.JSONAbstractLink;

public class Link extends LinkTOP{
    public static Link from(Thing thing){
        assert thing.getKind() == Kind.Link;

        Link target = new Link();
        JSONAbstractLink.fromJson(target, thing.getData().toString());
        return target;
    }

    @Override
    public Link getRealThis() {
        return this;
    }
}
