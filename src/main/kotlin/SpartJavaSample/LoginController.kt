package SpartJavaSample

import spark.Request
import spark.Response

public class LoginController : Controllable() {
    public override fun get(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        model.put("test", 44)

        return true
    }

    public override fun post(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        request.session() // Create session

        response.redirect("/authorize")

        return false
    }
}
