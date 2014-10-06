package SpartJavaSample;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

@SuppressWarnings("unused")
class ContainerComposer
{
    public static void composeApplication(DefaultPicoContainer appContainer)
    {
        appContainer.addComponent(AuthorizeController.class);
        appContainer.addComponent(TokenController.class);
        appContainer.addComponent(LoginController.class);
    }

    public static void composeSession(MutablePicoContainer container) { }

    public static void composeRequest(MutablePicoContainer container) { }
}
