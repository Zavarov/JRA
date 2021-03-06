package vartas.jra;

import vartas.jra._json.JSONLink;
import vartas.jra.models.Kind;
import vartas.jra.models.Thing;

public class Link extends LinkTOP{
    public static Link from(Thing thing){
        assert thing.getKind() == Kind.Link;

        Link target = new Link();
        JSONLink.fromJson(target, thing.getData().toString());
        return target;
    }

    @Override
    public Link getRealThis() {
        return this;
    }
}
