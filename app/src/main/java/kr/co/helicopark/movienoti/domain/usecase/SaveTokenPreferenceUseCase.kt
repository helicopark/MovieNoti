package kr.co.helicopark.movienoti.domain.usecase

// DataStore에 token 저장하기
interface SaveTokenPreferenceUseCase {
    suspend fun invoke(token: String)
}