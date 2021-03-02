package com.force.codes.fragmentnavigator.listener

import com.google.android.material.bottomnavigation.BottomNavigationView


internal interface OnNavigateBackListener {
    fun isStackEmpty(view: BottomNavigationView): Boolean
}
