package SpartJavaSample;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

@SuppressWarnings("unused")
class ContainerComposer
{
    public static void composeApplication(DefaultPicoContainer appContainer)
    {
        //To change body of created methods use File | Settings | File Templates.
        appContainer.addComponent(AuthorizeController.class);
        appContainer.addComponent(TokenController.class);
        appContainer.addComponent(LoginController.class);
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
