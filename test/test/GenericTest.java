package test;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import play.Configuration;

import java.io.File;
import java.io.IOException;

public class GenericTest {

    protected Configuration additionalConfigurations;

    @Before
    public void initialize(){
        Config additionalConfig = ConfigFactory.parseFile(new File("conf/test.conf"));
        additionalConfigurations = new Configuration(additionalConfig);

        try {
            FileUtils.deleteDirectory(new File("files/keys"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
