package vartas.reddit;

import java.util.Objects;

public enum Kind {
    Comment("t1"),
    Account("t2"),
    Link("t3"),
    Message("t4"),
    Subreddit("t5"),
    Award("t6");

    private final String name;

    Kind(String name){
        this.name = name;
    }

    public boolean matches(String kind){
        return Objects.equals(name, kind);
    }
}
