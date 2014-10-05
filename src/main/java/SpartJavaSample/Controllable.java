package SpartJavaSample;

import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

interface Controllable
{
    default void before() { }

    default void process(Request request, Response response, final Map<String, Object> model) { }

    default void accept(Request request, Response response)
    {
        process(request, response, new HashMap<>());
    }

    default void after() { }
}
