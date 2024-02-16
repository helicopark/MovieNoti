package kr.co.helicopark.movienoti.ui

import kr.co.helicopark.movienoti.AREA_LIST
import kr.co.helicopark.movienoti.THEATER_LIST
import kr.co.helicopark.movienoti.ui.model.AreaItem
import kr.co.helicopark.movienoti.ui.model.TheaterItem
import org.json.JSONArray
import org.json.JSONObject

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