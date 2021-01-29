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

import org.json.JSONObject;
import vartas.jra.$factory.CommentFactory;
import vartas.jra.$factory.LinkFactory;
import vartas.jra.$factory.SubredditFactory;
import vartas.jra.$json.JSONAccount;
import vartas.jra.*;
import vartas.jra.types.$factory.ListingFactory;
import vartas.jra.types.$factory.ThingFactory;
import vartas.jra.types.$json.JSONKarmaList;
import vartas.jra.types.$json.JSONTrophy;
import vartas.jra.types.$json.JSONTrophyList;
import vartas.jra.types.$json.JSONUserList;

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
    public Object getData() {
        return getSource().get(DATA);
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
        More("more"),
        UserList("UserList"),
        TrophyList("TrophyList"),
        KarmaList("KarmaList");

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

    public static Thing from(String source){
        return from(new JSONObject(source));
    }

    public static Thing from(JSONObject node){
        return ThingFactory.create(Thing::new, node);
    }

    public static JSONObject from(JSONObject source, Kind kind){
        JSONObject node = new JSONObject();
        node.put(DATA, source);
        node.put(KIND, kind.name);
        return node;
    }

    public Account toAccount(Client client){
        assert Kind.Account.matches(this);
        return JSONAccount.fromJson(new Account(client, getSource()), getSource().getJSONObject(DATA));
    }

    public KarmaList toKarmaList(){
        assert Kind.KarmaList.matches(this);

        //While TrophyList and UserList return a JSON Object, KarmaList returns a JSONArray.
        //We have to wrap this array in an object, to make it compatible with the rest of the program.
        JSONObject root = new JSONObject();
        root.put(JSONKarmaList.KEY, getSource().getJSONArray(DATA));
        return JSONKarmaList.fromJson(new KarmaList(), root);
    }

    public TrophyList toTrophyList(){
        assert Kind.TrophyList.matches(this);
        return JSONTrophyList.fromJson(new TrophyList(), getSource().getJSONObject(DATA));
    }

    public Trophy toTrophy(){
        assert Kind.Award.matches(this);
        return JSONTrophy.fromJson(new Trophy(), getSource().getJSONObject(DATA));
    }

    public UserList toUserList(){
        assert Kind.UserList.matches(this);
        return JSONUserList.fromJson(new UserList(), getSource().getJSONObject(DATA));
    }

    public Comment toComment(){
        assert Thing.Kind.Comment.matches(this);
        return CommentFactory.create(Comment::new, getSource().getJSONObject(DATA));
    }

    public Link toLink(){
        assert Thing.Kind.Link.matches(this);
        return LinkFactory.create(Link::new, getSource().getJSONObject(DATA));
    }

    public Subreddit toSubreddit(Client client){
        assert Kind.Subreddit.matches(this);
        return SubredditFactory.create(() -> new Subreddit(client), getSource().getJSONObject(DATA));
    }

    public Listing toListing(){
        assert Kind.Listing.matches(this);
        return ListingFactory.create(Listing::new, getSource().getJSONObject(DATA));
    }
}
