package helper;

import org.junit.After;
import org.junit.Before;
import play.test.Helpers;
import play.test.TestServer;
import play.test.WithApplication;

public class WithTestServer extends WithApplication {

    private TestServer server;

    @Before
    public void startPlay() {
        this.app = this.provideApplication();
        server = Helpers.testServer(9000, this.app);
        server.start();
    }

    @After
    public void stopPlay() {
        if(this.server != null) {
            server.stop();
            this.server = null;
        }

    }
}
