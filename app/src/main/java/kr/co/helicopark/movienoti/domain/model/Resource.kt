package kr.co.helicopark.movienoti.domain.model

sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null,
    var state: UiStatus? = null
) {
    class Success<T>(data: T, state: UiStatus?) : Resource<T>(data, null, state)
    class Error(message: String?, state: UiStatus?) : Resource<Nothing>(null, message, state)
    class Loading(state: UiStatus) : Resource<Nothing>(null, null, state)
}