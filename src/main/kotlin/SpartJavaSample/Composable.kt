package SpartJavaSample

import org.picocontainer.MutablePicoContainer

interface Composable
{
    fun composeApplication(appContainer: MutablePicoContainer) { }

    fun composeRequest(container: MutablePicoContainer) { }
}