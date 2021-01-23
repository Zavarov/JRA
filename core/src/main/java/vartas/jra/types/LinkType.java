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

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class LinkType extends LinkTypeTOP{
    public static final String AUTHOR = "author";
    public static final String AUTHOR_FLAIR_CSS_CLASS = "author_flair_css_class";
    public static final String AUTHOR_FLAIR_TEXT = "author_flair_text";
    public static final String CLICKED = "clicked";
    public static final String DOMAIN = "domain";
    public static final String HIDDEN = "hidden";
    public static final String IS_SELF = "is_self";
    public static final String LINK_FLAIR_CSS_CLASS = "link_flair_css_class";
    public static final String LINK_FLAIR_TEXT = "link_flair_text";
    public static final String LOCKED = "locked";
    public static final String MEDIA = "media";
    public static final String MEDIA_EMBED = "media_embed";
    public static final String NUM_COMMENTS = "num_comments";
    public static final String OVER_18 = "over_18";
    public static final String PERMALINK = "permalink";
    public static final String SAVED = "saved";
    public static final String SCORE = "score";
    public static final String SELFTEXT = "selftext";
    public static final String SELFTEXT_HTML = "selftext_html";
    public static final String SUBREDDIT = "subreddit";
    public static final String SUBREDDIT_ID = "subreddit_id";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String EDITED = "edited";
    public static final String DISTINGUISHED = "distinguished";
    public static final String STICKIED = "stickied";
    public static final String SPOILER = "spoiler";

    @Override
    public LinkType getRealThis() {
        return this;
    }

    //---------------------------------------------------------------------------------------------------------------
    //
    //  Accessing JSON attributes
    //
    //---------------------------------------------------------------------------------------------------------------

    @Override
    public Optional<String> getAuthor() {
        return Optional.ofNullable(getSource().optString(AUTHOR, null));
    }

    @Override
    public Optional<String> getAuthorFlairCssClass() {
        String value = getSource().optString(AUTHOR_FLAIR_CSS_CLASS, "");
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    @Override
    public Optional<String> getAuthorFlairText() {
        String value = getSource().optString(AUTHOR_FLAIR_TEXT, "");
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    @Override
    public boolean isClicked() {
        return getSource().getBoolean(CLICKED);
    }

    @Override
    public String getDomain() {
        return getSource().getString(DOMAIN);
    }

    @Override
    public boolean isHidden() {
        return getSource().getBoolean(HIDDEN);
    }

    @Override
    public boolean isSelf() {
        return getSource().getBoolean(IS_SELF);
    }

    @Override
    public Optional<String> getLinkFlairCssClass() {
        String value = getSource().optString(LINK_FLAIR_CSS_CLASS, "");
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    @Override
    public Optional<String> getLinkFlairText() {
        String value = getSource().optString(LINK_FLAIR_TEXT, "");
        return value.isBlank() ? Optional.empty() : Optional.of(value);
    }

    @Override
    public boolean isLocked() {
        return getSource().getBoolean(LOCKED);
    }

    @Override
    public Object getMedia() {
        return getSource().get(MEDIA);
    }

    @Override
    public Object getMediaEmbed() {
        return getSource().get(MEDIA_EMBED);
    }

    @Override
    public int getNumberOfComments() {
        return getSource().getInt(NUM_COMMENTS);
    }

    @Override
    public boolean isNsfw() {
        return getSource().getBoolean(OVER_18);
    }

    @Override
    public String getPermalink() {
        return getSource().getString(PERMALINK);
    }

    @Override
    public boolean isSaved() {
        return getSource().getBoolean(SAVED);
    }

    @Override
    public int getScore() {
        return getSource().getInt(SCORE);
    }

    @Override
    public Optional<String> getSelftext() {
        String selftext = getSource().optString(SELFTEXT, "");
        return selftext.isBlank() ? Optional.empty() : Optional.of(selftext);
    }

    @Override
    public Optional<String> getSelftextHtml() {
        String selftext = getSource().optString(SELFTEXT_HTML, "");
        return selftext.isBlank() ? Optional.empty() : Optional.of(selftext);
    }

    @Override
    public String getSubreddit() {
        return getSource().getString(SUBREDDIT);
    }

    @Override
    public String getSubredditId() {
        return getSource().getString(SUBREDDIT_ID);
    }

    @Override
    public Optional<String> getThumbnail() {
        String value = getSource().optString(THUMBNAIL, "");
        return value.isBlank() ? Optional.empty() : Optional.of(value);
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
    public Optional<OffsetDateTime> getEdited() {
        //Returns false if not edited, occasionally true for very old comments
        //Otherwise the edit-date in UTC
        if(getSource().get(EDITED) instanceof Boolean)
            return Optional.empty();
        else
            return Optional.of(getSource().getLong(EDITED))
                    .map(Instant::ofEpochSecond)
                    .map(instant -> OffsetDateTime.ofInstant(instant, ZoneOffset.UTC));
    }

    @Override
    public Optional<String> getDistinguished() {
        return Optional.ofNullable(getSource().optString(DISTINGUISHED, null));
    }

    @Override
    public boolean isStickied() {
        return getSource().getBoolean(STICKIED);
    }

    @Override
    public boolean isSpoiler() {
        return getSource().getBoolean(SPOILER);
    }
}
