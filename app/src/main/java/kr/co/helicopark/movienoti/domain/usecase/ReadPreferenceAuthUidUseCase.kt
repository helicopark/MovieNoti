package kr.co.helicopark.movienoti.domain.usecase

// DataStore에 저장한 Auth Uid 가져오기
interface ReadPreferenceAuthUidUseCase {
    suspend fun invoke(): String
}