package com.example.trabajapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trabajapp.JobDetailActivity.Companion.APPLICATIONS_KEY
import com.example.trabajapp.JobDetailActivity.Companion.BUNDLE_KEY
import com.example.trabajapp.JobDetailActivity.Companion.CATEGORY_KEY
import com.example.trabajapp.JobDetailActivity.Companion.COMPANY_KEY
import com.example.trabajapp.JobDetailActivity.Companion.COMP_LOGO_KEY
import com.example.trabajapp.JobDetailActivity.Companion.DESC_KEY
import com.example.trabajapp.JobDetailActivity.Companion.DESIRABLE_KEY
import com.example.trabajapp.JobDetailActivity.Companion.FUNCTIONS_KEY
import com.example.trabajapp.JobDetailActivity.Companion.ID_KEY
import com.example.trabajapp.JobDetailActivity.Companion.LANG_KEY
import com.example.trabajapp.JobDetailActivity.Companion.MODALITY_KEY
import com.example.trabajapp.JobDetailActivity.Companion.PUBDATE_KEY
import com.example.trabajapp.JobDetailActivity.Companion.PUBLIC_LINK_KEY
import com.example.trabajapp.JobDetailActivity.Companion.REMOTE_KEY
import com.example.trabajapp.JobDetailActivity.Companion.SENIORITY_KEY
import com.example.trabajapp.JobDetailActivity.Companion.TITLE_KEY
import com.example.trabajapp.databinding.ActivityMainBinding
import com.example.trabajapp.JobListActivity.Companion.CATEGORIE_KEY
import com.example.trabajapp.JobListActivity.Companion.NAME_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var retrofit : Retrofit

    private lateinit var adapter : SearchAdapder
    private lateinit var categoriesAdapter : CategoriesAdapder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = getRetrofit()
        initUi()
    }

        private fun initUi() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Cuando se presiona buscar
            override fun onQueryTextSubmit(query: String?): Boolean {

                searchByName(query.orEmpty())

                return false
            }

            // Cuando el texto de busqueda cambia
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.isEmpty()) {
                        // Search
                        binding.rvCategories.isVisible = true
                        binding.rvSearchResults.isVisible = false

                        binding.tvCategories.text = getString(R.string.categories)
                        binding.tvErrorMsg.isVisible = false
                    }
                }

                return false
            }
        })

        // Ya esta inicializado con una lista vacia
        adapter = SearchAdapder { navigateToDetail(it)}
        binding.rvSearchResults.setHasFixedSize(true)
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResults.adapter = adapter


        categoriesAdapter = CategoriesAdapder {navigateToJobList(it)}
        binding.rvCategories.setHasFixedSize(true)
        binding.rvCategories.layoutManager = LinearLayoutManager(this)
        binding.rvCategories.adapter = categoriesAdapter


        getCategories()
    }

    private fun getCategories() {
        binding.progressBar.isVisible = true
        binding.rvCategories.isVisible = false

        CoroutineScope(Dispatchers.IO).launch {
            val myResponse = retrofit.create(ApiService::class.java).getCategoriesList()

            if (myResponse.isSuccessful) {

                val response : CategoriesDataResponse? = myResponse.body()

                if (response != null) {

                    runOnUiThread {
                        categoriesAdapter.updateList(response.data)
                        binding.progressBar.isVisible = false
                        binding.rvCategories.isVisible = true

                    }
                }
            } else {
                Log.i("Prueba", "No Funciona")
            }
        }
    }

    private fun searchByName(query: String) {
        binding.progressBar.isVisible = true
        binding.rvSearchResults.isVisible = false
        binding.rvCategories.isVisible = false

        binding.tvCategories.text = "Trabajos en: \"" + query + "\""

        // Corrutinas
        CoroutineScope(Dispatchers.IO).launch {

            // Lo que ejecutemos aqui se ejecutara en un hilo de I/O
            val myResponse = retrofit.create(ApiService::class.java).getJobs(query)

            if (myResponse.isSuccessful) {
                val response : SearchDataResponse? = myResponse.body()

                if (response != null) {
                    runOnUiThread {
                        if (response.data.isNotEmpty()) {
                            adapter.updateList(response.data)
                            binding.progressBar.isVisible = false
                            binding.rvSearchResults.isVisible = true
                        } else {
                            // Mostrar error
                            binding.progressBar.isVisible = false
                            binding.tvErrorMsg.text = "No se han encontrado empleos para tu búsqueda \"" + query + "\"."
                            binding.tvErrorMsg.isVisible = true
                        }
                    }
                }

            } else {
                Log.i("Prueba", "Fallo la peticion de trabajos")
            }
        }
    }

    private fun getRetrofit() : Retrofit {
        return Retrofit.Builder().baseUrl("https://www.getonbrd.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // private fun navigateToDetail (id : String, jobTitle : String, pubdate : String, functions : String, desirables : String, remote : Boolean, language : String, category : String, applications_count : Int) {

    private fun navigateToDetail(job : JobDataResponse) {

        val intent = Intent(this, JobDetailActivity::class.java)

        val bundle = bundleOf(
            ID_KEY to job.id,
            TITLE_KEY to job.attributes.title,
            PUBDATE_KEY to job.attributes.published_at,
            DESC_KEY to job.attributes.description,
            FUNCTIONS_KEY to job.attributes.functions,
            DESIRABLE_KEY to job.attributes.desirable,
            REMOTE_KEY to job.attributes.remote,
            LANG_KEY to job.attributes.lang,
            CATEGORY_KEY to job.attributes.category_name,
            APPLICATIONS_KEY to job.attributes.applications_count,
            COMPANY_KEY to job.attributes.company.data.attributes.name,
            MODALITY_KEY to job.attributes.modality.data.attributes.name,
            SENIORITY_KEY to job.attributes.seniority.data.attributes.name,
            COMP_LOGO_KEY to job.attributes.company.data.attributes.logo,
            PUBLIC_LINK_KEY to job.links.public_url
        )

        intent.putExtra(BUNDLE_KEY, bundle)

        startActivity(intent)
    }

    private fun navigateToJobList(categorie : CategoriesDataResponseElements) {

        val intent = Intent(this, JobListActivity::class.java)

        intent.putExtra(CATEGORIE_KEY, categorie.id)
        intent.putExtra(NAME_KEY, categorie.attibutes.name)

        startActivity(intent)
    }
}





























