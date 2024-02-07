package kr.co.helicopark.movienoti.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.domain.model.UiStatus
import javax.inject.Inject


class DataStoreRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : DataStoreRepository {
    override suspend fun saveFirebaseAuthUid(uid: String) {
        dataStore.edit {
            it[stringPreferencesKey("firebaseAuthUid")] = uid
        }
    }

    override suspend fun readFirebaseAuthUid() = flow {
        emit(Resource.Loading(UiStatus.LOADING))
        val data = dataStore.data.first()
        emit(Resource.Success(data[stringPreferencesKey("firebaseAuthUid")] ?: "", UiStatus.SUCCESS))
    }

    override suspend fun saveFirebaseMessageToken(token: String) {
        dataStore.edit {
            it[stringPreferencesKey("firebaseMessageToken")] = token
        }
    }

    override suspend fun readFirebaseMessageToken() = flow {
        emit(Resource.Loading(UiStatus.LOADING))
        val data = dataStore.data.first()
        emit(Resource.Success(data[stringPreferencesKey("firebaseMessageToken")] ?: "", UiStatus.SUCCESS))
    }
}