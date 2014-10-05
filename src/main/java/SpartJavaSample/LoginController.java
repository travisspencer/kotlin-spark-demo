package SpartJavaSample;

import spark.Request;
import spark.Response;

import java.util.Map;

public class LoginController implements Controllable
{
    @Override
    public boolean get(Request request, Response response, Map<String, Object> model)
    {
        model.put("test", 44);

        return true;
    }

    @Override
    public boolean post(Request request, Response response, Map<String, Object> model)
    {
        request.session(); // Create session

        response.redirect("/authorize");

        return false;
    }
}
