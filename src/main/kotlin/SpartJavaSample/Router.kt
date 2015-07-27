package SpartJavaSample

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer
import org.picocontainer.PicoContainer
import spark.*
import spark.template.velocity.VelocityTemplateEngine

import java.lang.reflect.Method

import spark.Spark.halt
import java.util.*

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
    public fun <T : Controllable> to(controllerClass: Class<T>, container: PicoContainer, template: String) {
        addRouteForEachMethods(controllerClass, container, template)
    }

    // Sugar
    public fun usingContainer(container: PicoContainer): Router {
        val c = container

        return this
    }

    // Sugar
    public fun renderedWith(template: String): Router {
        val t = template

        return this
    }

    /**
     * Route a request to a controller using a particular parent container to resolve any decencies.

     * The response will not have any contents.

     * @param controllerClass the controller class that will handle the request/response
     *
     * @param container the parent container that will resolve any dependencies that aren't set at the request level
     *                  by the ContainerComposer
     *
     * @param  the Controllable type
     */
    public fun <T : Controllable> to(controllerClass: Class<T>, container: PicoContainer) {
        addRouteForEachMethods(controllerClass, container, null)
    }

    private fun <T : Controllable> addRoute(method: String, path: String, template: String, controllerClass: Class<T>, appContainer: PicoContainer) {
        //SparkBase.addRoute(method, TemplateViewRouteImpl.create(path, VelocityTemplateEngine()))
    }

    private fun <T : Controllable> addRoute(method: String, path: String, controllerClass: Class<T>, appContainer: PicoContainer) {
        /*SparkBase.addRoute(method, SparkBase.wrap(path)// There's no body for this method, so a reference to the model isn't needed. Pass in an object though
                // to avoid the chance of an accidental NullPointerException.
                // Dummy value
        )*/
    }

    private fun <T : Controllable> addRouteForEachMethods(controllerClass: Class<T>, container: PicoContainer, template: String?) {
        for (classMethod in controllerClass.getDeclaredMethods()) {
            val classMethodName = classMethod.getName()
            val isBeforeOrAfter = classMethodName == "before" || classMethodName == "after"

            if (isBeforeOrAfter) {
                continue // We don't want to route after or before using Spark, so skip these.
            }

            // See if the controller class' method is overriding one of Controllable's
            for (interfaceMethod in javaClass<Controllable>().getMethods()) {
                if (classMethodName == interfaceMethod.getName() && // method names match?
                        classMethod.getReturnType() == interfaceMethod.getReturnType() && // method return the same type?
                        Arrays.deepEquals(classMethod.getParameterTypes(), interfaceMethod.getParameterTypes()))
                // Params match?
                {
                    if (template == null) {
                        addRoute(classMethodName, path, controllerClass, container)
                    } else {
                        addRoute(classMethodName, path, template, controllerClass, container)
                    }

                    break
                }
            }
        }
    }

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

                val result = method.invoke(controller, request, response, model)

                if (java.lang.Boolean.TRUE == result) {
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
