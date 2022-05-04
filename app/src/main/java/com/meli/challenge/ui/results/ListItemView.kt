package com.meli.challenge.ui.results

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.meli.challenge.R
import com.meli.challenge.api.responses.GetSearchResponse
import com.meli.challenge.databinding.ItemListProductBinding
import com.meli.challenge.utils.extensions.isVisible
import com.meli.challenge.utils.extensions.layoutInflater
import com.meli.challenge.utils.extensions.toHttpsPrefix

class ListItemView(
    context: Context,
    attributeSet: AttributeSet? = null,
) : ConstraintLayout(
    ContextThemeWrapper(context, R.style.Theme_MyApplication),
    attributeSet,
) {
    private val binding = ItemListProductBinding.inflate(layoutInflater, this, true)

    fun render(product: GetSearchResponse.Product) {
        binding.title.text = product.title
        binding.mercadoPago.isVisible = product.acceptsMP
        binding.shipment.isVisible = product.shipping?.freeShipping == true

        binding.condition.text = if (product.condition == "new") "Nuevo" else "Usado"
        binding.price.text = "$".plus(product.price.toInt())
        binding.thumbnail.load(product.thumbnail?.toHttpsPrefix()) {
            crossfade(true)
            placeholder(R.drawable.ic_meli_icon)
            error(R.drawable.ic_meli_icon)
        }
    }
}
