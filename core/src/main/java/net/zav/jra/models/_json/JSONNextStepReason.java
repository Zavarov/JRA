package net.zav.jra.models._json;

import net.zav.jra.models.NextStepReason;
import org.json.JSONArray;
import org.json.JSONObject;

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

        source.forEachNextStepReasons(nextStepReason -> node.put(toJson(nextStepReason, new JSONObject())));

        target.put(NEXTSTEPREASONS, node);
    }
}
