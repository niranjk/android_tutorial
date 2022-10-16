package com.niranjan.androidtutorials

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

open class DrawerBaseActivity : AppCompatActivity(), OnNavigationItemSelectedListener{

    var drawerLayout: DrawerLayout? = null

    override fun setContentView(view: View?) {
        // viewbinding
        drawerLayout = layoutInflater.inflate(R.layout.activity_drawer_base, null) as DrawerLayout
        val containerLayout = drawerLayout?.findViewById<FrameLayout>(R.id.mainContainer)
        containerLayout?.addView(view)
        super.setContentView(drawerLayout)
        val toolbar = drawerLayout?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)
        val navigationView = drawerLayout?.findViewById<NavigationView>(R.id.nav_view)
        navigationView?.setNavigationItemSelectedListener(this@DrawerBaseActivity)
        val toggle = ActionBarDrawerToggle(
            this@DrawerBaseActivity,
            drawerLayout ,
            toolbar,
            R.string.label_drawer_open,
            R.string.label_drawer_close
        )
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout?.closeDrawer(GravityCompat.START)
        when(item.itemId){
            R.id.slotActivity -> {
                //navigate to SlotActivity
                startActivity(
                    Intent().setClassName(
                        BuildConfig.APPLICATION_ID,
                        BuildConfig.DF_SLOT_ACTIVITY
                    )
                )
            }
            R.id.priceCalculatorActivity -> {
                // navigate to PriceCalculatorActivity
                startActivity(
                    Intent().setClassName(
                        BuildConfig.APPLICATION_ID,
                        BuildConfig.DF_PRICE_ACTIVITY
                    )
                )
            }
            R.id.quizActivity -> {
                // navigate to PriceCalculatorActivity
                startActivity(
                    Intent().setClassName(
                        BuildConfig.APPLICATION_ID,
                        BuildConfig.DF_QUIZ_ACTIVITY
                    )
                )
            }
        }
        return false
    }

    protected fun allocateActivityTitle(titleString: String){
        if (supportActionBar != null) supportActionBar?.title = titleString
    }
}