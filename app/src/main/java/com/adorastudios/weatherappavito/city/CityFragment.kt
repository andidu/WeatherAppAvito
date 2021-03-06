package com.adorastudios.weatherappavito.city

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adorastudios.weatherappavito.R

class CityFragment : Fragment() {

    private var listener: IBackFromCityFragment? = null
    private val viewModel: CityViewModel by viewModels {
        CityViewModelFactory(requireContext())
    }
    private lateinit var editText: EditText
    private var toast: Toast? = null

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

        editText = view.findViewById(R.id.editTextEnterPlaceName)

        val setNameBtn = view.findViewById<TextView>(R.id.textViewSetName)

        setNameBtn.setOnClickListener {
            when (viewModel.setName(editText.text.toString())) {
                1 -> {
                    showToast(getString(R.string.location_not_found_error))
                }
                2 -> {
                    showToast(getString(R.string.location_another_error), long = true)
                }
            }
        }

        val saveBtn = view.findViewById<TextView>(R.id.textViewSave)
        saveBtn.setOnClickListener {
            val name = editText.text.toString()
            viewModel.save(name)
            listener?.backFromCityFragment()
        }

        val currentBtn = view.findViewById<TextView>(R.id.textViewCurrent)
        currentBtn.setOnClickListener {
            viewModel.saveDefault()
            listener?.backFromCityFragment()
        }

        viewModel.resultLatitude.observe(this.viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.textViewLatitude).text = String.format("%1$.2f", it)
        }
        viewModel.resultLongitude.observe(this.viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.textViewLongitude).text = String.format("%1$.2f", it)
        }
    }

    private fun showToast(message: String, long: Boolean = true) {
        toast?.cancel()
        toast = Toast.makeText(
            context,
            message,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        )
        toast?.show()
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