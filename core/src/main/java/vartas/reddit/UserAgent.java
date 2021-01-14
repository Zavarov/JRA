package vartas.reddit;

public class UserAgent extends UserAgentTOP{
    @Override
    public UserAgent getRealThis() {
        return this;
    }

    @Override
    public String toString(){
        return String.format("%s:%s:%s (by /u/%s)", getPlatform(), getName(), getVersion(), getAuthor());
    }
}
