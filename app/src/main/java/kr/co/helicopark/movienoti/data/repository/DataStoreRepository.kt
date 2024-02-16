package kr.co.helicopark.movienoti.data.repository

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource

interface DataStoreRepository {
    suspend fun saveFirebaseAuthUid(authUid: String)

    suspend fun readFirebaseAuthUid(): String

    suspend fun saveFirebaseMessageToken(token: String)

    suspend fun readFirebaseMessageToken(): String
}