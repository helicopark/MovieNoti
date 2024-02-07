package kr.co.helicopark.movienoti.domain.model

data class PersonalReservationMovie(
    val date: Long,
    val reservationDate: Long,
    val brand: String,    // CGV, LOTTE, MEGABOX
    val movieName: String,
    val movieFormat: String,   // 4DX, SCREENX, IMAX ...
    val areaCode: String,
    val theaterCode: String,   // 0001, 0110 ...
    val thumb: String
)