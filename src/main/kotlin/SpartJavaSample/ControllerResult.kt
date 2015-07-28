package SpartJavaSample

import java.util.*

data class ControllerResult(
        val continueProcessing: Boolean = true,
        val model: Map<String, Any> = emptyMap())