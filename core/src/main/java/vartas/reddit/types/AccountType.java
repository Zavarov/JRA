package vartas.reddit.types;

import java.util.Optional;

public class AccountType extends AccountTypeTOP{
    public static final String COMMENT_KARMA = "comment_karma";
    public static final String HAS_MAIL = "has_mail";
    public static final String HAS_MOD_MAIL = "has_mod_mail";
    public static final String HAS_VERIFIED_MAIL = "has_verified_email";
    public static final String HAS_SUBSCRIBED = "has_subscribed";
    public static final String ID = "id";
    public static final String INBOX_COUNT = "inbox_count";
    public static final String IS_FRIEND = "is_friend";
    public static final String IS_GOLD = "is_gold";
    public static final String IS_MOD = "is_mod";
    public static final String LINK_KARMA = "link_karma";
    public static final String MODHASH = "modhash";
    public static final String NAME = "name";
    public static final String OVER_18 = "over_18";

    @Override
    public AccountType getRealThis() {
        return this;
    }

    //---------------------------------------------------------------------------------------------------------------
    //
    //  Accessing JSON attributes
    //
    //---------------------------------------------------------------------------------------------------------------

    @Override
    public int getCommentKarma() {
        return getSource().getInt(COMMENT_KARMA);
    }

    @Override
    public Optional<Boolean> hasMail() {
        if(getSource().has(HAS_MAIL))
            return Optional.of(getSource().getBoolean(HAS_MAIL));
        else
            return Optional.empty();
    }

    @Override
    public Optional<Boolean> hasModMail() {
        if(getSource().has(HAS_MOD_MAIL))
            return Optional.of(getSource().getBoolean(HAS_MOD_MAIL));
        else
            return Optional.empty();
    }

    @Override
    public boolean hasVerifiedEmail() {
        return getSource().getBoolean(HAS_VERIFIED_MAIL);
    }

    @Override
    public boolean hasSubscribed() {
        return getSource().getBoolean(HAS_SUBSCRIBED);
    }

    @Override
    public String getId() {
        return getSource().getString(ID);
    }

    @Override
    public Optional<Integer> getInboxCount() {
        if(getSource().has(INBOX_COUNT))
            return Optional.of(getSource().getInt(INBOX_COUNT));
        else
            return Optional.empty();
    }

    @Override
    public boolean isFriend() {
        return getSource().getBoolean(IS_FRIEND);
    }

    @Override
    public boolean isGoldMember() {
        return getSource().getBoolean(IS_GOLD);
    }

    @Override
    public boolean isModerator() {
        return getSource().getBoolean(IS_MOD);
    }

    @Override
    public int getLinkKarma() {
        return getSource().getInt(LINK_KARMA);
    }

    @Override
    public Optional<String> getModHash() {
        return Optional.ofNullable(getSource().optString(MODHASH, null));
    }

    @Override
    public String getName() {
        return getSource().getString(NAME);
    }

    @Override
    public boolean isNsfw() {
        return getSource().getBoolean(OVER_18);
    }
}
