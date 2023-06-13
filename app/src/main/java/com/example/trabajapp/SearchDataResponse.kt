package com.example.trabajapp

import com.google.gson.annotations.SerializedName

data class SearchDataResponse (@SerializedName("data") val data : List<JobDataResponse>) {}

data class JobDataResponse(
        @SerializedName("id") val id : String,
        @SerializedName("attributes") val attributes : JobAttributesResponse,
        @SerializedName("links") val links : JobLinks
    )

data class JobAttributesResponse(
    @SerializedName("title") val title : String,
    @SerializedName("published_at") val published_at : Long,
    @SerializedName("description") val description : String,
    @SerializedName("functions") val functions : String,
    @SerializedName("desirable") val desirable : String,
    @SerializedName("remote") val remote : Boolean,
    @SerializedName("lang") val lang : String,
    @SerializedName("category_name") val category_name : String,
    @SerializedName("applications_count") val applications_count : Int,

    @SerializedName("modality") val modality : ModalityData,
    @SerializedName("seniority") val seniority : SeniorityData,
    @SerializedName("company") val company : CompanyData

)

data class ModalityData(@SerializedName("data") val data : ModalityDataDetail)
data class ModalityDataDetail(@SerializedName("id") val id : String, @SerializedName("attributes") val attributes : ModalityDataDetailAttributes) {}
data class ModalityDataDetailAttributes(
    @SerializedName("name") val name : String,
    @SerializedName("locale_key") val description : String,
) {}

data class SeniorityData(@SerializedName("data") val data : SeniorityDataDetail)
data class SeniorityDataDetail(@SerializedName("id") val id : String, @SerializedName("attributes") val attributes : SeniorityDataDetailAttributes) {}
data class SeniorityDataDetailAttributes(
    @SerializedName("name") val name : String,
    @SerializedName("locale_key") val description : String,
) {}


data class CompanyData(@SerializedName("data") val data : CompanyDataDetail)
data class CompanyDataDetail(@SerializedName("id") val id : String, @SerializedName("attributes") val attributes : CompanyDataDetailAttributes) {}
data class CompanyDataDetailAttributes(
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
    @SerializedName("web") val web : String,
    @SerializedName("country") val country : String,
    @SerializedName("logo") val logo : String,
    ) {}

data class JobLinks(@SerializedName("public_url") val public_url : String)