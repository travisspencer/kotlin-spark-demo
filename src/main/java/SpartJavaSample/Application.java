package SpartJavaSample;

import org.picocontainer.DefaultPicoContainer;
import spark.servlet.SparkApplication;

import javax.naming.ConfigurationException;

import static SpartJavaSample.Router.*;

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
        route("/authorize").to(AuthorizeController.class, usingContainer(appContainer), renderedWith("authorize.vm"));

        route("/login").to(LoginController.class, usingContainer(appContainer), renderedWith("login.vm"));

        route("/token").to(TokenController.class, usingContainer(appContainer));
    }
}
