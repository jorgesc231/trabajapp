package com.example.trabajapp

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.example.trabajapp.databinding.ItemSearchBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SearchViewHolder (view : View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemSearchBinding.bind(view)

    //private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ENGLISH)
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)

    fun bind(jobDataResponse: JobDataResponse, onItemSelected: (JobDataResponse) -> Unit) {
        binding.tvJobName.text = jobDataResponse.attributes.title

        binding.chipSeniority.text = jobDataResponse.attributes.seniority.data.attributes.name
        binding.chipModality.text = jobDataResponse.attributes.modality.data.attributes.name

        if (jobDataResponse.attributes.remote) {
            binding.chipRemote.visibility = VISIBLE
        } else {
            binding.chipRemote.visibility = GONE
        }

        val formattedPublishedDate = getDateString(jobDataResponse.attributes.published_at)
        binding.tvPublishedDate.text = formattedPublishedDate

        binding.root.setOnClickListener {
            onItemSelected(jobDataResponse)
        }

        // TODO: Hacer que carge la imagen de la empresa
        // Picasso.get().load(jobDataResponse.attributes.image).into(binding.ivCompanyImage)
    }

    private fun getDateString(time: Long) : String = simpleDateFormat.format(time * 1000L)
}