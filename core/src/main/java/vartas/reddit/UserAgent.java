package vartas.reddit;

/**
 * The user agent is used by Reddit to identify this application consisting of the target platform,
 * a unique application identifier, a version string, and the username as contact information.
 */
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
