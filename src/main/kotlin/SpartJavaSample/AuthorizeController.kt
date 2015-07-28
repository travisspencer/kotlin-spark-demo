package SpartJavaSample

import spark.Request
import spark.Response

public class AuthorizeController : Controllable() {
    public override fun before(request: Request, response: Response): Boolean
    {
        if (request.session(false) == null)
        {
            response.redirect("/login")

            return false
        }

        return true
    }

    public override fun get(request: Request, response: Response): ControllerResult = ControllerResult(model = mapOf(
            "user" to "jdoe",
            "data" to mapOf(
                    "e1" to "e1 value",
                    "e2" to "e2 value",
                    "e3" to "e3 value")))
}
