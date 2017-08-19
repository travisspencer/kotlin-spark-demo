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

LoginController.kt - The controller class that provides the logic for the login endpoint
*/

package com.nordicapis.kotlin_spark_sample

import spark.Request
import spark.Response

class LoginController : Controllable() {
    // If this isn't overidden, even with this trivial response, it won't be routed. So, this is the minimum
    // implementation to get the page to show up. Usually, a model will be created and returned in addition to the
    // default response (which, because continueProcessing is true, will cause the router to continue processing).
    override fun get(request: Request, response: Response): ControllerResult = ControllerResult()

    override fun post(request: Request, response: Response): ControllerResult {
        var session = request.session() // Create session

        // Save the username in the session, so that it can be used in the authorize endpoint (e.g., for consent)
        session.attribute("username", request.queryParams("username"))

        // Redirect back to the authorize endpoint now that "login" has been performed
        response.redirect("/authorize")

        return ControllerResult(continueProcessing = false)
    }
}
