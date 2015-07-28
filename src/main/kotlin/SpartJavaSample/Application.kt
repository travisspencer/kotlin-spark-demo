package SpartJavaSample

import org.picocontainer.DefaultPicoContainer
import spark.servlet.SparkApplication

var appContainer = DefaultPicoContainer()

public class Application(var routes: () -> Unit) : SparkApplication {
    init
    {
        ContainerComposer.composeApplication(appContainer)

        routes.invoke()
    }

    override fun init() {} //routes.invoke()

    companion object
    {
        fun host(routes: () -> Unit): Unit
        {
            Application(routes)
        }
    }
}

fun main(args: Array<String>) = Application.host()
{
    // route "/foo" to AuthorizeController() andRenderWith "foo.vm"
    System.out.println("Started")

    "/authorize".routesTo<AuthorizeController>().andIsRenderedWith("authorize.vm")

    "/login".routesTo<LoginController>().andIsRenderedWith("login.vm")

    "/token".routesTo<TokenController>()

    //"/foo" handledBy TokenController() andRenderedWith "foo.vm"
}

inline fun String.routesTo<reified T : Controllable>(): Router {
    var router = Router.route(this)

    router.routeTo(javaClass<T>(), appContainer)

    return router
}
