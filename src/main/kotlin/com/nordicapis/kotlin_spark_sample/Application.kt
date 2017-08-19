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

Application.kt - The main class and functions used to expose an API application using Spark
*/

package com.nordicapis.kotlin_spark_sample

import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer
import spark.servlet.SparkApplication
import kotlin.reflect.KClass

class Application(
        var composer: Composable = Noncomposer(),
        private var appContainer: MutablePicoContainer = DefaultPicoContainer(),
        var routes: () -> List<Application.RouteData<Controllable>>) : SparkApplication {
    private var router = Router()

    init {
        composer.composeApplication(appContainer)
    }

    override fun init() {}

    fun host() {
        var routes = routes.invoke()

        for (routeData in routes) {
            val (path, controllerClass, template) = routeData

            router.routeTo(path, appContainer, controllerClass, composer, template)
        }
    }

    data class RouteData<out T : Controllable>(val path: String, val controllerClass: Class<out T>, val template: String? = null)
}

fun api(composer: Composable, routes: () -> List<Application.RouteData<Controllable>>) {
    Application(composer = composer, routes = routes).host()
}

fun <T : Controllable> path(path: String, to: KClass<T>, renderWith: String? = null): Application.RouteData<T> {
    return Application.RouteData(path, to.java, renderWith)
}

fun <T> route(vararg values: T): List<T> = listOf(*values)
