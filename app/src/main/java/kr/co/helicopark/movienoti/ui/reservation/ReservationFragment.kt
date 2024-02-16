package kr.co.helicopark.movienoti.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.R
import kr.co.helicopark.movienoti.databinding.FragmentReservationBinding
import kr.co.helicopark.movienoti.domain.model.UiStatus

@AndroidEntryPoint
class ReservationFragment : Fragment() {
    private lateinit var binding: FragmentReservationBinding
    private val viewModel: ReservationViewModel by viewModels()

    private val adapter: ReservationAdapter by lazy {
        ReservationAdapter {
            AlertDialog.Builder(requireContext()).apply {
                setPositiveButton(R.string.dialog_ok) { _, _ ->

                }

                setNegativeButton(R.string.dialog_cancel) { _, _ ->

                }

                setNeutralButton(R.string.dialog_delete) { _, _ ->

                }
            }.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReservationBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.adapter = adapter
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.initReservationMovieList()
        }
    }
}