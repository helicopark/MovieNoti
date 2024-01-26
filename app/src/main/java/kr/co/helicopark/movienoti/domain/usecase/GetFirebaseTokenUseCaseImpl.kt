package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

class GetFirebaseTokenUseCaseImpl @Inject constructor(private val movieRepository: MovieRepository) : GetFirebaseTokenUseCase {
    override fun invoke(): Flow<Resource<String>> = movieRepository.getFirebaseToken()
}