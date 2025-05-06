package com.alpha.features.manga.data

import android.content.Context
import android.util.Log
import com.alpha.core.data.database.Manga
import com.alpha.core.network.VolleySingleton
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest

enum class FetchType {
    ALL, ID
}

fun fetchMangaData(
    context: Context,
    fetchType: FetchType,
    mangaId: String? = null,
    onDataFetched: (List<Manga>) -> Unit
) {
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
        "X-RapidAPI-Key" to "8aa74045fbmsh26980e26de39671p13b5b5jsn7b4ce572fb4e" // Replace with your actual API key
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

    // Add the request to the request queue
    VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
}
