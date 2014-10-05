package SpartJavaSample;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.parameters.ComponentParameter;
import org.picocontainer.parameters.ConstantParameter;

import javax.naming.ConfigurationException;
import java.net.URI;
import java.net.URISyntaxException;

class ContainerComposer
{
    public static void composeApplication(DefaultPicoContainer appContainer)
    {
        //To change body of created methods use File | Settings | File Templates.
        appContainer.addComponent(AuthorizeController.class);
        appContainer.addComponent(TokenController.class);
    }

    public static void composeSession(MutablePicoContainer container)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void composeRequest(MutablePicoContainer container)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
