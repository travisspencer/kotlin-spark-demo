package com.nordicapis.kotlin_spark_sample

fun main(args: Array<String>) = api(composer = ContainerComposer())
{
    route(
            path("/login", to = LoginController::class, renderWith = "login.vm"),
            path("/authorize", to = AuthorizeController::class, renderWith = "authorize.vm"),
            path("/token", to = TokenController::class))
}
