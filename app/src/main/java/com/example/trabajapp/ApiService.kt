package com.example.trabajapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/v0/search/jobs")
    suspend fun getJobs(@Query("query") search_query : String, @Query("expand") expand : String = "[\"modality\", \"seniority\", \"company\"]") : Response<SearchDataResponse>

    @GET("/api/v0/categories/{categorie}/jobs?per_page=20&page=1")
    suspend fun getJobsList(@Path("categorie") categorie : String, @Query("expand") expand : String = "[\"modality\", \"seniority\", \"company\"]") : Response<SearchDataResponse>

    @GET("/api/v0/categories?per_page=30")
    suspend fun getCategoriesList(@Query("page") pageNum:Int = 1) : Response<CategoriesDataResponse>
}