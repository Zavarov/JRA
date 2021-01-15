package vartas.reddit.types;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class Rule extends RuleTOP{
    protected static final String CREATED_UTC = "created_utc";
    protected static final String DESCRIPTION = "description";
    protected static final String DESCRIPTION_HTML = "description_html";
    protected static final String KIND = "kind";
    protected static final String PRIORITY = "priority";
    protected static final String SHORT_NAME = "short_name";
    protected static final String VIOLATION_REASON = "violation_reason";

    @Override
    public OffsetDateTime getCreatedUtc() {
        double seconds = getSource().getDouble(CREATED_UTC);
        Instant instant = Instant.ofEpochSecond((long)seconds);

        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @Override
    public Optional<String> getDescription() {
        String description = getSource().getString(DESCRIPTION);
        return description.isEmpty() ? Optional.empty() : Optional.of(description);
    }

    @Override
    public Optional<String> getDescriptionHtml() {
        //Only exists if the description is not empty
        return Optional.ofNullable(getSource().optString(DESCRIPTION_HTML));
    }

    @Override
    public String getKind() {
        return getSource().getString(KIND);
    }

    @Override
    public int getPriority() {
        return getSource().getInt(PRIORITY);
    }

    @Override
    public String getShortName() {
        return getSource().getString(SHORT_NAME);
    }

    @Override
    public String getViolationReason() {
        return getSource().getString(VIOLATION_REASON);
    }

    @Override
    public Rule getRealThis() {
        return this;
    }
}
