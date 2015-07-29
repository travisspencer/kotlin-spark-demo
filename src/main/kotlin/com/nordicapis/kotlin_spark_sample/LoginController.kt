package com.nordicapis.kotlin_spark_sample

import com.nordicapis.kotlin_spark_sample.Controllable
import com.nordicapis.kotlin_spark_sample.ControllerResult
import spark.Request
import spark.Response

public class LoginController : Controllable()
{
    // If this isn't overidden, even with this trivial response, it won't be routed. So, this is the minimum
    // implementation to get the page to show up. Usually, a model will be created and returned in addition to the
    // default response (which, because continueProcessing is trus, will cause the router to continue processing).
    public override fun get(request: Request, response: Response): ControllerResult = ControllerResult()

    public override fun post(request: Request, response: Response): ControllerResult
    {
        var session = request.session() // Create session

        // Save the username in the session, so that it can be used in the authorize endpoint (e.g., for consent)
        session.attribute("username", request.queryParams("username"))

        // Redirect back to the authorize endpoint now that "login" has been performed
        response.redirect("/authorize")

        return ControllerResult(continueProcessing = false)
    }
}
