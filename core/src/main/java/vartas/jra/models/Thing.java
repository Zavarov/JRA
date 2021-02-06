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

package vartas.jra.models;

import org.json.JSONObject;
import vartas.jra.*;
import vartas.jra._json.JSONAccount;
import vartas.jra._json.JSONComment;
import vartas.jra._json.JSONLink;
import vartas.jra._json.JSONSubreddit;
import vartas.jra.models._json.*;

import java.util.Objects;

public class Thing extends ThingTOP {
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

        private final String name;

        Kind(String name){
            this.name = name;
        }

        public boolean matches(Thing thing){
            return matches(thing.getKind());
        }

        public boolean matches(String kind){
            return Objects.equals(name, kind);
        }

        public String toString(){
            return name;
        }
    }

    public static Thing from(String source){
        return from(new JSONObject(source));
    }

    public static Thing from(JSONObject node){
        return JSONThing.fromJson(new Thing(), node);
    }

    public Account toAccount(Client client){
        assert Kind.Account.matches(this);
        return JSONAccount.fromJson(new Account(client), getData().toString());
    }

    public KarmaList toKarmaList(){
        assert Kind.KarmaList.matches(this);

        //While TrophyList and UserList return a JSON Object, KarmaList returns a JSONArray.
        //We have to wrap this array in an object, to make it compatible with the rest of the program.
        JSONObject root = new JSONObject();
        root.put(JSONKarmaList.KEY, getData());
        return JSONKarmaList.fromJson(new KarmaList(), root);
    }

    public TrophyList toTrophyList(){
        assert Kind.TrophyList.matches(this);
        return JSONTrophyList.fromJson(new TrophyList(), getData().toString());
    }

    public Trophy toTrophy(){
        assert Kind.Award.matches(this);
        return JSONTrophy.fromJson(new Trophy(), getData().toString());
    }

    public UserList toUserList(Client client){
        assert Kind.UserList.matches(this);
        return JSONUserList.fromJson(client, getData().toString());
    }

    public Comment toComment(){
        assert Thing.Kind.Comment.matches(this);
        return JSONComment.fromJson(new Comment(), getData().toString());
    }

    public Link toLink(){
        assert Thing.Kind.Link.matches(this);
        System.out.println(new JSONObject(getData().toString()).toString(2));
        return JSONLink.fromJson(new Link(), getData().toString());
    }

    public Subreddit toSubreddit(Client client){
        assert Kind.Subreddit.matches(this);
        return JSONSubreddit.fromJson(new Subreddit(client), getData().toString());
    }

    public Listing toListing(){
        assert Kind.Listing.matches(this);
        return JSONListing.fromJson(new Listing(), getData().toString());
    }
}
