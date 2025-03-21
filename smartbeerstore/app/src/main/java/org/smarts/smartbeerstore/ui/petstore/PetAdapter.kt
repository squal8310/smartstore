package org.smarts.smartbeerstore.ui.petstore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.smarts.smartbeerstore.data.petstore.models.Pet
import org.smarts.smartbeerstore.databinding.ItemPetBinding

class PetAdapter(private val onPetClick: (Pet) -> Unit) :
    ListAdapter<Pet, PetAdapter.PetViewHolder>(PetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ItemPetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PetViewHolder(private val binding: ItemPetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPetClick(getItem(position))
                }
            }
        }

        fun bind(pet: Pet) {
            binding.apply {
                petName.text = pet.name
                petCategory.text = "Category: ${pet.category?.name ?: "Unknown"}"
                petStatus.text = "Status: ${pet.status ?: "Unknown"}"

                // Load pet image if available
                pet.photoUrls.firstOrNull()?.let { photoUrl ->
                    Glide.with(petImage)
                        .load(photoUrl)
                        .centerCrop()
                        .into(petImage)
                }
            }
        }
    }

    private class PetDiffCallback : DiffUtil.ItemCallback<Pet>() {
        override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
            return oldItem == newItem
        }
    }
} 