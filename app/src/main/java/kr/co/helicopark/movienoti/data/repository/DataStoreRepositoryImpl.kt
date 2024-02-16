package kr.co.helicopark.movienoti.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class DataStoreRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : DataStoreRepository {
    override suspend fun saveFirebaseAuthUid(uid: String) {
        dataStore.edit {
            it[stringPreferencesKey("firebaseAuthUid")] = uid
        }
    }

    override suspend fun readFirebaseAuthUid(): String = dataStore.data.first()[stringPreferencesKey("firebaseAuthUid")] ?: ""

    override suspend fun saveFirebaseMessageToken(token: String) {
        dataStore.edit {
            it[stringPreferencesKey("firebaseMessageToken")] = token
        }
    }

    override suspend fun readFirebaseMessageToken() = dataStore.data.first()[stringPreferencesKey("firebaseMessageToken")] ?: ""
}