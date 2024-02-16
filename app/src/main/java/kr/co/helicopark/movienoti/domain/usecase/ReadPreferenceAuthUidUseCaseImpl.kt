package kr.co.helicopark.movienoti.domain.usecase

import kr.co.helicopark.movienoti.data.repository.DataStoreRepository
import javax.inject.Inject

// DataStore에 저장한 Auth Uid 가져오기
class ReadPreferenceAuthUidUseCaseImpl @Inject constructor(private val dataStoreRepository: DataStoreRepository) : ReadPreferenceAuthUidUseCase {
    override suspend fun invoke(): String = dataStoreRepository.readFirebaseAuthUid()
}