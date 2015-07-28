package SpartJavaSample

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer
import spark.servlet.SparkApplication

public class Application(
        var composer: Composable = SimpleControllerComposer(),
        var appContainer: MutablePicoContainer = DefaultPicoContainer(),
        var routes: () -> List<Application.Route>) : SparkApplication
{
    init
    {
        composer.composeApplication(appContainer)
    }

    override fun init() { }

    fun host()
    {
        var routes = routes.invoke()

        for (route in routes)
        {
            Router.route(route.path, appContainer, route.controller, route.template)
        }
    }

    data class Route(val path: String, val controller: Controllable, val template: String? = null)
}

/*inline fun String.routesTo<reified T : Controllable>(): Router
{
    var router = Router.route(this)

    router.routeTo(javaClass<T>(), appContainer)

    return router
}*/

/*
fun route(init : Application.Route.() -> Unit) : Application.Route
{
    return Application.Route(path = "", controller = LoginController(), template = "")
    //Application.Route().init()
}
*/

/*fun ff(init : Application.Route.() -> Unit) : Application.Route
{
    val r = Application.Route("", LoginController(), "")

    r.in

    return r
}*/

fun api(composer: Composable, routes: () -> List<Application.Route>)
{
    Application(composer = composer, routes = routes).host()
}

fun String.to(controller: Controllable): Controllable
{
    return controller
}

fun Controllable.renderedBy(template: String): String
{
    return template
}

fun path(path: String, to: Controllable, renderWith: String? = null) : Application.Route
{
    return Application.Route(path, to, renderWith)
}

fun route(vararg values: Application.Route): kotlin.List<Application.Route> = listOf(*values)

fun main(args: Array<String>) = api(composer = ContainerComposer())
{
    route(
        path("/login", to = LoginController(), renderWith = "login.vm"),
        path("/login", to = LoginController(), renderWith = "login.vm"),
        path("/token", to = TokenController()))
}

       /* "/" -> "/" -> AuthorizeController().andRenderedWith("authorize.vm")
        "/login" -> LoginController().andRenderedWith("login.vm")
        "/token" -> TokenController()
        // route "/foo" to AuthorizeController() andRenderWith "foo.vm"
        *//*System.out.println("Started")

        "/authorize".routesTo<AuthorizeController>().andIsRenderedWith("authorize.vm")

        "/login".routesTo<LoginController>().andIsRenderedWith("login.vm")

        "/token".routesTo<TokenController>()   *//*

        //"/foo" handledBy TokenController() andRenderedWith "foo.vm"
    }*/

