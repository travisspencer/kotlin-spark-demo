package SpartJavaSample

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer
import spark.servlet.SparkApplication
import kotlin.reflect.jvm.java

public class Application(
        var composer: Composable = SimpleControllerComposer(),
        var appContainer: MutablePicoContainer = DefaultPicoContainer(),
        var routes: () -> List<Application.RouteData<Controllable>>) : SparkApplication
{
    private var router = Router()

    init
    {
        composer.composeApplication(appContainer)
    }

    override fun init() { }

    fun host()
    {
        var routes = routes.invoke()

        for (routeData in routes)
        {
            val (path, controllerClass, template) = routeData

            router.routeTo(path, appContainer, controllerClass, template)
        }
    }

    data class RouteData<out T : Controllable>(val path: String, val controllerClass: Class<out T>, val template: String? = null)
}

fun api(composer: Composable, routes: () -> List<Application.RouteData<Controllable>>)
{
    Application(composer = composer, routes = routes).host()
}

fun <T : Controllable> path(path: String, to: Class<T>, renderWith: String? = null) : Application.RouteData<T>
{
    return Application.RouteData(path, to, renderWith)
}

fun <T> route(vararg values: T): kotlin.List<T> = listOf<T>(*values)

fun main(args: Array<String>) = api(composer = ContainerComposer())
{
    route(
            path("/login", to = LoginController::class.java, renderWith = "login.vm"),
            path("/authorize", to = AuthorizeController::class .java, renderWith = "login.vm"),
            path("/token", to = TokenController::class .java))
}
