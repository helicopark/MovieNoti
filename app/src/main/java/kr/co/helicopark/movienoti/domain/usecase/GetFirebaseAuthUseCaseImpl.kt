package kr.co.helicopark.movienoti.domain.usecase

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.data.repository.MovieRepository
import kr.co.helicopark.movienoti.domain.model.Resource
import javax.inject.Inject

class GetFirebaseAuthUseCaseImpl @Inject constructor(private val movieRepository: MovieRepository) : GetFirebaseAuthUseCase {
    override fun invoke(): Flow<Resource<AuthResult>> = movieRepository.getFirebaseAuth()
}