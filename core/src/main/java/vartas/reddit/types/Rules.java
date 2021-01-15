package vartas.reddit.types;

import org.json.JSONArray;
import vartas.reddit.types.$factory.NextStepReasonFactory;
import vartas.reddit.types.$factory.RuleFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rules extends RulesTOP{
    protected static final String RULES = "rules";
    protected static final String SITE_RULES = "site_rules";
    protected static final String SITE_RULES_FLOW = "site_rules_flow";

    @Override
    public List<Rule> getRules() {
        JSONArray node = getSource().getJSONArray(RULES);
        List<Rule> data = new ArrayList<>(node.length());

        for(int i = 0 ; i < node.length() ; ++i)
            data.add(RuleFactory.create(Rule::new, node.getJSONObject(i)));

        return Collections.unmodifiableList(data);
    }

    @Override
    public List<String> getSiteRules() {
        JSONArray node = getSource().getJSONArray(SITE_RULES);
        List<String> data = new ArrayList<>(node.length());

        for(int i = 0 ; i < node.length() ; ++i)
            data.add(node.getString(i));

        return Collections.unmodifiableList(data);
    }

    @Override
    public List<NextStepReason> getSiteRulesFlow() {
        JSONArray node = getSource().getJSONArray(SITE_RULES_FLOW);
        List<NextStepReason> data = new ArrayList<>(node.length());

        for(int i = 0 ; i < node.length() ; ++i)
            data.add(NextStepReasonFactory.create(NextStepReason::new, node.getJSONObject(i)));

        return Collections.unmodifiableList(data);
    }

    @Override
    public Rules getRealThis() {
        return this;
    }
}
