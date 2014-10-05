package SpartJavaSample;

import spark.Request;
import spark.Response;

import java.util.Map;

@SuppressWarnings("unused")
interface Controllable
{
    default boolean before(Request request, Response response) { return true; }

    default boolean get(Request request, Response response, final Map<String, Object> model) { return true; }

    default boolean post(Request request, Response response, final Map<String, Object> model) { return true; }

    default boolean put(Request request, Response response, final Map<String, Object> model) { return true; }

    default boolean delete(Request request, Response response, final Map<String, Object> model) { return true; }

    default boolean patch(Request request, Response response, final Map<String, Object> model) { return true; }

    default boolean head(Request request, Response response, final Map<String, Object> model) { return true; }

    default boolean trace(Request request, Response response, final Map<String, Object> model) { return true; }

    default boolean connect(Request request, Response response, final Map<String, Object> model) { return true; }

    default boolean options(Request request, Response response, final Map<String, Object> model) { return true; }

    default void after(Request request, Response response) { }
}
