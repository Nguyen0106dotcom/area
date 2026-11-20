package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("planning_prefs", Context.MODE_PRIVATE)
    
    private val _favoriteIds = MutableStateFlow<Set<String>>(loadFavorites())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private fun loadFavorites(): Set<String> {
        return prefs.getStringSet("favorite_ids", emptySet()) ?: emptySet()
    }

    fun toggleFavorite(id: String) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        prefs.edit().putStringSet("favorite_ids", current).apply()
        _favoriteIds.value = current
    }

    fun isFavorite(id: String): Boolean {
        return _favoriteIds.value.contains(id)
    }
}
