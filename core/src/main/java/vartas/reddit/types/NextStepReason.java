package vartas.reddit.types;

import org.json.JSONArray;
import vartas.reddit.types.$factory.NextStepReasonFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NextStepReason extends NextStepReasonTOP{
    protected static final String REASON_TEXT_TO_SHOW = "reasonTextToShow";
    protected static final String REASON_TEXT = "reasonText";
    protected static final String NEXT_STEP_REASONS = "nextStepReasons";
    protected static final String NEXT_STEP_HEADER = "nextStepHeader";

    @Override
    public String getReasonTextToShow() {
        return getSource().getString(REASON_TEXT_TO_SHOW);
    }

    @Override
    public String getReasonText() {
        return getSource().getString(REASON_TEXT);
    }

    @Override
    public List<NextStepReason> getNextStepReasons() {
        JSONArray node = getSource().optJSONArray(NEXT_STEP_REASONS);

        if(node != null) {
            List<NextStepReason> data = new ArrayList<>(node.length());

            for(int i = 0 ; i < node.length() ; ++i)
                data.add(NextStepReasonFactory.create(NextStepReason::new, node.getJSONObject(i)));

            return Collections.unmodifiableList(data);
        }else{
            return Collections.emptyList();
        }
    }
    @Override
    public Optional<String> getNextStepHeader() {
        return Optional.ofNullable(getSource().optString(NEXT_STEP_HEADER, null));
    }

    @Override
    public NextStepReason getRealThis() {
        return this;
    }
}
