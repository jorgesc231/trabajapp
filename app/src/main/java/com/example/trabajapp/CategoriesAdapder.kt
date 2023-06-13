package com.example.trabajapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoriesAdapder(var categoriesList : List<CategoriesDataResponseElements> = emptyList(),
                        private val onItemSelected:(data : CategoriesDataResponseElements) -> Unit) : RecyclerView.Adapter<CategoriesViewHolder>() {

    fun updateList(categoriesList : List<CategoriesDataResponseElements>) {
        this.categoriesList = categoriesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun getItemCount(): Int = categoriesList.size

    override fun onBindViewHolder(viewholder: CategoriesViewHolder, position: Int) {
        viewholder.bind(categoriesList[position], onItemSelected)
    }
}