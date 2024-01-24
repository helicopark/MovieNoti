package kr.co.helicopark.movienoti.presentation.cgv.bottom

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.AREA_LIST
import kr.co.helicopark.movienoti.BUSAN_ULSAN_CODE
import kr.co.helicopark.movienoti.BUSAN_ULSAN_THEATER_LIST
import kr.co.helicopark.movienoti.DAEGU_CODE
import kr.co.helicopark.movienoti.DAEGU_THEATER_LIST
import kr.co.helicopark.movienoti.DAEJEON_CHUNGCHEONG_CODE
import kr.co.helicopark.movienoti.DAEJEON_CHUNGCHEONG_THEATER_LIST
import kr.co.helicopark.movienoti.GANGWON_CODE
import kr.co.helicopark.movienoti.GANGWON_THEATER_LIST
import kr.co.helicopark.movienoti.GWANGJU_JEOLLA_JEJU_CODE
import kr.co.helicopark.movienoti.GWANGJU_JEOLLA_JEJU_THEATER_LIST
import kr.co.helicopark.movienoti.GYEONGGI_CODE
import kr.co.helicopark.movienoti.GYEONGGI_THEATER_LIST
import kr.co.helicopark.movienoti.GYEONGSANG_CODE
import kr.co.helicopark.movienoti.GYEONGSANG_THEATER_LIST
import kr.co.helicopark.movienoti.INCHON_CODE
import kr.co.helicopark.movienoti.INCHON_THEATER_LIST
import kr.co.helicopark.movienoti.MOVIE_INFO
import kr.co.helicopark.movienoti.R
import kr.co.helicopark.movienoti.SEOUL_CODE
import kr.co.helicopark.movienoti.SEOUL_THEATER_LIST
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.databinding.DialogBottomMovieTheaterBinding
import kr.co.helicopark.movienoti.presentation.model.AreaItem
import kr.co.helicopark.movienoti.presentation.model.TheaterItem
import kr.co.helicopark.movienoti.presentation.model.PersonalReservationMovieItem
import org.json.JSONArray
import java.util.Date

