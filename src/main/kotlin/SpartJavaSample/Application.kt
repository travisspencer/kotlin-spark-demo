package SpartJavaSample

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer
import spark.servlet.SparkApplication

public class Application(
        var composer: Composable = SimpleControllerComposer(),
        var appContainer: MutablePicoContainer = DefaultPicoContainer(),
        var routes: () -> Unit) : SparkApplication
{
    init
    {
        composer.composeApplication(appContainer)
    }

    override fun init() { }

    fun host()
    {
        routes.invoke()
    }
}

inline fun String.routesTo<reified T : Controllable>(): Router
{
    var router = Router.route(this)

    router.routeTo(javaClass<T>(), appContainer)

    return router
}

fun main(args: Array<String>)
{

    val api = Application(composer = ContainerComposer())
    {
        // route "/foo" to AuthorizeController() andRenderWith "foo.vm"
        System.out.println("Started")

        "/authorize".routesTo<AuthorizeController>().andIsRenderedWith("authorize.vm")

        "/login".routesTo<LoginController>().andIsRenderedWith("login.vm")

        "/token".routesTo<TokenController>()

        //"/foo" handledBy TokenController() andRenderedWith "foo.vm"
    }

    api.host()
}
