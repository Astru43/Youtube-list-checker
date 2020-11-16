package com.astru43.youtube_checker.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.youtube.YouTubeScopes

class Account(val context: Context) {

    var account: GoogleSignInAccount? = null
    lateinit var googleSignInClient: GoogleSignInClient


    fun login(activity: Activity)  {
        if (account != null) return
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope(YouTubeScopes.YOUTUBE_READONLY)
            ).build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(activity, signInIntent, 1, null)
    }

    fun logout() {
        if (account != null) {
            googleSignInClient.signOut().addOnCompleteListener {
                run {
                    account = null
                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun disconnect() {
        if (account != null) {
            googleSignInClient.revokeAccess().addOnCompleteListener {
                run {
                    account = null
                    Toast.makeText(context, "Account disconnected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getLastAccount(activity: Activity) : Boolean {
        account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(
                    Scope(YouTubeScopes.YOUTUBE_READONLY)
                ).build()
            googleSignInClient = GoogleSignIn.getClient(context, gso)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(activity, signInIntent, 1, null)
        }
        return false
    }
}