package com.force.codes.fragmentnavigatordemo

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.force.codes.fragmentnavigator.FragmentNavigator
import com.force.codes.fragmentnavigator.StackNavigatorHelper
import com.force.codes.fragmentnavigator.navBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : FragmentNavigatorActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            createNavigation()
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.e(null, "onRestoreInstanceState")
        createNavigation()
    }

    private fun createNavigation() {
        val sparseArray = SparseArray<Fragment>()

        sparseArray.put(R.id.item0, TestFragment.newInstance(0))
        sparseArray.put(R.id.item1, TestFragmentTwo.newInstance(1))
        sparseArray.put(R.id.item3, TestFragmentThree.newInstance(2))
        sparseArray.put(R.id.item4, TestFragmentFour.newInstance(3))

        navigator = navBuilder(intent) {
            lifecycleOwner(this@MainActivity)
            fragmentManger(supportFragmentManager)
            fragmentMap(sparseArray)
            fade(true)
            container(R.id.container_view)
        }
    }
}

open class FragmentNavigatorActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    protected var bottomNavigationView: BottomNavigationView? = null

    protected var navigator: FragmentNavigator? = null

    override fun onStart() {
        super.onStart()
        bottomNavigationView?.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigator?.navigate(keyId = item.itemId)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView = null
    }

    override fun onBackPressed() {
        if (StackNavigatorHelper.isStackEmpty(bottomNavigationView!!)) {
            super.onBackPressed()
        }
    }
}