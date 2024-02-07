package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.DataStoreRepository
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

class ReadTokenPreferenceUseCaseImpl @Inject constructor(private val dataStoreRepository: DataStoreRepository) : ReadTokenPreferenceUseCase {
    override suspend fun invoke(): Flow<Resource<String>> = dataStoreRepository.readFirebaseMessageToken()
}