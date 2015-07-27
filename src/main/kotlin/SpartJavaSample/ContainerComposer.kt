package SpartJavaSample

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer

SuppressWarnings("unused")
object ContainerComposer {
    public fun composeApplication(appContainer: DefaultPicoContainer) {
        appContainer.addComponent(javaClass<AuthorizeController>())
        appContainer.addComponent(javaClass<TokenController>())
        appContainer.addComponent(javaClass<LoginController>())
    }

    public fun composeSession(container: MutablePicoContainer) {
    }

    public fun composeRequest(container: MutablePicoContainer) {
    }
}
