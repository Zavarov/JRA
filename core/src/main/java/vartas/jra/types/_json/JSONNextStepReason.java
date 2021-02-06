package vartas.jra.types._json;

import org.json.JSONArray;
import org.json.JSONObject;
import vartas.jra.types.NextStepReason;

public class JSONNextStepReason extends JSONNextStepReasonTOP{
    @Override
    protected void $fromNextStepReasons(JSONObject source, NextStepReason target){
        JSONArray node = source.optJSONArray(NEXTSTEPREASONS);

        if(node != null)
            for(int i = 0 ; i < node.length() ; ++i)
                target.addNextStepReasons(JSONNextStepReason.fromJson(new NextStepReason(), node.getJSONObject(i)));
    }

    @Override
    protected void $toNextStepReasons(NextStepReason source, JSONObject target){
        JSONArray node = new JSONArray();

        source.forEachNextStepReasons(nextStepReason -> node.put(JSONNextStepReason.toJson(nextStepReason, new JSONObject())));

        target.put(NEXTSTEPREASONS, node);
    }
}
