package kr.co.helicopark.movienoti.domain.usecase

import kr.co.helicopark.movienoti.data.repository.DataStoreRepository
import javax.inject.Inject

// DataStore에 token 저장하기
class SaveTokenPreferenceUseCaseImpl @Inject constructor(private val dataStoreRepository: DataStoreRepository) : SaveTokenPreferenceUseCase {
    override suspend fun invoke(token: String) {
        dataStoreRepository.saveFirebaseMessageToken(token)
    }
}