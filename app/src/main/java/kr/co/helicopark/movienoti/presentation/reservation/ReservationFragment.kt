package kr.co.helicopark.movienoti.presentation.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.databinding.FragmentReservationBinding

@AndroidEntryPoint
class ReservationFragment : Fragment() {
    private lateinit var binding: FragmentReservationBinding
    private val viewModel: ReservationViewModel by viewModels()

    private val adapter:ReservationListAdapter by lazy {
        ReservationListAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initReservationMovieList()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                when (it.state) {
                    UiStatus.LOADING -> {
                        binding.progressReservation.visibility = View.VISIBLE
                    }

                    UiStatus.SUCCESS -> {
                        adapter.submitList(it.data)
                        binding.progressReservation.visibility = View.GONE
                    }

                    UiStatus.ERROR -> {
                        binding.progressReservation.visibility = View.GONE
                    }

                    else -> {
                        binding.progressReservation.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initReservationMovieList() {
        binding.rvReservation.itemAnimator = DefaultItemAnimator()
        binding.rvReservation.adapter = adapter

        //예약 영화 목록 불러오기
        viewModel.getReservationMovieList()
    }
}