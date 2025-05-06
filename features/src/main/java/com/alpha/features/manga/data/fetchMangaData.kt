package com.alpha.features.manga.data

import android.content.Context
import android.util.Log
import com.alpha.core.data.database.Manga
import com.alpha.core.network.VolleySingleton
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class FetchType {
    ALL, ID
}

private const val MAX_REQUESTS = 99

fun fetchMangaData(
    context: Context,
    fetchType: FetchType,
    mangaId: String? = null,
    onDataFetched: (List<Manga>) -> Unit
) {
    if (!canMakeApiRequest(context, MAX_REQUESTS)) {
        Log.e("MangaData", "Daily API request limit reached ($MAX_REQUESTS)")
        return
    }


    val url = when (fetchType) {
        FetchType.ALL -> "https://mangaverse-api.p.rapidapi.com/manga/fetch?type=all"
        FetchType.ID -> {
            // Construct URL with the provided mangaId
            if (mangaId.isNullOrEmpty()) {
                Log.e("MangaData", "mangaId must be provided for FetchType.ID")
                return
            }
            "https://mangaverse-api.p.rapidapi.com/manga?id=$mangaId"
        }
    }

    val headers = mapOf(
        "X-RapidAPI-Host" to "mangaverse-api.p.rapidapi.com",
        "X-RapidAPI-Key" to "43aab1fdffmsh0b0acc0b3d4b337p13172ejsn9b0b4a178c66" // Replace with your actual API key
    )

    val jsonObjectRequest =
        object : JsonObjectRequest(Method.GET, url, null, Response.Listener { response ->
            try {
                val mangaList = mutableListOf<Manga>()
                if (fetchType == FetchType.ALL) {
                    // Parse the "fetch all" response
                    val data = response.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val manga = data.getJSONObject(i)
                        val id = manga.getString("id")
                        val title = manga.getString("title")
                        val subTitle = manga.getString("sub_title")
                        val thumb = manga.getString("thumb")
                        val summary = manga.getString("summary")
                        val genresArray = manga.getJSONArray("genres")
                        val genres = mutableListOf<String>()
                        for (j in 0 until genresArray.length()) {
                            genres.add(genresArray.getString(j))
                        }
                        mangaList.add(Manga(id, title, subTitle, thumb, summary, genres))
                    }
                } else if (fetchType == FetchType.ID) {
                    // Parse the "get by ID" response
                    val manga = response.getJSONObject("data")
                    val id = manga.getString("id")
                    val title = manga.getString("title")
                    val subTitle = manga.getString("sub_title")
                    val thumb = manga.getString("thumb")
                    val summary = manga.getString("summary")
                    val genresArray = manga.getJSONArray("genres")
                    val genres = mutableListOf<String>()
                    for (j in 0 until genresArray.length()) {
                        genres.add(genresArray.getString(j))
                    }
                    mangaList.add(Manga(id, title, subTitle, thumb, summary, genres))
                }

                onDataFetched(mangaList)
            } catch (e: Exception) {
                Log.e("MangaData", "Error parsing response", e)
            }
        }, Response.ErrorListener { error: VolleyError ->
            Log.e("MangaData", "Error fetching data", error)
        }) {
            override fun getHeaders(): Map<String, String> {
                return headers
            }
        }

    // Set retry policy in case of request failure or timeouts
    jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    )

    VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
}


fun canMakeApiRequest(
    context: Context, maxRequestsPerDay: Int = 20
): Boolean {
    // Get the current logged-in user
    val userId = "default_user"// for simplicity using default user

    val prefs = context.getSharedPreferences("api_prefs", Context.MODE_PRIVATE)
    val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

    val savedDate = prefs.getString("last_request_date", null)
    val currentCount = prefs.getInt("request_count", 0)

    val newCount: Int
    if (savedDate == currentDate) {
        if (currentCount >= maxRequestsPerDay) return false
        newCount = currentCount + 1
    } else {
        newCount = 1
    }

    // Save locally
    prefs.edit().putString("last_request_date", currentDate).putInt("request_count", newCount)
        .apply()

    // Update Firebase
    updateRequestCountInFirebase(userId, currentDate, newCount)

    return true
}

private fun updateRequestCountInFirebase(userId: String, date: String, count: Int) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("api_requests/$userId/$date")
    ref.setValue(count)
}
