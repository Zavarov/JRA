package proj.zav.jra;

import proj.zav.jra._factory.FrontPageFactory;
import proj.zav.jra.endpoints.Listings;
import proj.zav.jra.endpoints.Search;
import proj.zav.jra.models.Listing;
import proj.zav.jra.models.Submission;
import proj.zav.jra.models.Thing;
import proj.zav.jra.query.QueryGet;

import java.io.IOException;
import java.util.stream.Stream;

public class FrontPage extends FrontPageTOP{
    @Override
    public FrontPage getRealThis() {
        return this;
    }

    public static FrontPage from(Client client){
        return FrontPageFactory.create(FrontPage::new, client);
    }

    //----------------------------------------------------------------------------------------------------------------//
    //    Listings                                                                                                    //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Stream<Link> getBestLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getBestLinks(getClient(), Link::from);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Link> getControversialLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getControversialLinks(getClient(), Link::from);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    @SuppressWarnings("")
    public Stream<Link> getHotLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getHotLinks(getClient(), Link::from);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    @SuppressWarnings("unused")
    public Stream<Link> getNewLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getNewLinks(getClient(), Link::from);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Submission getRandomSubmission(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Submission> query = Listings.getRandomSubmission(getClient(), Link::from);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }

    @Override
    public Stream<Link> getRisingLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getRisingLinks(getClient(), Link::from);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }

    @Override
    public Stream<Link> getTopLinks(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Link>> query = Listings.getTopLinks(getClient(), Link::from);

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return Listing.Iterator.from(query).toStream();
    }
    //----------------------------------------------------------------------------------------------------------------//
    //    Search                                                                                                      //
    //----------------------------------------------------------------------------------------------------------------//
    @Override
    public Listing<Thing> getSearch(Parameter... params) throws InterruptedException, IOException {
        QueryGet<Listing<Thing>> query = Search.getSearch(getClient());

        for(Parameter param : params)
            query.setParameter(param.getKey(), param.getValue());

        return query.query();
    }
}
