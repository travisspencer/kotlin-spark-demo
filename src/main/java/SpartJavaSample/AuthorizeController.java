package SpartJavaSample;

import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class AuthorizeController implements Controllable
{
    @Override
    public boolean before(Request request, Response response)
    {
        if (request.session(false) == null)
        {
            response.redirect("/login");

            return false;
        }

        return true;
    }

    @Override
    public boolean get(Request request, Response response, final Map<String, Object> model)
    {
        Map<String, String> data = new HashMap<>();

        data.put("e1", "e1 value");
        data.put("e2", "e2 value");
        data.put("e3", "e3 value");

        model.put("user", "tspencer");
        model.put("data", data);

        return true;
    }
}
