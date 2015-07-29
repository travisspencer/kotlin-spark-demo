package com.nordicapis.kotlin_spark_sample

import com.nordicapis.kotlin_spark_sample.Controllable
import com.nordicapis.kotlin_spark_sample.ControllerResult
import spark.Request
import spark.Response

public class TokenController : Controllable()
{
    public override fun get(request: Request, response: Response): ControllerResult
    {
        response.body("my good token")

        return ControllerResult()
    }
}
