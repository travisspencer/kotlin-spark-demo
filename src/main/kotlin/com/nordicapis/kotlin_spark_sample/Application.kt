package com.nordicapis.kotlin_spark_sample

import com.nordicapis.kotlin_spark_sample.Composable
import com.nordicapis.kotlin_spark_sample.Controllable
import com.nordicapis.kotlin_spark_sample.Router
import com.nordicapis.kotlin_spark_sample.SimpleControllerComposer
import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer
import spark.servlet.SparkApplication
import kotlin.reflect.KClass
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

fun <T : Controllable> path(path: String, to: KClass<T>, renderWith: String? = null) : Application.RouteData<T>
{
    return Application.RouteData(path, to.java, renderWith)
}

fun <T> route(vararg values: T): List<T> = listOf<T>(*values)
