/*
Copyright (C) 2015 Nordic APIs AB

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Router.kt - The class that routes all controllers and handles templating
*/

package com.nordicapis.kotlin_spark_sample

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.PicoContainer
import spark.*
import spark.Spark.halt
import spark.template.velocity.VelocityTemplateEngine
import java.util.*
import kotlin.reflect.jvm.java

class Router constructor() : SparkBase()
{
    public fun <T : Controllable> routeTo(path: String, container: PicoContainer, controllerClass: Class<T>,
                                          composer: Composable, template: String? = null)
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
                        addRoute(methodName, path, container, controllerClass, composer)
                    }
                    else
                    {
                        addTemplatizedRoute(methodName, template as String, path, container, controllerClass, composer)
                    }

                    break
                }
            }
        }
    }

    private fun <T: Controllable> addTemplatizedRoute(httpMethod: String, template: String, path: String,
                                                      container: PicoContainer, controllerClass: Class<T>, composer: Composable)
    {
        val r = fun (request: Request, response: Response): ModelAndView
        {
            var model = router(request, response, container, controllerClass, composer)

            return ModelAndView(model, template)
        }

        SparkBase.addRoute(httpMethod, TemplateViewRouteImpl.create(path, r, VelocityTemplateEngine()))
    }

    private fun <T: Controllable> addRoute(httpMethod: String, path: String, container: PicoContainer,
                                           controllerClass: Class<T>, composer: Composable)
    {
        val r = fun (request: Request, response: Response): Any
        {
            router(request, response, container, controllerClass, composer)

            return response.body()
        }

        SparkBase.addRoute(httpMethod, SparkBase.wrap(path, r))
    }

   private fun <T: Controllable> router(request: Request, response: Response, container: PicoContainer,
                                        controllerClass: Class<T>, composer: Composable) : Map<String, Any>
   {
       val requestContainer = DefaultPicoContainer(container)
       var model : Map<String, Any> = emptyMap()

       composer.composeRequest(requestContainer)

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
           }
       }
       catch (e: Exception)
       {
           halt(500, "Server Error")
       }

       return model
    }
}
