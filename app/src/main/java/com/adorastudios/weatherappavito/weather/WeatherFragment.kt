package com.adorastudios.weatherappavito.weather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adorastudios.weatherappavito.R
import com.adorastudios.weatherappavito.data.DataSourceProvider

class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(
            (requireActivity() as DataSourceProvider).provideDataSource()
        )
    }
    private var listener: IToCityFragment? = null
    private lateinit var adapter7D: WeatherListAdapter
    private lateinit var adapter24H: WeatherListAdapter

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

        viewModel.repetitiveInit()
        viewModel.locationName.observe(this.viewLifecycleOwner) {
            val text = view.findViewById<TextView>(R.id.textViewLocation)
            text.text = it
        }
        viewModel.locationCoordinates.observe(this.viewLifecycleOwner) {
            val coordinates = view.findViewById<TextView>(R.id.textViewLocationCoordinates)
            coordinates.text = it
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

        viewModel.weather7D.observe(this.viewLifecycleOwner) {
            adapter7D.submitList(it)
        }

        viewModel.weather24H.observe(this.viewLifecycleOwner) {
            adapter24H.submitList(it)
        }

        viewModel.weather.observe(this.viewLifecycleOwner) {
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

        val location = view.findViewById<LinearLayout>(R.id.locationLayout)
        location.setOnClickListener {
            listener?.toCityFragment()
        }
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