package test;

import org.junit.Before;
import play.test.FakeApplication;

import java.util.Map;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class GenericFakeAppTest extends GenericTest {

    protected FakeApplication fakeApp;

    @Before
    @Override
    public void initialize() {
        super.initialize();
        Map<String, Object> testConf = additionalConfigurations.asMap();
        fakeApp = fakeApplication(testConf);
    }
}
