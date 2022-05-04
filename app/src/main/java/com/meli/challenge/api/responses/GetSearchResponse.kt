package com.meli.challenge.api.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetSearchResponse : Serializable {
    @SerializedName("site_id")
    var siteId: String? = null
    @SerializedName("results")
    var results: ArrayList<Product>? = null

    inner class Product : Serializable {
        @SerializedName("title")
        var title: String? = null

        @SerializedName("price")
        var price: Number = 0

        @SerializedName("accepts_mercadopago")
        var acceptsMP: Boolean = false

        @SerializedName("thumbnail")
        var thumbnail: String? = null

        @SerializedName("permalink")
        var permalink: String? = null

        @SerializedName("condition")
        var condition: String? = null

        @SerializedName("available_quantity")
        var availableQuantity: Number = 0

        @SerializedName("shipping")
        var shipping: Shipping? = null
    }

    inner class Shipping : Serializable {
        @SerializedName("free_shipping")
        var freeShipping: Boolean = false
    }
}
