package vartas.jra;

import vartas.jra.endpoints.Users;
import vartas.jra.models.FakeAccount;
import vartas.jra.models.Listing;
import vartas.jra.models.Thing;
import vartas.jra.models.Trophy;
import vartas.jra.query.QueryDelete;
import vartas.jra.query.QueryGet;
import vartas.jra.query.QueryPost;
import vartas.jra.query.QueryPut;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class Account extends AccountTOP{
    @Override
    public Account getRealThis() {
        return this;
    }

    public static Account from(Client client){
        Account account = new Account();
        account.setClient(client);
        return account;
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Users                                                                                                      //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public String postBlock(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Users.postBlockUser(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postFriend(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Users.postFriend(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postReport(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Users.postReportUser(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postSetPermission(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Users.postSetPermission(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public String postUnfriend(Parameter... params) throws InterruptedException, IOException {
        QueryPost<String> query = Users.postUnfriend(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public void deleteFriends(Parameter... params) throws InterruptedException, IOException {
        QueryDelete<Void> query = Users.deleteMeFriends(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        query.query();
    }

    @Override
    public FakeAccount getFriends(Parameter... params) throws InterruptedException, IOException {
        QueryGet<FakeAccount> query = Users.getMeFriends(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public FakeAccount putFriends(Parameter... params) throws InterruptedException, IOException {
        QueryPut<FakeAccount> query = Users.putMeFriends(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<Trophy> getTrophies(Parameter... params) throws InterruptedException, IOException {
        QueryGet<List<Trophy>> query = Users.getTrophies(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query().stream();
    }

    @Override
    public Stream<Comment> getComments(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Comment>> query = Users.getComments(getClient(), Comment::from, getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Listing<Thing> getDownvoted(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Thing>> query = Users.getDownvoted(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();

    }

    @Override
    public Listing<Thing> getGilded(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Thing>> query = Users.getGilded(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();

    }

    @Override
    public Listing<Thing> getHidden(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Thing>> query = Users.getHidden(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Listing<Thing> getOverview(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Thing>> query = Users.getOverview(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();

    }

    @Override
    public Listing<Thing> getSaved(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Thing>> query = Users.getSaved(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<Link> getSubmitted(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Users.getSubmitted(getClient(), Link::from, getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Listing<Thing> getUpvoted(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Thing>> query = Users.getUpvoted(getClient(), getName());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }
}
