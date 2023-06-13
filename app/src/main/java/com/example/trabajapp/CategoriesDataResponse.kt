package com.example.trabajapp

import com.google.gson.annotations.SerializedName

data class CategoriesDataResponse(
    @SerializedName("data") val data : List<CategoriesDataResponseElements>,
) {}

data class CategoriesDataResponseElements (
    @SerializedName("id") val id : String,
    @SerializedName("attributes") val attibutes : CategoriesDataResponseAttribs
) {}

data class CategoriesDataResponseAttribs (
        @SerializedName("name") val name : String,
        @SerializedName("dimension") val dimension : String
) {}