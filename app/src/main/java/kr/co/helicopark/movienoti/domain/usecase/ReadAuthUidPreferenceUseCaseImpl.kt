package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.DataStoreRepository
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

class ReadAuthUidPreferenceUseCaseImpl @Inject constructor(private val dataStoreRepository: DataStoreRepository) : ReadAuthUidPreferenceUseCase {
    override suspend fun invoke(): Flow<Resource<String>> = dataStoreRepository.readFirebaseAuthUid()
}