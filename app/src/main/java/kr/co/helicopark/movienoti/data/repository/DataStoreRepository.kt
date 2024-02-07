package kr.co.helicopark.movienoti.data.repository

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource

interface DataStoreRepository {
    suspend fun saveFirebaseAuthUid(uid : String)

    suspend fun readFirebaseAuthUid() : Flow<Resource<String>>

    suspend fun saveFirebaseMessageToken(token : String)

    suspend fun readFirebaseMessageToken() : Flow<Resource<String>>
}