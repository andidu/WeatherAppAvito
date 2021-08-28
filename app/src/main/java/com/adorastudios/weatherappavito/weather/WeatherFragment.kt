package com.adorastudios.weatherappavito.weather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adorastudios.weatherappavito.R
import com.adorastudios.weatherappavito.data.DataSourceImpl

class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(
            DataSourceImpl(),
            requireContext()
        )
    }
    private var listener : IToCityFragment? = null
    private lateinit var adapter7D : WeatherListAdapter
    private lateinit var adapter24H : WeatherListAdapter

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
        viewModel.location.observe(this.viewLifecycleOwner) {
            val text = view.findViewById<TextView>(R.id.textViewLocation)
            text.text = it
        }

        adapter7D = WeatherListAdapter()
        adapter24H = WeatherListAdapter()

        weatherList24H.adapter = adapter24H
        weatherList24H.setHasFixedSize(true)
        weatherList24H.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        weatherList7D.adapter = adapter7D
        weatherList7D.setHasFixedSize(true)
        weatherList7D.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.weather7D.observe(this.viewLifecycleOwner) {
            adapter7D.submitList(it)
        }

        viewModel.weather24H.observe(this.viewLifecycleOwner) {
            adapter24H.submitList(it)
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