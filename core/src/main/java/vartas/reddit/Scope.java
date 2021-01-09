package vartas.reddit;

import java.util.Locale;

public enum Scope {
    IDENTITY,
    EDIT,
    FLAIR,
    HISTORY,
    MODCONFIG,
    MODFLAIR,
    MODLOG,
    MODPOST,
    MODWIKI,
    MYSUBREDDITS,
    PRIVATEMESSAGES,
    READ,
    REPORT,
    SAVE,
    SUBMIT,
    SUBSCRIBE,
    VOTE,
    WIKIEDIT,
    WIKIREAD;

    @Override
    public String toString(){
        return name().toLowerCase(Locale.ENGLISH);
    }
}
