package com.app.growtaskapplication.utills

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.app.growtaskapplication.data.repository.artist.ArtistRepository
import com.app.growtaskapplication.data.repository.token.GetTokeRepository
import com.app.growtaskapplication.utills.Constants.CLIENT_CREDENTIAL
import com.app.growtaskapplication.utills.Constants.CLIENT_ID
import com.app.growtaskapplication.utills.Constants.CLIENT_SECRET
import com.app.growtaskapplication.utills.Constants.PREFS_TOKEN_FILE
import com.app.growtaskapplication.utills.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun clearData() {
        prefs.edit().clear().apply()
    }
}