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

class Router constructor() : SparkBase()
{
    public fun <T : Controllable> routeTo(path: String, container: PicoContainer, controllerClass: Class<T>,
                                          template: String? = null)
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
                        addRoute(methodName, path, container, controllerClass)
                    }
                    else
                    {
                        addTemplatizedRoute(methodName, template as String, path, container, controllerClass)
                    }

                    break
                }
            }
        }
    }

    private fun <T: Controllable> addTemplatizedRoute(httpMethod: String, template: String, path: String, container: PicoContainer, controllerClass: Class<T>)
    {
        val r = fun (request: Request, response: Response): ModelAndView
        {
            var model = router(request, response, container, controllerClass)

            return ModelAndView(model, template)
        }

        SparkBase.addRoute(httpMethod, TemplateViewRouteImpl.create(path, r, VelocityTemplateEngine()))
    }

    private fun <T: Controllable> addRoute(httpMethod: String, path: String, container: PicoContainer, controllerClass: Class<T>)
    {
        val r = fun (request: Request, response: Response): Any
        {
            router(request, response, container, controllerClass)

            return Unit
        }

        SparkBase.addRoute(httpMethod, SparkBase.wrap(path, r))
    }

   private fun <T: Controllable> router(request: Request, response: Response, container: PicoContainer, controllerClass: Class<T>) : Map<String, Any>
   {
       val requestContainer = DefaultPicoContainer(container)
       var model : Map<String, Any> = emptyMap()

       //ContainerComposer.composeRequest(requestContainer)

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
}
