package com.nordicapis.kotlin_spark_sample

import com.nordicapis.kotlin_spark_sample.ControllerResult
import spark.Request
import spark.Response

SuppressWarnings("unused")
abstract class Controllable {
    public open fun before(request: Request, response: Response): Boolean = true

    public open fun get(request: Request, response: Response): ControllerResult = ControllerResult()

    public open fun post(request: Request, response: Response): ControllerResult = ControllerResult()

    public open fun put(request: Request, response: Response): ControllerResult = ControllerResult()

    public open fun delete(request: Request, response: Response): ControllerResult = ControllerResult()

    public open fun patch(request: Request, response: Response): ControllerResult = ControllerResult()

    public open fun head(request: Request, response: Response): ControllerResult = ControllerResult()

    public open fun trace(request: Request, response: Response): ControllerResult = ControllerResult()

    public open fun connect(request: Request, response: Response): ControllerResult = ControllerResult()

    public open fun options(request: Request): ControllerResult = ControllerResult()

    public open fun after(request: Request, response: Response) { }
}
