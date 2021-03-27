package proj.zav.jra.models._json;

import org.json.JSONArray;
import org.json.JSONObject;
import proj.zav.jra.models.NextStepReason;
import proj.zav.jra.models.Rule;
import proj.zav.jra.models.Rules;

public class JSONRules extends JSONRulesTOP{
    @Override
    protected void $fromSiteRulesFlow(JSONObject source, Rules target){
        JSONArray node = source.optJSONArray(SITERULESFLOW);

        if(node != null)
            for(int i = 0 ; i < node.length() ; ++i)
                target.addSiteRulesFlow(JSONNextStepReason.fromJson(new NextStepReason(), node.getJSONObject(i)));
    }
    @Override
    protected void $toSiteRulesFlow(Rules source, JSONObject target){
        JSONArray node = new JSONArray();

        source.forEachSiteRulesFlow(siteRulesFlow -> node.put(JSONNextStepReason.toJson(siteRulesFlow, new JSONObject())));

        target.put(SITERULESFLOW, node);
    }
    @Override
    protected void $fromSiteRules(JSONObject source, Rules target){
        JSONArray node = source.optJSONArray(SITERULES);

        if(node != null)
            for(int i = 0 ; i < node.length() ; ++i)
                target.addSiteRules(node.getString(i));
    }
    @Override
    protected void $toSiteRules(Rules source, JSONObject target){
        JSONArray node = new JSONArray();

        source.forEachSiteRules(node::put);

        target.put(SITERULES, node);
    }

    @Override
    protected void $fromRules(JSONObject source, Rules target){
        JSONArray node = source.optJSONArray(RULES);

        if(node != null)
            for(int i = 0 ; i < node.length() ; ++i)
                target.addRules(JSONRule.fromJson(new Rule(), node.getJSONObject(i)));
    }

    @Override
    protected void $toRules(Rules source, JSONObject target){
        JSONArray node = new JSONArray();

        source.forEachRules(rule -> node.put(JSONRule.toJson(rule, new JSONObject())));

        target.put(RULES, node);
    }
}