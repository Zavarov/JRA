package vartas.jra;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.NotFoundException;

import java.io.IOException;

public class SubredditTest extends AbstractTest{
    static Client client;
    static Subreddit subreddit;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(SubredditTest.class.getSimpleName());
        client.login(Client.Duration.TEMPORARY);
        subreddit = client.getSubreddit("RedditDev").query();
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Listings                                                                                                    //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//


    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetComments(String id) throws InterruptedException, IOException, HttpException {
        subreddit.getComments(id).query();
    }

    @Test
    public void testGetControversialLinks() throws InterruptedException, IOException, HttpException {
        subreddit.getControversialLinks().query();
    }

    @Test
    public void testGetHotLinks() throws InterruptedException, IOException, HttpException {
        subreddit.getHotLinks().query();
    }

    @Test
    public void testGetNewLinks() throws InterruptedException, IOException, HttpException {
        subreddit.getNewLinks().query();
    }

    @Test
    public void testGetRandomSubmission() throws InterruptedException, IOException, HttpException {
        subreddit.getRandomSubmission().query();
    }

    @Test
    public void testGetRisingLinks() throws InterruptedException, IOException, HttpException {
        subreddit.getRisingLinks().query();
    }

    @Test
    public void testGetTopLinks() throws InterruptedException, IOException, HttpException {
        subreddit.getTopLinks().query();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Search                                                                                                      //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    public void testGetSearch() throws InterruptedException, IOException, HttpException {
        subreddit.getSearch().setParameter("q", "penguins").query();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Subreddits                                                                                                  //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    public void testGetModerators() throws InterruptedException, IOException, HttpException {
        subreddit.getModerators().query();
    }

    @Test
    public void testGetPostRequirements() throws InterruptedException, IOException, HttpException {
        subreddit.getPostRequirements().query();
    }

    @Test
    public void testGetSubmitText() throws InterruptedException, IOException, HttpException {
        subreddit.getSubmitText().query();
    }

    @Test
    public void testGetRules() throws IOException, HttpException, InterruptedException {
        subreddit.getRules().query();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetSidebar() throws InterruptedException, IOException, HttpException {
        subreddit.getSidebar().query();
    }

    @Test
    public void testGetSticky() throws InterruptedException, IOException, HttpException {
        try {
            subreddit.getSticky().setParameter("num", 1).query();
        }catch(NotFoundException ignored){}
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class Moderator{
        Client client;
        Subreddit subreddit;

        @BeforeAll
        public void setUpAll() throws IOException, HttpException, InterruptedException {
            client = getScript(SubredditTest.class.getSimpleName());
            client.login(Client.Duration.TEMPORARY);
            subreddit = client.getSubreddit("Zavarov").query();
        }

        @AfterAll
        public void tearDownAll() throws InterruptedException, IOException, HttpException {
            client.logout();
        }

        @Test
        public void testGetBanned() throws InterruptedException, IOException, HttpException {
            subreddit.getBanned().query();
        }

        @Test
        public void testGetContributors() throws InterruptedException, IOException, HttpException {
            subreddit.getContributors().query();
        }

        @Test
        public void testGetMuted() throws InterruptedException, IOException, HttpException {
            subreddit.getMuted().query();
        }

        @Test
        public void testGetWikibanned() throws InterruptedException, IOException, HttpException {
            subreddit.getWikibanned().query();
        }

        @Test
        public void testGetWikicontributors() throws InterruptedException, IOException, HttpException {
            subreddit.getWikicontributors().query();
        }

        @Test
        public void testPostDeleteBanner() throws InterruptedException, IOException, HttpException {
            subreddit.postDeleteBanner().query();
        }

        @Test
        public void testPostDeleteHeader() throws InterruptedException, IOException, HttpException {
            subreddit.postDeleteHeader().query();
        }

        @Test
        public void testPostDeleteIcon() throws InterruptedException, IOException, HttpException {
            subreddit.postDeleteIcon().query();
        }

        //@Test
        public void testPostDeleteImage() throws InterruptedException, IOException, HttpException {
            //Testing would require an image to delete
            subreddit.postDeleteImage().query();
        }

        @Test
        public void testGetEdit() throws InterruptedException, IOException, HttpException {
            subreddit.getEdit().query();
        }

        @Test
        public void testGetTraffic() throws InterruptedException, IOException, HttpException {
            subreddit.getTraffic().query();
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        //                                                                                                            //
        //    Users                                                                                                   //
        //                                                                                                            //
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

        //@Test
        public void testPostFriend() throws InterruptedException, IOException, HttpException {
            subreddit.postFriend().query();
        }

        //@Test
        public void testPostSetPermission() throws InterruptedException, IOException, HttpException {
            subreddit.postSetPermission().query();
        }

        //@Test
        public void testPostUnfriend() throws InterruptedException, IOException, HttpException {
            subreddit.postUnfriend().query();
        }
    }
}
