package net.zav.jra;

import net.zav.jra.models.Kind;
import net.zav.jra.models.Thing;
import net.zav.jra.models._json.JSONAbstractLink;

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
