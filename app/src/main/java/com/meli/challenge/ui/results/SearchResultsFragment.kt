package com.meli.challenge.ui.results

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.codechallenge.model.Failure
import com.example.codechallenge.model.Idle
import com.example.codechallenge.model.Loading
import com.example.codechallenge.model.Success
import com.google.android.material.snackbar.Snackbar
import com.meli.challenge.R
import com.meli.challenge.api.responses.GetSearchResponse
import com.meli.challenge.databinding.FragmentSearchResultsBinding
import com.meli.challenge.ui.detail.ARG_PRODUCT
import com.meli.challenge.utils.SimpleDividerItemDecoration
import com.squareup.cycler.Recycler
import com.squareup.cycler.toDataSource
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {
    private val binding by viewBinding(FragmentSearchResultsBinding::bind)
    private val viewModel: SearchResultsViewModel by viewModels()
    private lateinit var recycler: Recycler<GetSearchResponse.Product>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        viewModel.viewStateFlow
            .flowWithLifecycle(lifecycle)
            .onEach { render(it) }
            .launchIn(lifecycleScope)
    }

    private fun render(uiModel: SearchResultsUiModel) {
        with(uiModel) {
            when (uiStatus) {
                Idle -> Timber.d("Idle")
                Loading -> binding.animationView.isVisible = true
                Success -> {
                    binding.animationView.visibility = View.GONE
                    binding.toolbar.title =
                        getString(R.string.resultados_para)
                            .plus(" ")
                            .plus(uiModel.keyword)
                    uiModel.results?.toDataSource()?.let {
                        recycler.update {
                            data = it
                        }
                    }
                }
                is Failure -> {
                    binding.animationView.setAnimation(R.raw.error)
                    binding.animationView.repeatCount = 0
                    binding.animationView.playAnimation()
                    Snackbar.make(
                        requireContext(),
                        requireView(),
                        getString(R.string.error),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        R.string.try_again
                    ) { findNavController().navigateUp() }.show()
                    Timber.e(uiStatus.error)
                }
            }
        }
    }

    private fun setupUI() {
        binding.toolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back)
        binding.toolbar.setOnClickListener {
            findNavController().navigateUp()
        }
        recycler = Recycler.adopt(binding.recycler) {
            row<GetSearchResponse.Product, ListItemView> {
                create { context ->
                    view = ListItemView(context)
                    bind { product ->
                        view.render(product)
                        view.setOnClickListener {
                            val b = Bundle()
                            b.putSerializable(ARG_PRODUCT, product)
                            findNavController().navigate(R.id.product_detail_fragment, b)
                        }
                    }
                }
            }
        }

        recycler.view
            .addItemDecoration(
                SimpleDividerItemDecoration(
                    requireContext(), R.drawable.line_divider
                )
            )
    }
}
