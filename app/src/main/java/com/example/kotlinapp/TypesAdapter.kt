package com.example.kotlinapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.TypeElementBinding

class TypesAdapter(
    private val types: List<String>,
    private val getColor: (String) -> Int,
) : RecyclerView.Adapter<TypesAdapter.TypeViewHolder>() {

    inner class TypeViewHolder(private val binding: TypeElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(typeElement: String) {
            binding.typeTextView.text = typeElement.replaceFirstChar { it.uppercase() }
            binding.typeCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    App.instance.applicationContext,
                    getColor(typeElement)
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TypeElementBinding.inflate(inflater, parent, false)
        return TypeViewHolder(binding)
    }

    override fun getItemCount() = types.size

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.bind(types[position])
    }
}