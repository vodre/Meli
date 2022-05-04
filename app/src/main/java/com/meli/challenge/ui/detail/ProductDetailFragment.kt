package com.meli.challenge.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.codechallenge.model.Failure
import com.example.codechallenge.model.Idle
import com.example.codechallenge.model.Loading
import com.example.codechallenge.model.Success
import com.google.android.material.snackbar.Snackbar
import com.meli.challenge.R
import com.meli.challenge.databinding.FragmentProductDetailBinding
import com.meli.challenge.utils.extensions.isVisible
import com.meli.challenge.utils.extensions.toHttpsPrefix
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {
    private val binding by viewBinding(FragmentProductDetailBinding::bind)
    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.viewStateFlow
            .flowWithLifecycle(lifecycle)
            .onEach { render(it) }
            .launchIn(lifecycleScope)
    }

    private fun render(uiModel: ProductDetailUiModel) {
        with(uiModel) {
            when (uiStatus) {
                Idle -> Timber.d("Idle")
                Loading -> Timber.d("Loading")
                Success -> {
                    uiModel.product?.let { p ->
                        binding.toolbar.title = getString(R.string.interested)
                        binding.condition.text = if (p.condition == "new") "Nuevo" else "Usado"
                        binding.price.text = "$".plus(p.price.toInt())
                        binding.photo.load(p.thumbnail?.toHttpsPrefix()) {
                            crossfade(true)
                            placeholder(R.drawable.ic_meli_icon)
                            error(R.drawable.ic_meli_icon)
                        }
                        binding.shipment.isVisible = p.shipping?.freeShipping == true
                        binding.btnDetails.setOnClickListener {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(p.permalink))
                            startActivity(browserIntent)
                        }
                    }
                }
                is Failure -> {
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
}
