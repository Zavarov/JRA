package vartas.jra.types;

import java.util.Optional;

public interface Votable extends VotableTOP{
    String UPS = "ups";
    String DOWNS = "downs";
    String LIKES = "likes";

    @Override
    default int getUpvotes() {
        return getSource().getInt(UPS);
    }

    @Override
    default int getDownvotes() {
        return getSource().getInt(DOWNS);
    }

    @Override
    default Optional<Boolean> getLikes() {
        if(getSource().has(LIKES))
            return Optional.of(getSource().getBoolean(LIKES));
        else
            return Optional.empty();
    }

    @Override
    default Votable getRealThis(){
        return this;
    }
}
