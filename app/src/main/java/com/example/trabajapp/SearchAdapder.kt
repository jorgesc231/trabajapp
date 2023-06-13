package com.example.trabajapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapder(var jobsList : List<JobDataResponse> = emptyList(),
                    private val onItemSelected:(data : JobDataResponse) -> Unit) : RecyclerView.Adapter<SearchViewHolder>() {

    fun updateList(jobsList : List<JobDataResponse>) {
        this.jobsList = jobsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun getItemCount(): Int = jobsList.size

    override fun onBindViewHolder(viewholder: SearchViewHolder, position: Int) {
        viewholder.bind(jobsList[position], onItemSelected)
    }
}