package com.example.datastore.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.example.datastore.util.toDomain
import com.example.datastore.util.toProto
import com.example.model.AppSettings
import com.example.model.User
import com.example.model.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import proto.UserPreferences
import proto.UserProto
import proto.copy
import timber.log.Timber

class BloomPreferencesDataStore(private val preferences: DataStore<UserPreferences>) {
    val user: Flow<User?> =
        preferences.data.map { prefs ->
            if (prefs.hasUser()) prefs.user.toDomain() else null
        }

    val stats: Flow<UserStats?> =
        preferences.data.map { prefs ->
            if (prefs.hasStats()) prefs.stats.toDomain() else null
        }

    val settings: Flow<AppSettings> =
        preferences.data.map { prefs ->
            if (prefs.hasSettings()) prefs.settings.toDomain() else AppSettings.default()
        }

    suspend fun setUser(user: User) {
        try {
            preferences.updateData { current ->
                current.copy {
                    this.user = user.toProto()
                }
            }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Failed to update user")
        }
    }

    suspend fun clearUser() {
        try {
            preferences.updateData { current ->
                current.copy {
                    clearUser()
                }
            }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Failed to clear user")
        }
    }

    suspend fun setStats(stats: UserStats) {
        try {
            preferences.updateData { current ->
                current.copy {
                    this.stats = stats.toProto()
                }
            }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Failed to update stats")
        }
    }

    suspend fun setSettings(settings: AppSettings) {
        try {
            preferences.updateData { current ->
                current.copy {
                    this.settings = settings.toProto()
                }
            }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Failed to update settings")
        }
    }

    suspend fun addAchievement(id: Int) {
        try {
            preferences.updateData { current ->
                current.copy {
                    val existing = if (hasUser()) user else UserProto.getDefaultInstance()
                    user =
                        existing.copy {
                            val set = achievements.toMutableSet()
                            set.add(id)
                            achievements.clear()
                            achievements.addAll(set)
                        }
                }
            }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Failed to add achievement")
        }
    }

    suspend fun removeAchievement(id: Int) {
        try {
            preferences.updateData { current ->
                current.copy {
                    if (!hasUser()) return@copy
                    user =
                        user.copy {
                            val set = achievements.toMutableSet()
                            set.remove(id)
                            achievements.clear()
                            achievements.addAll(set)
                        }
                }
            }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Failed to remove achievement")
        }
    }

    suspend fun addPurchase(
        first: String,
        second: String,
    ) {
        try {
            preferences.updateData { current ->
                current.copy {
                    val existing = if (hasUser()) user else UserProto.getDefaultInstance()
                    user =
                        existing.copy {
                            purchases.add(
                                UserProto.Purchase
                                    .newBuilder()
                                    .setFirst(first)
                                    .setSecond(second)
                                    .build(),
                            )
                        }
                }
            }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Failed to add purchase")
        }
    }

    suspend fun removePurchase(
        first: String,
        second: String,
    ) {
        try {
            preferences.updateData { current ->
                current.copy {
                    if (!hasUser()) return@copy
                    user =
                        user.copy {
                            val filtered =
                                purchases.filterNot { it.first == first && it.second == second }
                            purchases.clear()
                            purchases.addAll(filtered)
                        }
                }
            }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Failed to remove purchase")
        }
    }

    suspend fun clearAll() {
        try {
            preferences.updateData {
                UserPreferences.getDefaultInstance()
            }
        } catch (ioException: IOException) {
            Timber.e(ioException, "Failed to clear all preferences")
        }
    }
}
