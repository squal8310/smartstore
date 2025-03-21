package org.smarts.smartbeerstore.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import org.smarts.smartbeerstore.data.weather.Cities
import org.smarts.smartbeerstore.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initially hide weather views
        updateWeatherViewsVisibility(false)

        // Setup city spinner
        setupCitySpinner(homeViewModel)

        homeViewModel.weatherData.observe(viewLifecycleOwner) { weather ->
            weather?.let {
                binding.cityTitle.text = homeViewModel.selectedCity.value
                binding.temperatureText.text = "${it.temperature}°C"
                binding.descriptionText.text = it.description
                binding.humidityText.text = "Humidity: ${it.humidity}%"
                binding.windSpeedText.text = "Wind: ${it.windSpeed} m/s"
                
                // Load weather icon
                val iconUrl = "https://openweathermap.org/img/wn/${it.icon}@2x.png"
                Glide.with(this)
                    .load(iconUrl)
                    .into(binding.weatherIcon)
                
                updateWeatherViewsVisibility(true)
            } ?: run {
                updateWeatherViewsVisibility(false)
            }
        }

        homeViewModel.error.observe(viewLifecycleOwner) { error ->
            binding.errorText.text = error
            binding.errorText.visibility = if (error.isNullOrEmpty()) View.GONE else View.VISIBLE
            if (!error.isNullOrEmpty()) {
                updateWeatherViewsVisibility(false)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                updateWeatherViewsVisibility(false)
            }
        }

        // Add retry button click listener
        binding.errorText.setOnClickListener {
            homeViewModel.loadWeather()
        }

        return root
    }

    private fun setupCitySpinner(viewModel: HomeViewModel) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Cities.cities
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.citySpinner.adapter = adapter
        binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.setSelectedCity(Cities.cities[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun updateWeatherViewsVisibility(show: Boolean) {
        binding.apply {
            cityTitle.visibility = if (show) View.VISIBLE else View.GONE
            weatherIcon.visibility = if (show) View.VISIBLE else View.GONE
            temperatureText.visibility = if (show) View.VISIBLE else View.GONE
            descriptionText.visibility = if (show) View.VISIBLE else View.GONE
            humidityText.visibility = if (show) View.VISIBLE else View.GONE
            windSpeedText.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}