package com.example.trabajapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trabajapp.databinding.ActivityJobsListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JobListActivity : AppCompatActivity() {

    companion object {
        const val CATEGORIE_KEY = "categorie_key"
        const val NAME_KEY = "name_key"
    }

    private lateinit var binding : ActivityJobsListBinding
    private lateinit var adapter : SearchAdapder
    private lateinit var retrofit : Retrofit

    private lateinit var categorie : String
    private lateinit var categorie_name : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJobsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        retrofit = getRetrofit()

        categorie_name = intent.getStringExtra(NAME_KEY).orEmpty()
        categorie = intent.getStringExtra(CATEGORIE_KEY).orEmpty()

        initUi()
        getJobsList(categorie)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun initUi() {

        binding.tvTitle.text = categorie_name

        adapter = SearchAdapder { navigateToDetail(it)}
        binding.rvJobs.setHasFixedSize(true)
        binding.rvJobs.layoutManager = LinearLayoutManager(this)
        binding.rvJobs.adapter = adapter
    }

    private fun getJobsList(categorie : String) {
        binding.progressBar.isVisible = true
        binding.rvJobs.isVisible = false

        // Corrutinas
        CoroutineScope(Dispatchers.IO).launch {

            // Lo que ejecutemos aqui se ejecutara en un hilo de I/O
            val myResponse = retrofit.create(ApiService::class.java).getJobsList(categorie)

            if (myResponse.isSuccessful) {
                val response : SearchDataResponse? = myResponse.body()

                if (response != null) {
                    runOnUiThread {
                        if (response.data.isNotEmpty()) {
                            adapter.updateList(response.data)
                            binding.progressBar.isVisible = false
                            binding.rvJobs.isVisible = true
                        } else {
                            // Mostrar error
                            binding.progressBar.isVisible = false
                        }
                    }
                }

            } else {
                Log.i("Prueba", "No Funciona")
            }
        }
    }

    private fun navigateToDetail(job : JobDataResponse) {

        val intent = Intent(this, JobDetailActivity::class.java)

        val bundle = bundleOf(
            JobDetailActivity.ID_KEY to job.id,
            JobDetailActivity.TITLE_KEY to job.attributes.title,
            JobDetailActivity.PUBDATE_KEY to job.attributes.published_at,
            JobDetailActivity.DESC_KEY to job.attributes.description,
            JobDetailActivity.FUNCTIONS_KEY to job.attributes.functions,
            JobDetailActivity.DESIRABLE_KEY to job.attributes.desirable,
            JobDetailActivity.REMOTE_KEY to job.attributes.remote,
            JobDetailActivity.LANG_KEY to job.attributes.lang,
            JobDetailActivity.CATEGORY_KEY to job.attributes.category_name,
            JobDetailActivity.APPLICATIONS_KEY to job.attributes.applications_count,
            JobDetailActivity.COMPANY_KEY to job.attributes.company.data.attributes.name,
            JobDetailActivity.MODALITY_KEY to job.attributes.modality.data.attributes.name,
            JobDetailActivity.SENIORITY_KEY to job.attributes.seniority.data.attributes.name,
            JobDetailActivity.COMP_LOGO_KEY to job.attributes.company.data.attributes.logo,
            JobDetailActivity.PUBLIC_LINK_KEY to job.links.public_url
        )

        intent.putExtra(JobDetailActivity.BUNDLE_KEY, bundle)

        startActivity(intent)
    }

    private fun getRetrofit() : Retrofit {
        return Retrofit.Builder().baseUrl("https://www.getonbrd.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}