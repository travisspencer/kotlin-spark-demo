package SpartJavaSample

import spark.Request
import spark.Response

SuppressWarnings("unused")
abstract class Controllable {
    public open fun before(request: Request, response: Response): Boolean {
        return true
    }

    public open fun get(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public open fun post(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public open fun put(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public open fun delete(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public open fun patch(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public open fun head(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public open fun trace(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public open fun connect(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public open fun options(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public open fun after(request: Request, response: Response) {
    }
}
