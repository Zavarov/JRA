package vartas.reddit;

public enum GrantType {
    USERLESS("https://oauth.reddit.com/grants/installed_client"),
    CLIENT("client_credentials"),
    REFRESH("refresh_token");

    private final String value;
    GrantType(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }
}
