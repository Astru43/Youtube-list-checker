package com.astru43.youtube_checker

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.astru43.youtube_checker.ui.main.management.ManagementPagerAdapter
import com.astru43.youtube_checker.ui.main.management.SaveViewModel
import com.astru43.youtube_checker.util.Account
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ListManagementActivity : AppCompatActivity() {

    private val account = Account.instance
    private val saveViewModel: SaveViewModel by viewModels()
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                account.account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    .getResult(ApiException::class.java)
                account.getCredential(this)
                saveViewModel.refresh()
            } catch (e: ApiException) {
                Log.w("Error", "signInResult:failed code=" + e.statusCode)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_management)
        val managementPagerAdapter =
            ManagementPagerAdapter(
                this,
                supportFragmentManager,
                this.lifecycle
            )
        val viewPager: ViewPager2 = findViewById(R.id.management_view_pager)
        viewPager.adapter = managementPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = managementPagerAdapter.getPageTitle(position)
        }.attach()
        val fab: FloatingActionButton = findViewById(R.id.fab)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onStart() {
        super.onStart()
        account.getLastAccount(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.login -> {
                account.login(this, getResult)
                return true
            }
            R.id.logout -> {
                account.logout(this)
                saveViewModel.refresh()
                return true
            }
            R.id.disconnect -> {
                account.disconnect(this)
                saveViewModel.refresh()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}