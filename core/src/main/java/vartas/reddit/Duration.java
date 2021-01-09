package vartas.reddit;

import java.util.Locale;

public enum Duration {
    PERMANENT,
    TEMPORARY;

    @Override
    public String toString(){
        return name().toLowerCase(Locale.ENGLISH);
    }
}
