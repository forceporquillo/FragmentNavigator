package com.force.codes.fragmentnavigator

import com.force.codes.fragmentnavigator.listener.OnNavigateBackListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

object StackNavigatorHelper : OnNavigateBackListener {

    private val fragmentIds = Stack<Int>()
    private var primaryId: Int = 0

    fun pushKeyId(keyId: Int) {
        val duplicates = mutableListOf<Int>()

        if (fragmentIds.isEmpty()) {
            checkKeyId(keyId)
            primaryId = keyId
            return
        }

        fragmentIds.let { stackId ->
            if (stackId.contains(keyId)) {
                stackId.forEachIndexed { index, id ->
                    if (id == keyId) {
                        duplicates.add(index)
                    }
                }
            }
        }

        if (duplicates.isNotEmpty()) {
            removeDuplicates(duplicates)
        }

        var mutableId = keyId
        var lastKeyId: Int? = null

        try {
            lastKeyId = fragmentIds.run {
                lastElement() ?: peek()
            }
        } catch (_: NoSuchElementException) {
            mutableId = FragmentNavigator.Builder.containerView
        } finally {
            if (lastKeyId != mutableId) {
                checkKeyId(mutableId)
            }
        }
    }

    private fun checkKeyId(keyId: Int) {
        if (FragmentDelegate.isAdded(keyId)) {
            fragmentIds.push(keyId)
        }
    }

    private fun popKeyId(): Int = fragmentIds.run {
        return if (size > 1) {
            return try {
                removeAt(size - 1)
                pop()
            } catch (e: EmptyStackException) {
                FragmentNavigator.Builder.containerView
            }
        } else FragmentNavigator.Builder.containerView
    }

    private fun removeDuplicates(index: List<Int>) {
        index.forEach { fragmentIds.removeAt(it) }
    }

    override fun isStackEmpty(view: BottomNavigationView): Boolean {
        return try {
            if (fragmentIds.peek() != primaryId) {
                view.selectedItemId = popKeyId()
                false
            } else true
        } catch (_: EmptyStackException) {
            true
        }
    }
}
