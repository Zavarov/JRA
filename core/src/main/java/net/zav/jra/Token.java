package net.zav.jra;

import okhttp3.Request;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

/**
 * This class contains the credentials received after authorization. Those credentials have to be attached
 * to every {@link Request} for authentication.
 */
public class Token extends TokenTOP{
    /**
     * The token only stores the duration, in which the new access token is valid.<p>
     * Upon receiving a new token, this variable will be updated with the new expiration date.
     */
    private LocalDateTime expirationDate = LocalDateTime.now();

    /**
     * Sets the duration in second, during which the access token is valid.<p>
     * In addition, this method also updates the expiration date with the new lifetime.
     * @param lifetime The lifetime of the access token in seconds.
     */
    @Override
    public void setLifetime(int lifetime){
        super.setLifetime(lifetime);
        this.expirationDate = LocalDateTime.now().plusSeconds(lifetime);
    }

    /**
     * Checks if the access token has expired. In order to allow a little bit of tolerance, the access token
     * will be treated as expired, the moment it is less than one minute valid. This minimizes the risk of the token
     * expiring between calling this method and sending the request.
     * @return {@code true}, if the access token is no longer valid.
     */
    @Override
    public boolean isExpired() {
        //In order to prevent using an expired token, a fresh one
        //has to be requested a minute before the current one expires
        return expirationDate.minusMinutes(1).isBefore(LocalDateTime.now());
    }

    /**
     * Returns a reference to the instance of this class.
     * @return {@code this}.
     */
    @Override
    @Nonnull
    public Token getRealThis() {
        return this;
    }
}
