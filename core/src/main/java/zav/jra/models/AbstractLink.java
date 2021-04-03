package zav.jra.models;

import javax.annotation.Nonnull;

public class AbstractLink extends AbstractLinkTOP{
    @Nonnull
    private static final String QUALIFIED_PERMALINK = "https://www.reddit.com%s";

    @Nonnull
    private static final String SHORT_LINK = "https://redd.it/%s";

    public static String getPermalink(AbstractLink source){
        return String.format(QUALIFIED_PERMALINK, source.getPermalink());
    }

    public static String getQualifiedTitle(AbstractLink source){
        StringBuilder titleBuilder = new StringBuilder();

        source.ifPresentLinkFlairText(flair  -> titleBuilder.append("[").append(flair).append("] "));
        titleBuilder.append(source.getTitle());
        if(source.getSpoiler()) titleBuilder.append(" [Spoiler]");
        if(source.getOver18()) titleBuilder.append(" [NSFW]");

        return titleBuilder.toString();
    }

    public static String getShortLink(AbstractLink source){
        return String.format(SHORT_LINK, source.getId());
    }

    @Override
    public AbstractLink getRealThis() {
        return this;
    }
}
