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

Controllable.kt - The interface of all controllers (i.e., all request handler logic classes)
*/

package com.nordicapis.kotlin_spark_sample

import spark.Request
import spark.Response

SuppressWarnings("unused")
abstract class Controllable
{
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
