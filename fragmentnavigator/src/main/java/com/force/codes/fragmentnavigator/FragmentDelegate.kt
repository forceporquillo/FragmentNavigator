package com.force.codes.fragmentnavigator

import android.util.SparseArray
import androidx.core.util.containsKey
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

object FragmentDelegate {

    private val fragmentMap = SparseArray<Fragment>()

    private fun init(map: SparseArray<Fragment>): Int {
        var firstIndex = 0
        for (i in 0 until map.size()) {
            if (i == 0) {
                firstIndex = map.keyAt(i)
            }
            fragmentMap.put(map.keyAt(i), map.valueAt(i))
        }

        return firstIndex
    }

    fun createDestinations(map: SparseArray<Fragment>?): Int {
        return init(requireNotNull(map))
    }

    fun isAdded(key: Int) = fragmentMap.containsKey(key)

    fun getDelegateByKey(keyId: Int): Fragment = fragmentMap[keyId]
}

