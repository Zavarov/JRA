/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vartas.reddit;

/**
 * This interface specifies all the required information the credential file
 * for this client has to contain.
 * @author u/Zavarov
 */
public interface RedditCredentials{
    /**
     * @return the id used for accessing the Reddit API. 
     */
    public String getId();
    /**
     * @return the token used for accessing the Reddit API. 
     */
    public String getSecret();
    /**
     * @return a descriptor of this bot. 
     */
    public String getAppid();
    /**
     * @return the user who created the Reddit instance.
     */
    public String getUser();
    /**
     * @return a redirect link, in case Reddit returned an error. 
     */
    public String getRedirect();
    /**
     * @return the permissions the bot is allowed to use. 
     */
    public String getScope();
    /**
     * @return the type of hardware the program is running on. 
     */
    public String getPlatform();
    /**
     * @return the version of the program. 
     */
    public String getVersion();
}
