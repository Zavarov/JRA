package proj.zav.jra.endpoints;

import proj.zav.jra.AbstractClient;
import proj.zav.jra.query.QueryGet;

import javax.annotation.Nonnull;

@Nonnull
public abstract class Captcha {
    /**
     * Specifies whether this application needs to solve a captcha before executing API requests.
     * @param client The client instance performing the API request.
     * @return {@code true}, if a captcha is required.
     * @deprecated With OAuth2, the need for captchas is no longer exist.
     * @see Endpoint#GET_NEEDS_CAPTCHA
     */
    @Nonnull
    @Deprecated
    public static QueryGet<Boolean> needsCaptcha(@Nonnull AbstractClient client) {
        return new QueryGet<>(
                Boolean::parseBoolean,
                client,
                Endpoint.GET_NEEDS_CAPTCHA
        );
    }
}
