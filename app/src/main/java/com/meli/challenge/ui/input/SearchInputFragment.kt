package com.meli.challenge.ui.input

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.codechallenge.model.Idle
import com.meli.challenge.R
import com.meli.challenge.databinding.FragmentInputSearchBinding
import com.meli.challenge.ui.model.UiModel
import com.meli.challenge.ui.results.ARG_SEARCH_KEYWORD
import com.meli.challenge.utils.extensions.showError
import com.meli.challenge.utils.extensions.showValidationIcon
import com.meli.challenge.utils.extensions.textEvents
import com.meli.challenge.utils.validation.MAX_CHILD_NAME_LENGTH
import com.meli.challenge.utils.validation.SearchInputValidationError
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchInputFragment : Fragment(R.layout.fragment_input_search) {
    private val binding by viewBinding(FragmentInputSearchBinding::bind)
    private val viewModel: SearchInputViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewStateFlow
            .flowWithLifecycle(lifecycle)
            .onEach { render(it) }
            .launchIn(lifecycleScope)

        binding.searchBtn.setOnClickListener {
            val b = Bundle()
            b.putString(ARG_SEARCH_KEYWORD, binding.emailTextInput.editText?.text.toString())
            findNavController().navigate(R.id.search_results_fragment, b)
        }
    }

    private fun render(uiModel: UiModel) {
        when (uiModel.uiStatus) {
            Idle -> {
                binding.emailTextInput.errorIconDrawable = null
                binding.emailTextInput.textEvents()
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .onEach(viewModel::validate)
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }
            else -> {
                with(binding.emailTextInput) {
                    showValidationIcon(uiModel.validatorError == null)
                    binding.searchBtn.isEnabled = uiModel.validatorError == null
                    when (uiModel.validatorError) {
                        SearchInputValidationError.EMPTY_NAME -> {
                            showError(getString(R.string.agrega_criterio))
                        }
                        SearchInputValidationError.LENGTH_EXCEEDED -> {
                            showError(getString(R.string.limite_superado))
                        }
                        else -> Unit // Ignore
                    }
                }
            }
        }
    }
}
