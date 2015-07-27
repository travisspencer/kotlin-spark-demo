package SpartJavaSample

import spark.Request
import spark.Response

SuppressWarnings("unused")
interface Controllable {
    public fun before(request: Request, response: Response): Boolean {
        return true
    }

    public fun get(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public fun post(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public fun put(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public fun delete(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public fun patch(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public fun head(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public fun trace(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public fun connect(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public fun options(request: Request, response: Response, model: MutableMap<String, Any>): Boolean {
        return true
    }

    public fun after(request: Request, response: Response) {
    }
}
