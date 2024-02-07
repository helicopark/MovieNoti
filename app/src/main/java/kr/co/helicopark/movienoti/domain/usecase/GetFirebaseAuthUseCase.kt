package kr.co.helicopark.movienoti.domain.usecase

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource

interface GetFirebaseAuthUseCase {
    fun invoke(): Flow<Resource<AuthResult>>
}