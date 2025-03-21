package org.smarts.smartbeerstore.ui.petstore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.smarts.smartbeerstore.databinding.FragmentPetstoreBinding

class PetStoreFragment : Fragment() {
    private var _binding: FragmentPetstoreBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PetStoreViewModel
    private lateinit var adapter: PetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PetStoreViewModel::class.java)
        _binding = FragmentPetstoreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupObservers()
        setupClickListeners()

        return root
    }

    private fun setupRecyclerView() {
        adapter = PetAdapter { pet ->
            viewModel.selectPet(pet)
            // TODO: Navigate to pet details
        }

        binding.petsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PetStoreFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.pets.observe(viewLifecycleOwner) { pets ->
            adapter.submitList(pets)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.errorText.text = error
            binding.errorText.visibility = if (error.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.addPetButton.setOnClickListener {
            // TODO: Navigate to add pet screen
        }

        binding.errorText.setOnClickListener {
            viewModel.loadAvailablePets()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 