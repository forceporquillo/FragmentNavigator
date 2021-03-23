package com.force.codes.fragmentnavigator

import android.content.Intent
import android.util.Log
import android.util.SparseArray
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.IllegalArgumentException

@Suppress("unused")
class FragmentNavigator private constructor(
    private val owner: LifecycleOwner?,
    private val saveIntent: Intent,
    private val fragmentManager: FragmentManager,
    private val fragmentMap: SparseArray<Fragment>,
    private val animate: Boolean,
    @IdRes private val container: Int,
) : LifecycleObserver {

    private var keyId: Int = Int.MAX_VALUE

    init {
        owner?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        val savedId = saveIntent.extras?.getInt(KEY_ID)
        if (savedId == null) {
            val keyId = FragmentDelegate.createDestinations(fragmentMap)
            setFragmentDestination(keyId)
        } else {
            if (keyId != Int.MAX_VALUE) {
                setFragmentDestination(keyId)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        saveState()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
       saveState()
    }

    fun navigate(keyId: Int) {
        if (keyId != this.keyId) {
            setFragmentDestination(keyId)
            this.keyId = keyId
        }
    }

    private fun saveState() {
        saveIntent.putExtra(KEY_ID, keyId)
    }

    private fun setFragmentDestination(keyId: Int) {
        StackNavigatorHelper.pushKeyId(keyId)
        val fragment = FragmentDelegate.getDelegateByKey(keyId)
        val tag = keyTag(fragment)

        var previous: Fragment?
        var current: Fragment?

        val transaction = fragmentManager.run {
            previous = primaryNavigationFragment
            current = findFragmentByTag(tag)
            beginTransaction()
        }

        if (previous != null) {
            transaction.hide(previous!!)
        }

        if (animate) {
            transaction.setCustomAnimations(
                R.anim.fragment_fade_enter,
                R.anim.fragment_fade_exit
            )
        }

        if (current == null) {
            transaction.add(container, fragment, tag)
            current = fragment
        } else {
            transaction.show(current!!)
        }

        transaction.apply {
            setPrimaryNavigationFragment(current)
            setReorderingAllowed(true)
            commitAllowingStateLoss()
        }
    }

    private fun keyTag(fragment: Fragment): String {
        return "${BuildConfig.LIBRARY_PACKAGE_NAME}: ${fragment::class.java.canonicalName}"
    }

    companion object {
        private const val KEY_ID = "key_id"
    }

    object Builder {
        private lateinit var owner: LifecycleOwner
        private lateinit var manager: FragmentManager
        private lateinit var fragmentMap: SparseArray<Fragment>
        @IdRes
        private var container: Int = 0
        private var animate: Boolean = false

        fun lifecycleOwner(owner: LifecycleOwner): Builder {
            this.owner = owner
            return this
        }

        fun fragmentManger(manager: FragmentManager): Builder {
            this.manager = manager
            return this
        }

        fun fragmentMap(fragmentMap: SparseArray<Fragment>): Builder {
            this.fragmentMap = fragmentMap
            return this
        }

        fun container(@IdRes containerView: Int): Builder {
            this.container = containerView
            container = containerView
            return this
        }

        fun fade(animate: Boolean = false) {
            this.animate = animate
        }

        var containerView: Int = 0

        fun build(saveState: Intent): FragmentNavigator {
            return FragmentNavigator(
                saveIntent = saveState,
                owner = owner,
                fragmentManager = manager,
                fragmentMap = fragmentMap,
                container = container,
                animate = animate
            )
        }
    }
}