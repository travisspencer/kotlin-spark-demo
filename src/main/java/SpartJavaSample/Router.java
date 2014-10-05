package SpartJavaSample;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import spark.*;
import spark.template.velocity.VelocityTemplateEngine;

import java.lang.reflect.Method;
import java.util.*;

import static spark.Spark.halt;

class Router extends SparkBase
{
    private String path;

    private Router(String path)
    {
        this.path = path;
    }

    static Router route(String path)
    {
        return new Router(path);
    }

    /**
     * Route a request to a controller using a particular template to generate the response and a parent container
     * to resolve any dependencies.
     *
     * @param controllerClass the controller class that will handle the request/response
     * @param container the parent container that will resolve any dependencies that aren't set at the request level
     *                  by the ContainerComposer
     * @param template the Velocity template file (relative to resources) to use when rendering the response
     * @param <T> the Controllable type
     */
    public <T extends Controllable> void to(Class<T> controllerClass, PicoContainer container, String template)
    {
        addRouteForEachMethods(controllerClass, container, template);
    }

    /**
     * Route a request to a controller using a particular parent container to resolve any decencies.
     *
     * The response will not have any contents.
     *
     * @param controllerClass the controller class that will handle the request/response
     * @param container the parent container that will resolve any dependencies that aren't set at the request level
     *                  by the ContainerComposer
     * @param <T> the Controllable type
     */
    public <T extends Controllable> void to(Class<T> controllerClass, PicoContainer container)
    {
        addRouteForEachMethods(controllerClass, container, null);
    }

    // Sugar
    public static String renderedWith(String template)
    {
        return template;
    }

    // Sugar
    public static PicoContainer usingContainer(PicoContainer container)
    {
        return container;
    }

    private <T extends Controllable> void addRoute(String method, String path, String template, Class<T> controllerClass, PicoContainer appContainer)
    {
        addRoute(method, TemplateViewRouteImpl.create(path, (Request request, Response response) ->
        {
            Map<String, Object> model = new HashMap<>();

            router(controllerClass, appContainer, model, request, response);

            return new ModelAndView(model, template);
        }, new VelocityTemplateEngine()));
    }

    private <T extends Controllable> void addRoute(String method, String path, Class<T> controllerClass, PicoContainer appContainer)
    {
        addRoute(method, SparkBase.wrap(path, (Request request, Response response) ->
        {
            // There's no body for this method, so a reference to the model isn't needed. Pass in an object though
            // to avoid the chance of an accidental NullPointerException.
            router(controllerClass, appContainer, new HashMap<>(), request, response);

            return null; // Dummy value
        }));
    }

    private <T extends Controllable> void addRouteForEachMethods(Class<T> controllerClass, PicoContainer container, String template)
    {
        for (Method classMethod : controllerClass.getDeclaredMethods())
        {
            String classMethodName = classMethod.getName();
            boolean isBeforeOrAfter = classMethodName.equals("before") || classMethodName.equals("after");

            if (isBeforeOrAfter)
            {
                continue; // We don't want to route after or before using Spark, so skip these.
            }

            // See if the controller class' method is overriding one of Controllable's
            for (Method interfaceMethod : Controllable.class.getMethods())
            {
                if (classMethodName.equals(interfaceMethod.getName()) && // method names match?
                        classMethod.getReturnType() == interfaceMethod.getReturnType() && // method return the same type?
                        Arrays.deepEquals(classMethod.getParameterTypes(), interfaceMethod.getParameterTypes())) // Params match?
                {
                    if (template == null)
                    {
                        addRoute(classMethodName, path, controllerClass, container);
                    }
                    else
                    {
                        addRoute(classMethodName, path, template, controllerClass, container);
                    }

                    break;
                }
            }
        }
    }

    private <T extends Controllable> void router(Class<T> controllerClass, PicoContainer appContainer,
                                                 Map<String, Object> model, Request request, Response response)
    {
        MutablePicoContainer requestContainer = new DefaultPicoContainer(appContainer);

        ContainerComposer.composeRequest(requestContainer);

        try
        {
            Controllable controller = requestContainer.getComponent(controllerClass);

            // TODO: Weave output of before into process and then into after
            boolean continue_ = controller.before(request, response);

            if (continue_)
            {
                // Fire the controller's method depending on the HTTP method of the request
                String httpMethod = request.requestMethod().toLowerCase();
                Method method = controllerClass.getMethod(httpMethod, Request.class, Response.class, Map.class);

                Object result = method.invoke(controller, request, response, model);

                if (Boolean.TRUE.equals(result))
                {
                    controller.after(request, response);
                }
            }
        }
        catch (Exception e)
        {
            halt(500, "Server Error");
        }
    }
}
