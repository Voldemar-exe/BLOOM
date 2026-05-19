package com.example.data.repository

import com.example.data.util.UserGarbageCollector
import com.example.datastore.datastore.BloomPreferencesDataStore
import com.example.model.AppSettings
import com.example.model.CustomizationItem
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
        key: String,
        type: CustomizationType,
        price: Int,
    )

    suspend fun clearUser()

    suspend fun clearAll()

    suspend fun deleteAccount(): Result<Boolean>
}

@Singleton
internal class UserRepositoryImpl(
    private val dataStore: BloomPreferencesDataStore,
    private val authRepository: AuthRepository,
    private val garbageCollector: UserGarbageCollector,
) : UserRepository {
    override val user = dataStore.user
    override val stats = dataStore.stats
    override val settings = dataStore.settings

    override suspend fun updateUser(user: User) {
        dataStore.setUser(user)
    }

    override suspend fun updateStats(stats: UserStats) {
        dataStore.setStats(stats)
    }

    override suspend fun updateSettings(settings: AppSettings) {
        dataStore.setSettings(settings)
    }

    override suspend fun updateUsername(username: String) {
        val current = user.first() ?: return
        dataStore.setUser(current.copy(username = username))
    }

    override suspend fun updateEmail(email: String) {
        val current = user.first() ?: return
        dataStore.setUser(current.copy(email = email))
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
                dataStore.setUser(user = current.copy(avatarKey = key))
            }

            CustomizationType.BACKGROUND -> {
                dataStore.setUser(user = current.copy(backgroundKey = key))
            }

            CustomizationType.COLOR -> {
                dataStore.setUser(user = current.copy(colorKey = key))
            }

            else -> {
                Timber.e("Wrong type for customization")
            }
        }
    }

    override suspend fun addAchievement(id: Int) {
        dataStore.addAchievement(id)
    }

    override suspend fun removeAchievement(id: Int) {
        dataStore.removeAchievement(id)
    }

    override suspend fun addPurchase(
        key: String,
        type: CustomizationType,
        price: Int,
    ) {
        val currentUser = user.first() ?: return
        val currentStats = stats.first()

        dataStore.addPurchase(key, type.name)
        dataStore.setStats(
            currentStats.copy(
                currentCoinsAmount =
                    currentStats.currentCoinsAmount - price,
            ),
        )
        dataStore.setUser(
            currentUser.copy(
                ownedItems =
                    currentUser.ownedItems +
                        CustomizationItem(
                            key,
                            type,
                        ),
            ),
        )
    }

    override suspend fun clearUser() {
        dataStore.clearUser()
        dataStore.clearToken()
    }

    override suspend fun clearAll() {
        garbageCollector.clearAll()
    }

    override suspend fun deleteAccount() = authRepository.deleteAccount()
}
