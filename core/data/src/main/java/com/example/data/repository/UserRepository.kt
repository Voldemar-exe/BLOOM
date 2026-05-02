package com.example.data.repository

import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.model.AppSettings
import com.example.model.CustomizationType
import com.example.model.User
import com.example.model.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Singleton
import timber.log.Timber

@Singleton
interface UserRepository {
    val user: Flow<User?>
    val stats: Flow<UserStats>
    val settings: Flow<AppSettings>

    suspend fun updateUser(user: User)

    suspend fun updateStats(stats: UserStats)

    suspend fun updateSettings(settings: AppSettings)

    suspend fun updateUsername(username: String)

    suspend fun updateEmail(email: String)

    suspend fun updatePassword(password: String)

    suspend fun updateCustomization(
        key: String,
        type: CustomizationType,
    )

    suspend fun addAchievement(id: Int)

    suspend fun removeAchievement(id: Int)

    suspend fun addPurchase(
        first: String,
        second: String,
    )

    suspend fun removePurchase(
        first: String,
        second: String,
    )

    suspend fun clearUser()

    suspend fun clearAll()
}

@Singleton
internal class UserRepositoryImpl(private val dataSource: BloomPreferencesDataStore) :
    UserRepository {
    override val user = dataSource.user
    override val stats = dataSource.stats
    override val settings = dataSource.settings

    override suspend fun updateUser(user: User) {
        dataSource.setUser(user)
    }

    override suspend fun updateStats(stats: UserStats) {
        dataSource.setStats(stats)
    }

    override suspend fun updateSettings(settings: AppSettings) {
        dataSource.setSettings(settings)
    }

    override suspend fun updateUsername(username: String) {
        val current = user.first() ?: return
        dataSource.setUser(current.copy(username = username))
    }

    override suspend fun updateEmail(email: String) {
        val current = user.first() ?: return
        dataSource.setUser(current.copy(email = email))
    }

    override suspend fun updatePassword(password: String) {
        // TODO
    }

    override suspend fun updateCustomization(
        key: String,
        type: CustomizationType,
    ) {
        val current = user.first() ?: return
        when (type) {
            CustomizationType.AVATAR -> {
                dataSource.setUser(user = current.copy(avatarKey = key))
            }
            CustomizationType.BACKGROUND -> {
                dataSource.setUser(user = current.copy(backgroundKey = key))
            }
            CustomizationType.COLOR -> {
                dataSource.setUser(user = current.copy(colorKey = key))
            }
            else -> {
                Timber.e("Wrong type for customization")
            }
        }
    }

    override suspend fun addAchievement(id: Int) {
        dataSource.addAchievement(id)
    }

    override suspend fun removeAchievement(id: Int) {
        dataSource.removeAchievement(id)
    }

    override suspend fun addPurchase(
        first: String,
        second: String,
    ) {
        dataSource.addPurchase(first, second)
    }

    override suspend fun removePurchase(
        first: String,
        second: String,
    ) {
        dataSource.removePurchase(first, second)
    }

    override suspend fun clearUser() {
        dataSource.clearUser()
    }

    override suspend fun clearAll() {
        dataSource.clearAll()
    }
}
