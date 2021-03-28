package zav.jra.models._json;

import org.json.JSONArray;
import zav.jra.models.FakeAccount;
import zav.jra.models.Messaging;
import zav.jra.models._factory.MessagingFactory;

import java.util.List;

public class JSONMessaging extends JSONMessagingTOP{
    public static Messaging fromJson(String source){
        JSONArray response = new JSONArray(source);

        assert response.length() == 2;

        List<FakeAccount> blocked = JSONUserList.fromThing(response.getJSONObject(0)).getData();
        List<FakeAccount> trusted = JSONUserList.fromThing(response.getJSONObject(1)).getData();

        return MessagingFactory.create(blocked, trusted);
    }
}
