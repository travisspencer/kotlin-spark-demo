package SpartJavaSample;

import spark.Request;
import spark.Response;

import java.util.Map;

public class AuthorizeController implements Controllable
{
    @Override
    public void process(Request request, Response response, final Map<String, Object> model)
    {
        model.put("Test", "test value");
    }
}
