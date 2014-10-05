package SpartJavaSample;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import spark.*;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.halt;

class Tinder extends SparkBase
{
    private String path;
    private List<String> methods;

    private Tinder(String path)
    {
        this.path = path;
    }

    static Tinder route(String path)
    {
        return new Tinder(path);
    }

    public Tinder havingMethod(String method)
    {
        this.methods = Arrays.asList(method);

        return this;
    }

    public Tinder havingMethods(String... methods)
    {
        this.methods = Arrays.asList(methods);

        return this;
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
        methods.forEach(method -> addRoute(method, path, template, controllerClass, container));
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
        methods.forEach(method -> addRoute(method, path, controllerClass, container));
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

    private <T extends Controllable> void router(Class<T> controllerClass, PicoContainer appContainer,
                                                 Map<String, Object> model, Request request, Response response)
    {
        MutablePicoContainer requestContainer = new DefaultPicoContainer(appContainer);

        ContainerComposer.composeRequest(requestContainer);

        try
        {
            Controllable controller = requestContainer.getComponent(controllerClass);

            // TODO: Weave output of before into process and then into after
            controller.before();

            controller.process(request, response, model);

            controller.after();
        }
        catch (Exception e)
        {
            halt(500, "Server Error");
        }
    }
}
