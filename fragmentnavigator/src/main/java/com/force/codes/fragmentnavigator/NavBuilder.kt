package com.force.codes.fragmentnavigator

import android.content.Intent

inline fun navBuilder(
    saveIntent: Intent,
    body: FragmentNavigator.Builder.() -> Unit
): FragmentNavigator {
    val builder = FragmentNavigator.Builder
    builder.body()
    return builder.build(saveIntent)
}