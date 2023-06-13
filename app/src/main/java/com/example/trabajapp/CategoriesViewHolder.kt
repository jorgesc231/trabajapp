package com.example.trabajapp

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.example.trabajapp.databinding.ItemCategoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class CategoriesViewHolder (view : View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCategoryBinding.bind(view)

    fun bind(categories: CategoriesDataResponseElements, onItemSelected: (CategoriesDataResponseElements) -> Unit) {
        binding.tvCategoryName.text = categories.attibutes.name

        binding.root.setOnClickListener {
            onItemSelected(categories)
        }
    }
}