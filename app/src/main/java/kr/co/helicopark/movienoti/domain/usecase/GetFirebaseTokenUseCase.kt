package kr.co.helicopark.movienoti.domain.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.helicopark.movienoti.domain.model.Resource

// Firebase 에서 message Token 가져오기
interface GetFirebaseTokenUseCase {
    fun invoke(): Flow<Resource<String>>
}