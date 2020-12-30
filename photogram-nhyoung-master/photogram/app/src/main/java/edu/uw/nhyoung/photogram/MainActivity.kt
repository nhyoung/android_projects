package edu.uw.nhyoung.photogram

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val TAG = "MAIN_ACTIVITY"
    private val RC_SIGN_IN = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_login -> {
                if (FirebaseAuth.getInstance().currentUser == null) {
                    startSignInFlow()
                } else {
                    signOut()
                }
                true
            }

            R.id.menu_item_settings -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.SettingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startSignInFlow() {
        //providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Sign in successful
                invalidateOptionsMenu() // refresh menu title to "log out"
            } else {
                // Sign in failed
                Toast.makeText(this,"Sign in failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.run {
            val menuItemLogin = findItem(R.id.menu_item_login)

            // no user logged in
            if(FirebaseAuth.getInstance().currentUser == null) {
                menuItemLogin.setTitle("Log in")
            // user logged in
            } else {
                menuItemLogin.setTitle("Log out")
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                invalidateOptionsMenu() // refresh menu title to "log in"
            }
    }
}