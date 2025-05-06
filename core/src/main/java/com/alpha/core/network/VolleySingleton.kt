package com.alpha.core.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton private constructor(context: Context) {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: VolleySingleton(context).also { INSTANCE = it }
        }
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue.add(request)
    }
}
