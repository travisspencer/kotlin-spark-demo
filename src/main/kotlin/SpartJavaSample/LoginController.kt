package SpartJavaSample

import spark.Request
import spark.Response

public class LoginController : Controllable()
{
    public override fun get(request: Request, response: Response): ControllerResult =
            ControllerResult(model = mapOf("test" to 44))

    public override fun post(request: Request, response: Response): ControllerResult
    {
        request.session() // Create session

        response.redirect("/authorize")

        return ControllerResult(continueProcessing = false)
    }
}
