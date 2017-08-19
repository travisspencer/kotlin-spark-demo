/*
Copyright (C) 2015 Nordic APIs AB

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

AuthorizeController.kt - The controller class that provides the logic for the authorize endpoint
*/

package com.nordicapis.kotlin_spark_sample

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response

class AuthorizeController : Controllable() {

    companion object {
        val _logger: Logger = LoggerFactory.getLogger(AuthorizeController::class.java)
    }

    override fun before(request: Request, response: Response): Boolean {
        _logger.trace("before on Authorize controller invoked")

        if (request.session(false) == null) {
            _logger.debug("No session exists. Redirecting to login")

            response.redirect("/login")

            // Return false to abort any further processing
            return false
        }

        _logger.debug("Session exists")

        return true
    }

    override fun get(request: Request, response: Response): ControllerResult = ControllerResult(model = mapOf(
            "user" to request.session(false).attribute("username"),
            "data" to mapOf(
                    "e1" to "e1 value",
                    "e2" to "e2 value",
                    "e3" to "e3 value")))
}
