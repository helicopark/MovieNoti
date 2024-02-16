package kr.co.helicopark.movienoti.ui.model

data class AdminReservationMovieItem(
    val date: Long,
    val reservationDate: Long,
    val brand: String,
    val movieTitle: String,
    val movieFormat: String,
    val theaterCode: String,
    val areaCode: String,
    var token: String
)
