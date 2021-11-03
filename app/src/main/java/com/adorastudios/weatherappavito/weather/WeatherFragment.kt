package com.adorastudios.weatherappavito.weather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adorastudios.weatherappavito.R
import com.adorastudios.weatherappavito.data.DataSourceProvider
import com.adorastudios.weatherappavito.model.Weather

class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(
            (requireActivity() as DataSourceProvider).provideDataSource()
        )
    }
    private var listener: IToCityFragment? = null
    private lateinit var adapter7D: WeatherListAdapter
    private lateinit var adapter24H: WeatherListAdapter

    private var toast: Toast? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is IToCityFragment) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherList7D = view.findViewById<RecyclerView>(R.id.recycler7Days)
        val weatherList24H = view.findViewById<RecyclerView>(R.id.recycler24Hours)

        val location = view.findViewById<LinearLayout>(R.id.locationLayout)
        location.setOnClickListener {
            listener?.toCityFragment()
        }

        when (viewModel.repetitiveInit()) {
            WeatherViewModel.LOADING_RESPONSE_ERROR_CURRENT_LOCATION -> {
                showToast(getString(R.string.loading_response_error_current_location))
                listener?.toCityFragment()
                return
            }
            WeatherViewModel.LOADING_RESPONSE_ERROR_SELECTED_LOCATION -> {
                showToast(getString(R.string.loading_response_error_selected_location))
                listener?.toCityFragment()
                return
            }
        }

        viewModel.location.observe(this.viewLifecycleOwner) {
            val text = view.findViewById<TextView>(R.id.textViewLocation)
            val coordinates = view.findViewById<TextView>(R.id.textViewLocationCoordinates)
            when (it) {
                LocationResponseState.LocationError -> {
                    text.text = requireContext().getString(R.string.error)
                    coordinates.text = ""
                }
                is LocationResponseState.LocationOK -> {
                    text.text = it.name
                    coordinates.text = String.format("[%1$.2fx%2$.2f]", it.latitude, it.longitude)
                }
            }
        }

        adapter7D = WeatherListAdapter()
        adapter24H = WeatherListAdapter()

        weatherList24H.adapter = adapter24H
        weatherList24H.setHasFixedSize(true)
        weatherList24H.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        weatherList7D.adapter = adapter7D
        weatherList7D.setHasFixedSize(true)
        weatherList7D.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.weatherResponse.observe(this.viewLifecycleOwner) {
            when (it) {
                is WeatherResponseState.WeatherError -> {
                    showToast(getString(R.string.error_occurred, it.error.message))
                }
                is WeatherResponseState.WeatherOK -> {
                    adapter7D.submitList(it.weather7d)
                    adapter24H.submitList(it.weather24h)
                    showCurrentWeather(view, it.weatherNow)
                }
            }
        }
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        )
        toast?.show()
    }

    private fun showCurrentWeather(view: View, it: Weather) {
        view.findViewById<TextView>(R.id.textViewHumidity).text =
            requireContext().getString(R.string.number_percentage, it.humidity)

        view.findViewById<TextView>(R.id.textViewTemperature).text =
            requireContext().getString(R.string.number_degrees, it.temperature)

        view.findViewById<ImageView>(R.id.imageViewRainState)
            .load(requireContext().getString(R.string.load_image_big, it.rainImage))

        val time = it.time % (24 * 60 * 60)
        view.findViewById<TextView>(R.id.textViewTime).text = requireContext().getString(
            R.string.time_string,
            time / 3600,
            time / 60 % 60,
            time % 60
        )
        view.findViewById<ImageView>(R.id.imageViewBackground).setBackgroundColor(
            if (time / 3600 >= 18 || time / 3600 < 6) {
                resources.getColor(R.color.dark_sky, null)
            } else {
                resources.getColor(R.color.blue_sky, null)
            }
        )
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    interface IToCityFragment {
        fun toCityFragment()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WeatherFragment()
    }
}