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

package vartas.reddit.types;

import org.json.JSONObject;
import vartas.reddit.$factory.CommentFactory;
import vartas.reddit.$factory.LinkFactory;
import vartas.reddit.$factory.SubredditFactory;
import vartas.reddit.Client;
import vartas.reddit.Comment;
import vartas.reddit.Link;
import vartas.reddit.Subreddit;
import vartas.reddit.types.$factory.ListingFactory;
import vartas.reddit.types.$factory.ThingFactory;

import java.util.Objects;

public class Thing extends ThingTOP {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String KIND = "kind";
    private static final String DATA = "data";

    @Override
    public String getId() {
        return getSource().getString(ID);
    }

    @Override
    public String getName() {
        return getSource().getString(NAME);
    }

    @Override
    public String getKind() {
        return getSource().getString(KIND);
    }

    @Override
    public JSONObject getData() {
        return getSource().getJSONObject(DATA);
    }

    @Override
    public Thing getRealThis() {
        return this;
    }

    public enum Kind {
        Comment("t1"),
        Account("t2"),
        Link("t3"),
        Message("t4"),
        Subreddit("t5"),
        Award("t6"),
        Listing("Listing"),
        More("more");

        public final String name;

        Kind(String name){
            this.name = name;
        }

        public boolean matches(Thing thing){
            return matches(thing.getKind());
        }

        public boolean matches(String kind){
            return Objects.equals(name, kind);
        }
    }

    public static Thing from(JSONObject node){
        return ThingFactory.create(Thing::new, node);
    }

    public static Comment toComment(Thing thing){
        assert Thing.Kind.Comment.matches(thing);
        return CommentFactory.create(Comment::new, thing.getData());
    }

    public static Link toLink(Thing thing){
        assert Thing.Kind.Link.matches(thing);
        return LinkFactory.create(Link::new, thing.getData());
    }

    public static Subreddit toSubreddit(Thing thing, Client client){
        assert Kind.Subreddit.matches(thing);
        return SubredditFactory.create(() -> new Subreddit(client), thing.getData());
    }

    public static Listing toListing(Thing thing){
        assert Kind.Listing.matches(thing);
        return ListingFactory.create(Listing::new, thing.getData());
    }
}
