package kr.co.helicopark.movienoti.presentation.reservation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservationItem(
    val reservationDate: Long,
    val brand: String,    // CGV, LOTTE, MEGABOX
    val movieTitle: String,
    val movieFormat: String,   // 4DX, SCREENX, IMAX ...
    val theaterCode: String,   // 0001, 0110 ...
    val areaCode: String
) : Parcelable
