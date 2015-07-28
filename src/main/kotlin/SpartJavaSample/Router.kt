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

class Router private constructor(private val path: String, private var template: String? = null) : SparkBase()
{
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
    public fun <T : Controllable> routeTo(controllerClass: Class<T>, container: PicoContainer)
    {
        addRouteForEachMethods(controllerClass, container)
    }

    private fun <T : Controllable> addRouteForEachMethods(controllerClass: Class<T>, container: PicoContainer)
    {
        for (classMethod in controllerClass.getDeclaredMethods())
        {
            val methodName = classMethod.getName()

            if (methodName == "before" || methodName == "after")
            {
                continue // We don't want to route after or before using Spark, so skip these.
            }

            // See if the controller class' method is overriding one of Controllable's
            for (interfaceMethod in javaClass<Controllable>().getMethods())
            {
                if (methodName == interfaceMethod.getName() && // method names match?
                        classMethod.getReturnType() == interfaceMethod.getReturnType() && // method return the same type?
                        Arrays.deepEquals(classMethod.getParameterTypes(), interfaceMethod.getParameterTypes())) // Params match?
                {
                    if (template.isNullOrBlank())
                    {
                        addRoute(methodName, controllerClass, container)
                    }
                    else
                    {
                        addRoute(methodName, template as String, controllerClass, container)
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
            var model = router(type, container, request, response)

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
        val r = fun (request: Request, response: Response): Any
        {
            router(type, container, request, response)

            return Unit
        }

        SparkBase.addRoute(httpMethod, SparkBase.wrap(path, r))
    }

    /*    // Sugar
        public fun usingContainer(container: PicoContainer): Router {
            val c = container

            return this
        }
            */
        // Sugar
   public fun andIsRenderedWith(template: String): Router {
       this.template = template

       return this
   }

   private fun <T : Controllable> router(controllerClass: Class<T>, appContainer: PicoContainer, request: Request, response: Response) : Map<String, Any>
   {
       val requestContainer = DefaultPicoContainer(appContainer)
       var model : Map<String, Any> = emptyMap()

       ContainerComposer.composeRequest(requestContainer)

       try
       {
           val controller = requestContainer.getComponent(controllerClass)

           if (controller.before(request, response))
           {
                // Fire the controller's method depending on the HTTP method of the request
               val httpMethod = request.requestMethod().toLowerCase()
               val method = controllerClass.getMethod(httpMethod, javaClass<Request>(), javaClass<Response>())
               val result = method.invoke(controller, request, response)

               if (result is ControllerResult && result.continueProcessing)
               {
                   controller.after(request, response)

                   model = result.model
               }
               else if (result is Boolean && result)
               {
                   controller.after(request, response)
               }
           }
       }
       catch (e: Exception)
       {
           halt(500, "Server Error")
       }

       return model
    }

    companion object
    {
        fun route(path: String): Router
        {
            return Router(path)
        }
    }
}
