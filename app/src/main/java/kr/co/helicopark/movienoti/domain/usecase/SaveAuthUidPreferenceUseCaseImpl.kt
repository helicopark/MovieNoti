package kr.co.helicopark.movienoti.domain.usecase

import kr.co.helicopark.movienoti.data.repository.DataStoreRepository
import javax.inject.Inject

// DataStore에 uid 저장하기
class SaveAuthUidPreferenceUseCaseImpl @Inject constructor(private val dataStoreRepository: DataStoreRepository) : SaveAuthUidPreferenceUseCase {
    override suspend fun invoke(authUid: String) {
        dataStoreRepository.saveFirebaseAuthUid(authUid)
    }
}