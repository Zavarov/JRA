package vartas.reddit;

public enum TokenType {
    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token");

    private final String value;
    TokenType(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }
}
