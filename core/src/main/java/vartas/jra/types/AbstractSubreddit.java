/*
 * Copyright (c) 2020 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package vartas.jra.types;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractSubreddit extends AbstractSubredditTOP{
    public static final String ACCOUNTS_ACTIVE = "accounts_active";
    public static final String BANNER_IMG = "banner_img";
    public static final String COMMENT_SCORE_HIDE_MINS = "comment_score_hide_mins";
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_HTML = "description_html";
    public static final String DISPLAY_NAME = "display_name";
    public static final String HEADER_IMG = "header_img";
    public static final String HEADER_SIZE = "header_size";
    public static final String HEADER_TITLE = "header_title";
    public static final String ICON_IMG = "icon_img";
    public static final String OVER_18 = "over18";
    public static final String PUBLIC_DESCRIPTION = "public_description";
    public static final String PUBLIC_TRAFFIC = "public_traffic";
    public static final String SUBSCRIBERS = "subscribers";
    public static final String SUBMISSION_TYPE = "submission_type";
    public static final String SUBMIT_LINK_LABEL = "submit_link_label";
    public static final String SUBMIT_TEXT_LABEL = "submit_text_label";
    public static final String SUBREDDIT_TYPE = "subreddit_type";
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String USER_IS_BANNED = "user_is_banned";
    public static final String USER_IS_CONTRIBUTOR = "user_is_contributor";
    public static final String USER_IS_MODERATOR = "user_is_moderator";
    public static final String USER_IS_SUBSCRIBER = "user_is_subscriber";

    @Override
    public AbstractSubreddit getRealThis() {
        return this;
    }

    //---------------------------------------------------------------------------------------------------------------
    //
    //  Accessing JSON attributes
    //
    //---------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<Integer> getAccountsActive() {
        if(getSource().has(ACCOUNTS_ACTIVE))
            return Optional.of(getSource().getInt(ACCOUNTS_ACTIVE));
        else
            return Optional.empty();
    }

    @Override
    public int getCommentScoreHideMinutes() {
        return getSource().getInt(COMMENT_SCORE_HIDE_MINS);
    }

    @Override
    public String getDescription() {
        return getSource().getString(DESCRIPTION);
    }

    @Override
    public String getDescriptionHtml() {
        return getSource().getString(DESCRIPTION_HTML);
    }

    @Override
    public String getDisplayName() {
        return getSource().getString(DISPLAY_NAME);
    }

    @Override
    public Optional<String> getBannerImage() {
        return Optional.ofNullable(getSource().optString(BANNER_IMG));
    }

    @Override
    public Optional<String> getHeaderImage() {
        return Optional.ofNullable(getSource().optString(HEADER_IMG, null));
    }

    @Override
    public Optional<List<Integer>> getHeaderSize() {
        JSONArray bounds = getSource().optJSONArray(HEADER_SIZE);
        if(bounds == null) {
            return Optional.empty();
        }else{
            List<Integer> data = new ArrayList<>(bounds.length());
            for(int i = 0 ; i < bounds.length() ; ++i)
                data.add(bounds.getInt(i));
            return Optional.of(Collections.unmodifiableList(data));
        }
    }

    @Override
    public Optional<String> getHeaderTitle() {
        return Optional.ofNullable(getSource().optString(HEADER_TITLE, null));
    }

    @Override
    public boolean isNsfw() {
        return getSource().getBoolean(OVER_18);
    }

    @Override
    public String getPublicDescription() {
        return getSource().getString(PUBLIC_DESCRIPTION);
    }

    @Override
    public boolean isPublicTraffic() {
        return getSource().getBoolean(PUBLIC_TRAFFIC);
    }

    @Override
    public long getSubscribers() {
        return getSource().getLong(SUBSCRIBERS);
    }

    @Override
    public String getSubmissionType() {
        return getSource().getString(SUBMISSION_TYPE);
    }

    @Override
    public String getSubmitLinkLabel() {
        return getSource().getString(SUBMIT_LINK_LABEL);
    }

    @Override
    public String getSubmitTextLabel() {
        return getSource().getString(SUBMIT_TEXT_LABEL);
    }

    @Override
    public String getSubredditType() {
        return getSource().getString(SUBREDDIT_TYPE);
    }

    @Override
    public String getTitle() {
        return getSource().getString(TITLE);
    }

    @Override
    public String getUrl() {
        return getSource().getString(URL);
    }

    @Override
    public boolean isUserBanned() {
        return getSource().getBoolean(USER_IS_BANNED);
    }

    @Override
    public boolean isUserContributor() {
        return getSource().getBoolean(USER_IS_CONTRIBUTOR);
    }

    @Override
    public boolean isUserModerator() {
        return getSource().getBoolean(USER_IS_MODERATOR);
    }

    @Override
    public boolean isUserSubscriber() {
        return getSource().getBoolean(USER_IS_SUBSCRIBER);
    }

    @Override
    public Optional<String> getIconImage(){
        return Optional.ofNullable(getSource().optString(ICON_IMG, null));
    }
}
