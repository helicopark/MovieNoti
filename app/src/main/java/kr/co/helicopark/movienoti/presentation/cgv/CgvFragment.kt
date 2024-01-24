package kr.co.helicopark.movienoti.presentation.cgv

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.R
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.databinding.FragmentCgvBinding
import kr.co.helicopark.movienoti.presentation.MainActivity
import kr.co.helicopark.movienoti.presentation.cgv.bottom.MovieBottomFragment
import kr.co.helicopark.movienoti.presentation.model.CgvMovieItem

@AndroidEntryPoint
class CgvFragment : Fragment() {
    private lateinit var binding: FragmentCgvBinding
    private val viewModel: CgvViewModel by viewModels()

    private val itemList = ArrayList<CgvMovieItem>()

    private val adapter by lazy {
        CgvMovieListAdapter { movieListItem ->
            MovieBottomFragment().let { fragment ->
                Bundle().let {
                    it.putString("movieName", movieListItem.title)
                    fragment.arguments = it
                }
                fragment.show(requireActivity().supportFragmentManager, "MovieBottomFragment")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCgvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        binding.tvListAbcOrder.setOnClickListener {

            Firebase.messaging.token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    // Log and toast
                    Log.e(MainActivity::class.java.simpleName, "Firebase.messaging.token.addOnCompleteListener: $token")
                },
            )
        }

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == adapter.currentList.size - 1) {
                    lifecycleScope.launch {
                        viewModel.fetchCgvMoreMovieList().collectLatest {
                            when (it.state) {
                                UiStatus.SUCCESS -> {
                                    val list = ArrayList(itemList)
                                    it.data?.forEach { cgvMoreMovieItem ->
                                        list.add(
                                            CgvMovieItem(
                                                cgvMoreMovieItem.thumb,
                                                cgvMoreMovieItem.title,
                                                String.format(getString(R.string.format_reservation_rate), cgvMoreMovieItem.reservationRate),
                                                if (cgvMoreMovieItem.dDay.contains("-")) {
                                                    String.format(getString(R.string.format_released_date), cgvMoreMovieItem.releasedDate, "")
                                                } else {
                                                    String.format(
                                                        getString(R.string.format_released_date),
                                                        cgvMoreMovieItem.releasedDate,
                                                        "D-${cgvMoreMovieItem.dDay}"
                                                    )
                                                }
                                            )
                                        )
                                    }

                                    adapter.submitList(list)
                                    binding.progressCgv.visibility = View.GONE
                                }

                                UiStatus.ERROR -> {
                                    binding.progressCgv.visibility = View.GONE
                                }

                                UiStatus.LOADING -> {
                                    binding.progressCgv.visibility = View.VISIBLE
                                }

                                else -> {
                                    binding.progressCgv.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }
        })

        lifecycleScope.launch {
            viewModel.fetchCgvMovieList().collectLatest {
                when (it.state) {
                    UiStatus.SUCCESS -> {
                        it.data?.forEach { cgvMovieItem ->
                            itemList.add(cgvMovieItem)
                        }

                        adapter.submitList(itemList)
                        binding.progressCgv.visibility = View.GONE
                    }

                    UiStatus.ERROR -> {
                        binding.progressCgv.visibility = View.GONE
                    }

                    UiStatus.LOADING -> {
                        binding.progressCgv.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.progressCgv.visibility = View.GONE
                    }
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO)  {
//            launch {
//                viewModel.cgvMovieList.collect {
//                    when (it.state) {
//                        UiStatus.LOADING -> {
//                            binding.progressCgv.visibility = View.VISIBLE
//                        }
//
//                        UiStatus.SUCCESS -> {
//                            it.data?.forEach { cgvMovieItem ->
//                                itemList.add(cgvMovieItem)
//                            }
//
//                            adapter.submitList(itemList)
//
//                            binding.progressCgv.visibility = View.GONE
//                        }
//
//                        UiStatus.ERROR -> {
//                            // TODO: 에러 처리 추가 생각
//                            binding.progressCgv.visibility = View.GONE
//                        }
//
//                        else -> {}
//                    }
//                }
//            }
//
//            launch {
//                viewModel.cgvMoreMovieList.collect {
//                    when (it.state) {
//                        UiStatus.LOADING -> {
//                            binding.progressCgv.visibility = View.VISIBLE
//                        }
//
//                        UiStatus.SUCCESS -> {
//                            it.data?.forEach { cgvMoreMovieItem ->
//                                itemList.add(
//                                    CgvMovieItem(
//                                        cgvMoreMovieItem.thumb,
//                                        cgvMoreMovieItem.title,
//                                        String.format(getString(R.string.format_reservation_rate), cgvMoreMovieItem.reservationRate),
//                                        if (cgvMoreMovieItem.dDay.contains("-")) {
//                                            String.format(getString(R.string.format_released_date), cgvMoreMovieItem.releasedDate, "")
//                                        } else {
//                                            String.format(getString(R.string.format_released_date), cgvMoreMovieItem.releasedDate, "D-${cgvMoreMovieItem.dDay}")
//                                        }
//                                    )
//                                )
//
//                                adapter.submitList(itemList)
//                            }
//
//                            binding.progressCgv.visibility = View.GONE
//                        }
//
//                        UiStatus.ERROR -> {
//                            // TODO: 에러 처리 추가 생각
//                            binding.progressCgv.visibility = View.GONE
//                        }
//
//                        else -> {}
//                    }
//                }
//            }
//
//            launch { viewModel.fetchCgvMovieList() }
//        }
    }

    private fun initAdapter() {
        binding.rvList.adapter = adapter
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.itemAnimator = DefaultItemAnimator()
    }
}