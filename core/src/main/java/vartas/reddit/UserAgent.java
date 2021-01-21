package vartas.reddit;

import javax.annotation.Nonnull;

/**
 * The user agent is used by Reddit to identify this application consisting of the target platform, a unique application
 * identifier, a version string, and the username as contact information, in the following format:<p>
 * {@code <platform>:<app ID>:<version string> (by /u/<reddit username>)}
 */
@Nonnull
public class UserAgent extends UserAgentTOP{
    /**
     * Returns a reference to the instance of this class.
     * @return {@code this}.
     */
    @Override
    @Nonnull
    public UserAgent getRealThis() {
        return this;
    }

    /**
     * The user agent string is of the form:<p>
     * {@code <platform>:<app ID>:<version string> (by /u/<reddit username>)}
     * @return The user agent string.
     */
    @Override
    @Nonnull
    public String toString(){
        return String.format("%s:%s:%s (by /u/%s)", getPlatform(), getName(), getVersion(), getAuthor());
    }
}
