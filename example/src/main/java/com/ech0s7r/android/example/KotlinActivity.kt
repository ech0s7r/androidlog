package com.ech0s7r.android.example

import android.app.Activity
import android.os.Bundle
import android.util.Log

/**
 * @author ech0s7r
 */
class KotlinActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.w("TAG", "lint check should fail here!")
    }
}