@AndroidEntryPoint
class MovieBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomMovieTheaterBinding
    private val viewModel: MovieBottomViewModel by viewModels()

    private var date: Long = 0L
    private var movieName: String = ""
    private var movieFormat = ""
    private var areaCode = "01"
    private var theaterCode = ""

    private val movieBottomAreaAdapter: MovieBottomAreaAdapter by lazy {
        MovieBottomAreaAdapter {
            areaCode = it

            movieBottomAreaAdapter.submitList(loadAreaItem(it))

            when (it) {
                SEOUL_CODE -> movieBottomTheaterAdapter.submitList(loadTheaterItem(SEOUL_THEATER_LIST))
                GYEONGGI_CODE -> movieBottomTheaterAdapter.submitList(loadTheaterItem(GYEONGGI_THEATER_LIST))
                INCHON_CODE -> movieBottomTheaterAdapter.submitList(loadTheaterItem(INCHON_THEATER_LIST))
                GANGWON_CODE -> movieBottomTheaterAdapter.submitList(loadTheaterItem(GANGWON_THEATER_LIST))
                DAEJEON_CHUNGCHEONG_CODE -> movieBottomTheaterAdapter.submitList(loadTheaterItem(DAEJEON_CHUNGCHEONG_THEATER_LIST))
                DAEGU_CODE -> movieBottomTheaterAdapter.submitList(loadTheaterItem(DAEGU_THEATER_LIST))
                BUSAN_ULSAN_CODE -> movieBottomTheaterAdapter.submitList(loadTheaterItem(BUSAN_ULSAN_THEATER_LIST))
                GYEONGSANG_CODE -> movieBottomTheaterAdapter.submitList(loadTheaterItem(GYEONGSANG_THEATER_LIST))
                GWANGJU_JEOLLA_JEJU_CODE -> movieBottomTheaterAdapter.submitList(loadTheaterItem(GWANGJU_JEOLLA_JEJU_THEATER_LIST))
            }
        }
    }

    private val movieBottomTheaterAdapter: MovieBottomTheaterAdapter by lazy {
        MovieBottomTheaterAdapter { item ->
//            viewModel.setTheaterCode(it)
            theaterCode = item.code

            AlertDialog.Builder(requireContext()).apply {
                setTitle(getString(R.string.dialog_movie_info_title, item.name))

                val movieInfoJsonArray = JSONArray(MOVIE_INFO)
                val movieInfoItems: Array<CharSequence> = Array(movieInfoJsonArray.length()) {
                    movieInfoJsonArray.getJSONObject(it).getString("movieInfo")
                }
                movieFormat = movieInfoItems[0].toString()
                setSingleChoiceItems(movieInfoItems, 0) { _, b ->
                    movieFormat = movieInfoItems[b].toString()
                }

                setPositiveButton(R.string.dialog_ok) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    dismiss()
                    date = Date().time

                    AlertDialog.Builder(requireContext()).apply {
                        setMessage("name = ${movieName}, info = ${movieFormat}, date = ${date}, theater = ${theaterCode},area = ${areaCode}")
                        setPositiveButton("확인", null)
                        show()
                    }

                    val adminReservationMovieInfo = hashMapOf(
                        "date" to date,
                        "brand" to "CGV",
                        "movieName" to movieName,
                        "movieFormat" to movieFormat,
                        "theaterCode" to theaterCode,
                        "areaCode" to areaCode,
                        "token" to "token"
                    )

                    val personalReservationMovieInfo = hashMapOf(
                        date.toString() to PersonalReservationMovieItem(date, "CGV", movieName, movieFormat, theaterCode, areaCode)
                    )

                    viewModel.setAdminReservationMovie(adminReservationMovieInfo)
                    viewModel.setPersonalReservationMovieList(personalReservationMovieInfo)
                }

                setNegativeButton(R.string.dialog_cancel) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogBottomMovieTheaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapterSetting()

        movieName = arguments?.getString("movieName") ?: ""

        binding.tvBottomTitle.text = Html.fromHtml(
            String.format(getString(R.string.bottom_sheet_format_title), arguments?.getString("movieName")),
            Html.FROM_HTML_MODE_COMPACT
        )

        binding.ivBottomClose.setOnClickListener {
            this.dismiss()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setPersonalReservationMovieState.collectLatest {
                when (it.state) {
                    UiStatus.LOADING -> {

                    }

                    UiStatus.SUCCESS -> {
                        Toast.makeText(requireContext(), it.data, Toast.LENGTH_LONG).show()
                    }

                    UiStatus.ERROR -> {

                    }

                    else -> {

                    }
                }
            }

            viewModel.setAdminReservationMovieState.collectLatest {
                when (it.state) {
                    UiStatus.LOADING -> {

                    }

                    UiStatus.SUCCESS -> {
                        Toast.makeText(requireContext(), it.data, Toast.LENGTH_LONG).show()
                    }

                    UiStatus.ERROR -> {

                    }

                    else -> {

                    }
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }

        return dialog
    }

    private fun initAdapterSetting() {
        val areaDividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        areaDividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bottom_area_decoration)!!)

        binding.rvBottomArea.adapter = movieBottomAreaAdapter
        binding.rvBottomArea.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBottomArea.addItemDecoration(areaDividerItemDecoration)
        movieBottomAreaAdapter.submitList(loadAreaItem("01"))

        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bottom_theater_decoration)!!)

        binding.rvBottomTheater.adapter = movieBottomTheaterAdapter
        binding.rvBottomTheater.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBottomTheater.addItemDecoration(dividerItemDecoration)
        movieBottomTheaterAdapter.submitList(loadTheaterItem(SEOUL_THEATER_LIST))
    }

    private fun loadAreaItem(selectedCode: String): ArrayList<AreaItem> {
        val areaJsonArray = JSONArray(AREA_LIST)
        val areaItemList = ArrayList<AreaItem>()
        (0 until areaJsonArray.length()).forEach {
            val item = areaJsonArray.getJSONObject(it)
            areaItemList.add(AreaItem(item.getString("areaCode"), item.getString("areaName"), item.getString("areaCode") == selectedCode))
        }

        return areaItemList
    }

    private fun loadTheaterItem(theaterList: String): ArrayList<TheaterItem> {
        val theaterJsonArray = JSONArray(theaterList)
        val theaterItemList = ArrayList<TheaterItem>()

        (0 until theaterJsonArray.length()).forEach {
            val item = theaterJsonArray.getJSONObject(it)
            theaterItemList.add(TheaterItem(item.getString("theaterCode"), item.getString("theaterName")))
        }

        return theaterItemList
    }
}