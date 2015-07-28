package SpartJavaSample

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer

class ContainerComposer : Composable
{
    public override fun composeApplication(appContainer: MutablePicoContainer)
    {
        appContainer.addComponent(javaClass<AuthorizeController>())
        appContainer.addComponent(javaClass<TokenController>())
        appContainer.addComponent(javaClass<LoginController>())
    }

    public override fun composeRequest(container: MutablePicoContainer)
    {
    }
}
