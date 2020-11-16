package com.astru43.youtube_checker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.viewpager.widget.ViewPager
import com.astru43.youtube_checker.ui.main.management.ManagementPagerAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.astru43.youtube_checker.util.Account


class ListManagementActivity : AppCompatActivity() {

    private var account = Account(this)
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_management)
        val managementPagerAdapter =
            ManagementPagerAdapter(
                this,
                supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.management_view_pager)
        viewPager.adapter = managementPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            request()
        }



    }

    override fun onStart() {
        super.onStart()
        account.getLastAccount(this);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.login -> {
                Account(this).login(this)
                return true
            }
            R.id.logout -> {
                Account(this).logout()
                return true
            }
            R.id.disconnect -> {

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            try {
                account.account = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)
            } catch (e: ApiException) {
                Log.w("Error", "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    private fun request() {
        if (account.account != null) {
            val scopes = mutableListOf(YouTubeScopes.YOUTUBE_READONLY)
            val credential =
                GoogleAccountCredential.usingOAuth2(this, scopes).setBackOff(ExponentialBackOff())
            credential.selectedAccount = account.account!!.account

            Log.i("Account", credential.selectedAccountName)

            val httpTransport = AndroidHttp.newCompatibleTransport()
            val jasonFactory = JacksonFactory.getDefaultInstance()
            val youtube = YouTube.Builder(httpTransport, jasonFactory, credential)
                .setApplicationName("Youtube_list_checker").build()

            val request = youtube.Playlists().list("snippet")


            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                val response = request.setMaxResults(50).setMine(true).execute()
                Log.i("youtube", response.items.size.toString())
            }
        } else Toast.makeText(this, "Not logged in", Toast.LENGTH_LONG).show()
    }

}