package vartas.reddit;

public enum Duration {
    PERMANENT("permanent"),
    TEMPORARY("temporary");

    private final String name;
    Duration(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
