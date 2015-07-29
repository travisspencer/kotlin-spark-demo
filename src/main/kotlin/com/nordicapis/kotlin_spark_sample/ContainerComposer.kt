package com.nordicapis.kotlin_spark_sample

import com.nordicapis.kotlin_spark_sample.AuthorizeController
import com.nordicapis.kotlin_spark_sample.Composable
import com.nordicapis.kotlin_spark_sample.LoginController
import com.nordicapis.kotlin_spark_sample.TokenController
import org.picocontainer.DefaultPicoContainer
import org.picocontainer.MutablePicoContainer

class ContainerComposer : Composable
{
    public override fun composeApplication(appContainer: MutablePicoContainer)
    {
        appContainer.addComponent(javaClass<AuthorizeController>())
        appContainer.addComponent(javaClass<TokenController>())
        appContainer.addComponent(javaClass<LoginController>())
    }

    public override fun composeRequest(container: MutablePicoContainer)
    {
    }
}
