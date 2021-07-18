package com.astru43.youtube_checker.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes

class Account {

    var account: GoogleSignInAccount? = null
    var credential: GoogleAccountCredential? = null
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        val instance = Account()
    }


    fun login(context: Context, launcher: ActivityResultLauncher<Intent>) {
        if (account != null) return
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope(YouTubeScopes.YOUTUBE_READONLY)
            ).build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)

        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    fun logout(context: Context) {
        if (account != null) {
            googleSignInClient.signOut().addOnCompleteListener {
                run {
                    account = null
                    credential = null
                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun disconnect(context: Context) {
        if (account != null) {
            googleSignInClient.revokeAccess().addOnCompleteListener {
                run {
                    account = null
                    credential = null
                    Toast.makeText(context, "Account disconnected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getLastAccount(context: Context) {
        account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(
                    Scope(YouTubeScopes.YOUTUBE_READONLY)
                ).build()
            googleSignInClient = GoogleSignIn.getClient(context, gso)

            getCredential(context)
        }
    }

    fun getCredential(context: Context) {
        val scopes = mutableListOf(YouTubeScopes.YOUTUBE_READONLY)
        val credential =
            GoogleAccountCredential.usingOAuth2(context, scopes)
                .setBackOff(ExponentialBackOff())
        credential.selectedAccount = account!!.account
        this.credential = credential
    }
}