package SpartJavaSample

import spark.Request
import spark.Response

import java.util.HashMap

public class AuthorizeController : Controllable() {
    public override fun before(request: Request, response: Response): Boolean {
        if (request.session(false) == null) {
            response.redirect("/login")

            return false
        }

        return true
    }

    public override fun get(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        val data = HashMap<String, String>()

        data.put("e1", "e1 value")
        data.put("e2", "e2 value")
        data.put("e3", "e3 value")

        model.put("user", "tspencer")
        model.put("data", data)

        return true
    }
}
