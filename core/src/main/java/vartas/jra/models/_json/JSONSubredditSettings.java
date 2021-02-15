package vartas.jra.models._json;

import org.json.JSONObject;
import vartas.jra.models.Kind;
import vartas.jra.models.SubredditSettings;
import vartas.jra.models.Thing;

public class JSONSubredditSettings extends JSONSubredditSettingsTOP{
    public static SubredditSettings fromThing(String source){
        return fromThing(JSONThing.fromJson(source));
    }

    public static SubredditSettings fromThing(Thing source){
        assert source.getKind() == Kind.SubredditSettings;

        SubredditSettings target = new SubredditSettings();
        fromJson(target, source.getData().toString());
        return target;
    }

    @Override
    protected void $fromDomain(JSONObject source, SubredditSettings target) {
        target.setDomain(source.opt(DOMAIN));
    }

    @Override
    protected void $toDomain(SubredditSettings source, JSONObject target) {
        target.put(DOMAIN, source.getDomain());
    }

    @Override
    protected void $fromHeaderHoverText(JSONObject source, SubredditSettings target) {
        target.setDomain(source.opt(HEADERHOVERTEXT));
    }

    @Override
    protected void $toHeaderHoverText(SubredditSettings source, JSONObject target) {
        target.put(HEADERHOVERTEXT, source.getHeaderHoverText());
    }

    @Override
    protected void $fromSubmitLinkLabel(JSONObject source, SubredditSettings target) {
        target.setDomain(source.opt(SUBMITLINKLABEL));
    }

    @Override
    protected void $toSubmitLinkLabel(SubredditSettings source, JSONObject target) {
        target.put(SUBMITLINKLABEL, source.getSubmitLinkLabel());
    }

    @Override
    protected void $fromSubmitTextLabel(JSONObject source, SubredditSettings target) {
        target.setDomain(source.opt(SUBMITTEXTLABEL));
    }

    @Override
    protected void $toSubmitTextLabel(SubredditSettings source, JSONObject target) {
        target.put(SUBMITTEXTLABEL, source.getSubmitTextLabel());
    }

    @Override
    protected void $fromSuggestedCommentSort(JSONObject source, SubredditSettings target) {
        target.setDomain(source.opt(SUGGESTEDCOMMENTSORT));
    }

    @Override
    protected void $toSuggestedCommentSort(SubredditSettings source, JSONObject target) {
        target.put(SUGGESTEDCOMMENTSORT, source.getSuggestedCommentSort());
    }

    @Override
    protected void $fromWelcomeMessageText(JSONObject source, SubredditSettings target) {
        target.setDomain(source.opt(WELCOMEMESSAGETEXT));
    }

    @Override
    protected void $toWelcomeMessageText(SubredditSettings source, JSONObject target) {
        target.put(WELCOMEMESSAGETEXT, source.getWelcomeMessageText());
    }
}
