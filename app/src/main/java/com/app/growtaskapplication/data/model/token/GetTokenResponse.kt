package com.app.growtaskapplication.data.model.token

data class GetTokenResponse(
    val access_token: String,
    val expires_in: Int,
    val token_type: String
)