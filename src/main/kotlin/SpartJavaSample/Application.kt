package SpartJavaSample

import org.picocontainer.DefaultPicoContainer
import spark.servlet.SparkApplication

public class Application : SparkApplication {
    val appContainer = DefaultPicoContainer()

    override fun init() {
        ContainerComposer.composeApplication(appContainer)

        "/authorize" routeTo AuthorizeController() usingContainer appContainer renderedWith "authorize.vme"

        "/login" routeTo LoginController() usingContainer appContainer renderedWith "login.vm"

        "/token" routeTo TokenController() usingContainer appContainer
    }
}

fun main(args: Array<String>) = Application().init()

fun String.routeTo<T : Controllable>(controller: T): Router {
    var router = Router.route(this)

    router.to(controller)

    return router
}
