package SpartJavaSample;

import org.picocontainer.DefaultPicoContainer;
import spark.servlet.SparkApplication;

import javax.naming.ConfigurationException;

import static SpartJavaSample.Tinder.*;

public class Application implements SparkApplication
{
    private final DefaultPicoContainer appContainer;

    public Application() throws ConfigurationException
    {
        appContainer = new DefaultPicoContainer();

        ContainerComposer.composeApplication(appContainer);
    }

    @Override
    public void init()
    {
        route("/token").havingMethods("get", "post")
            .to(TokenController.class, usingContainer(appContainer), renderedWith("token.vm"));

        route("/authorize").havingMethod("get")
            .to(AuthorizeController.class, usingContainer(appContainer));
    }
}
