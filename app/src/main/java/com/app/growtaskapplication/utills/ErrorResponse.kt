package com.app.growtaskapplication.utills

data class ErrorResponse(
    val error: Error?
)
data class Error(
    val message: String?,
    val status: Int?
)