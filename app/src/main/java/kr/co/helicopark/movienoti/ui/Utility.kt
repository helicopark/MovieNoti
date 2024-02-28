package kr.co.helicopark.movienoti.ui

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.Builder
import kr.co.helicopark.movienoti.AREA_LIST
import kr.co.helicopark.movienoti.THEATER_LIST
import kr.co.helicopark.movienoti.ui.model.AreaItem
import kr.co.helicopark.movienoti.ui.model.TheaterItem
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

fun getAreaName(code: String): String? {
    val areaJsonArray = JSONArray(AREA_LIST)
    val areaItemList = ArrayList<AreaItem>()
    (0 until areaJsonArray.length()).forEach {
        val item = areaJsonArray.getJSONObject(it)
        areaItemList.add(AreaItem(item.getString("areaCode"), item.getString("areaName"), true))
    }

    val item = areaItemList.find {
        it.code == code
    }

    return item?.name
}

fun getTheaterName(areaCode: String, theaterCode: String): String? {
    val theaterJsonObject = JSONObject(THEATER_LIST)
    val theaterJsonArray = theaterJsonObject.getJSONArray(areaCode)
    val theaterItemList = ArrayList<TheaterItem>()
    (0 until theaterJsonArray.length()).forEach {
        val item = theaterJsonArray.getJSONObject(it)
        theaterItemList.add(TheaterItem(item.getString("theaterCode"), item.getString("theaterName")))
    }

    val item = theaterItemList.find {
        it.code == theaterCode
    }

    return item?.name
}

fun datePicker(
    movieTitle: String,
): MaterialDatePicker<Long> {
    //오늘부터 다음달까지 일자 선택 제한
    val constraintsBuilderRange = CalendarConstraints.Builder()
    val calendarStart: Calendar = Calendar.getInstance()
    val calendarEnd: Calendar = Calendar.getInstance()
    calendarStart.add(Calendar.DATE, -1)
    calendarEnd.add(Calendar.MONTH, 1)

    constraintsBuilderRange.setStart(calendarStart.timeInMillis)
    constraintsBuilderRange.setEnd(calendarEnd.timeInMillis)

    val listValidators = ArrayList<CalendarConstraints.DateValidator>()
    listValidators.add(DateValidatorPointForward.from(calendarStart.timeInMillis))
    listValidators.add(DateValidatorPointBackward.before(calendarEnd.timeInMillis))
    val validators = CompositeDateValidator.allOf(listValidators)
    constraintsBuilderRange.setValidator(validators)

    val datePickerBuilder = Builder.datePicker()
    datePickerBuilder.setTitleText(movieTitle)
    datePickerBuilder.setCalendarConstraints(constraintsBuilderRange.build())

    return datePickerBuilder.build()
}
