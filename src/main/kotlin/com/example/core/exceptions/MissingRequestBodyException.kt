package com.example.core.exceptions

import java.lang.RuntimeException

class MissingRequestBodyException(override val message: String) : RuntimeException()