package com.adorastudios.weatherappavito.city

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adorastudios.weatherappavito.R

class CityFragment : Fragment() {

    private var listener: IBackFromCityFragment? = null
    private val viewModel: CityViewModel by viewModels {
        CityViewModelFactory(requireContext())
    }
    private lateinit var editText: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is IBackFromCityFragment) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = view.findViewById(R.id.editTextEnterLocation)

        val setName = view.findViewById<TextView>(R.id.textViewSetName)
        val setZip = view.findViewById<TextView>(R.id.textViewSetZip)

        setName.setOnClickListener {
            viewModel.setName(editText.text.toString())
            listener?.backFromCityFragment()
        }

        setZip.setOnClickListener {
            viewModel.setZip(editText.text.toString())
            listener?.backFromCityFragment()
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    interface IBackFromCityFragment {
        fun backFromCityFragment()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CityFragment()
    }
}