package com.nordicapis.kotlin_spark_sample

import org.picocontainer.MutablePicoContainer

interface Composable
{
    fun composeApplication(appContainer: MutablePicoContainer) { }

    fun composeRequest(container: MutablePicoContainer) { }
}