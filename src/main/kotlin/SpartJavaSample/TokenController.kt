package SpartJavaSample

import spark.Request
import spark.Response

public class TokenController : Controllable() {
    public override fun get(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        //return null; // TODO: Do work

        return true
    }
}
