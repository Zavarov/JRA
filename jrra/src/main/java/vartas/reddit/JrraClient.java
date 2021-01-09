package vartas.reddit;

import java.util.UUID;

public class JrraClient extends Client {

    private static final String AUTHORIZATION_URL = "";
    private static final String CLIENT_ID = "client_id";
    private static final String RESPONSE_TYPE = "response_type";
    private static final String STATE = "client_id";
    private static final String REDIRECT_URI = "client_id";
    private static final String DURATION = "client_id";
    private static final String SCOPE = "client_id";

        Credentials.userless();
        Credentials.script();
        Credentials.installedApp();
        Credentials.userlessApp();
        Credentials.webapp();

    public Jrra(String userName, String version, String clientId, String secret){
        UserAgent userAgent = new UserAgent("bot", getClass().getPackage().getName(), version, userName);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new MalformedJsonInterceptor()).build();
        OkHttpNetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent, client);

        jrawClient = OAuthHelper.automatic(adapter, Credentials.userless(clientId, secret, UUID.randomUUID()));
        jrawClient.setLogHttp(false);
        jrawClient.setAutoRenew(true);
    }
}
