package SpartJavaSample

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer
import org.picocontainer.PicoContainer
import spark.*
import spark.template.velocity.VelocityTemplateEngine

import java.lang.reflect.Method

import spark.Spark.halt
import java.util.*
import kotlin.util.measureTimeMillis

class Router private constructor(private val path: String) : SparkBase() {

    /**
     * Route a request to a controller using a particular template to generate the response and a parent container
     * to resolve any dependencies.

     * @param controllerClass the controller class that will handle the request/response
     *
     * @param container the parent container that will resolve any dependencies that aren't set at the request level
     *                  by the ContainerComposer
     *
     * @param template the Velocity template file (relative to resources) to use when rendering the response
     *
     * @param  the Controllable type
     */
    public fun <T : Controllable> routeTo(controllerClass: Class<T>, container: PicoContainer, template: String? = null)
    {
        addRouteForEachMethods(controllerClass, container, template)
    }

    private fun <T : Controllable> addRouteForEachMethods(controllerClass: Class<T>, container: PicoContainer, template: String?)
    {
        for (classMethods in controllerClass.getDeclaredMethods())
        {
            val methodName = classMethods.getName()

            if (methodName == "before" || methodName == "after")
            {
                continue // We don't want to route after or before using Spark, so skip these.
            }

            // See if the controller class' method is overriding one of Controllable's
            for (interfaceMethod in javaClass<Controllable>().getMethods())
            {
                if (methodName == interfaceMethod.getName() && // method names match?
                        classMethods.getReturnType() == interfaceMethod.getReturnType() && // method return the same type?
                        Arrays.deepEquals(classMethods.getParameterTypes(), interfaceMethod.getParameterTypes())) // Params match?
                {
                    if (template == null)
                    {
                        addRoute(methodName, controllerClass, container)
                    }
                    else
                    {
                        addRoute(methodName, template, controllerClass, container)
                    }

                    break
                }
            }
        }
    }

    private fun <T : Controllable> addRoute(httpMethod: String, template: String, type: Class<T>, container: PicoContainer)
    {
        val r = fun (request: Request, response: Response): ModelAndView
        {
            var model = HashMap<String, String>()

            router(type, container, model, request, response)

            return ModelAndView(model, template)
        }
/*
        fun rr(request: Request, response: Response) : ModelAndView {
            var model = HashMap<String, String>()

            router(type, container, model, request, response)

            return ModelAndView(model, template)
        }*/

        SparkBase.addRoute(httpMethod, TemplateViewRouteImpl.create(path, r, VelocityTemplateEngine()))
    }

    private fun <T : Controllable> addRoute(httpMethod: String, type: Class<T>, container: PicoContainer)
    {
        val r = Route()
        {
            request, response -> router(type, container, HashMap<String, String>(), request, response)
        }

        SparkBase.addRoute(httpMethod, SparkBase.wrap(path, r))
    }

    /*    // Sugar
        public fun usingContainer(container: PicoContainer): Router {
            val c = container

            return this
        }

        // Sugar
        public fun andIsRenderedWith(template: String): Router {
            val t = template

            return this
        }*/


   private fun <T : Controllable> router(controllerClass: Class<T>, appContainer: PicoContainer, model: Map<String, Any>, request: Request, response: Response) {
        val requestContainer = DefaultPicoContainer(appContainer)

        ContainerComposer.composeRequest(requestContainer)

        try {
            val controller = requestContainer.getComponent(controllerClass)

            // TODO: Weave output of before into process and then into after
            val continue_ = controller.before(request, response)

            if (continue_) {
                // Fire the controller's method depending on the HTTP method of the request
                val httpMethod = request.requestMethod().toLowerCase()
                val method = controllerClass.getMethod(httpMethod, javaClass<Request>(), javaClass<Response>(), javaClass<Map<Any, Any>>())

                val result = method.invoke(controller, request, response, model) as Boolean

                if (result) {
                    controller.after(request, response)
                }
            }
        } catch (e: Exception) {
            halt(500, "Server Error")
        }

    }

    companion object {

        fun route(path: String): Router {
            return Router(path)
        }
    }
}
