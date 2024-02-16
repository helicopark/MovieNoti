package kr.co.helicopark.movienoti.domain.usecase

import kr.co.helicopark.movienoti.data.repository.DataStoreRepository
import javax.inject.Inject

// DataStore에 저장한 Token 가져오기
class ReadPreferenceTokenUseCaseImpl @Inject constructor(private val dataStoreRepository: DataStoreRepository) : ReadPreferenceTokenUseCase {
    override suspend fun invoke(): String = dataStoreRepository.readFirebaseMessageToken()
}