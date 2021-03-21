package vartas.jra;

import vartas.jra.models.Kind;
import vartas.jra.models.Thing;
import vartas.jra.models._json.JSONAbstractLink;

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
