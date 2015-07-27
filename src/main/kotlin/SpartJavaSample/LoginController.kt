package SpartJavaSample

import spark.Request
import spark.Response

public class LoginController : Controllable {
    override fun get(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        model.put("test", 44)

        return true
    }

    override fun post(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        request.session() // Create session

        response.redirect("/authorize")

        return false
    }
}
