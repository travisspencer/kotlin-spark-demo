package com.nordicapis.kotlin_spark_sample

import com.nordicapis.kotlin_spark_sample.Controllable
import com.nordicapis.kotlin_spark_sample.ControllerResult
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
            "user" to request.session(false).attribute("username"),
            "data" to mapOf(
                    "e1" to "e1 value",
                    "e2" to "e2 value",
                    "e3" to "e3 value")))
}
