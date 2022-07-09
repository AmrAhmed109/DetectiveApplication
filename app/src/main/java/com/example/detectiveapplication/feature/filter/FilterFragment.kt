package com.example.detectiveapplication.feature.filter

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.databinding.FragmentFilterBinding
import com.example.detectiveapplication.utils.City
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maxkeppeler.sheets.calendar.CalendarSheet
import com.maxkeppeler.sheets.calendar.SelectionMode
import com.maxkeppeler.sheets.info.InfoSheet
import java.util.*


class FilterFragment : BottomSheetDialogFragment() {


    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        handleSpinner()
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.etFirstKidnappedDate.setOnClickListener {
            fromDatePicker()
        }
        binding.etSecondKidnappedDate.setOnClickListener {
            toDatePicker()
        }
        binding.btnSearchButton.setOnClickListener {

            if (validateFormData()) {
                val action =
                    FilterFragmentDirections.actionFilterFragmentToSearchFragment(
                        binding.etName.text.toString(),
                        binding.etCityAutoCompleteTextView.text.toString(),
                        binding.etAge.text.toString(),
                        binding.etFirstKidnappedDate.text.toString(),
                        binding.etSecondKidnappedDate.text.toString(),
                    )
                findNavController().navigate(action)
            }
        }

        return binding.root
    }

    private fun handleSpinner(){
        val name = arrayListOf<String>()
        val city = arrayListOf<City>()
        city.add(
            City(
                "بنى سويف", listOf(
                    "مدينة بني سويف",
                    "مدينة الواسطى",
                    "مدينة ناصر",
                    "مدينة إهناسيا",
                    "مدينة ببا",
                    "مدينة سمسطا"
                )
            )
        )
        city.add(
            City(
                "الجيزة",
                listOf("مدينة البدرشين", "مدينة الصف", "مدينة أطفيح", "مدينة العياط,مدينة الباويطي")
            )
        )
        city.add(
            City(
                "الفيوم",
                listOf("مدينة الفيوم", "مدينة طامية", "مدينة سنورس", "مدينة إطسا", "مدينة إطسا")
            )
        )
        city.forEach {
            name.add(it.name)
        }

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, name)

        binding.etCityAutoCompleteTextView.setAdapter(arrayAdapter)

        binding.etCityAutoCompleteTextView.setOnClickListener {
            hideKeyboard()
        }

    }

    private fun fromDatePicker() {
        CalendarSheet().show(requireActivity()) {
            title("التاريخ من") // Set the title of the sheet
            selectionMode(SelectionMode.DATE)
            onPositive { dateStart, dateEnd ->
                binding.etFirstKidnappedDate.setText("${timeInMillisTOString(dateStart.timeInMillis)}")
                Log.d("date", "addAnotherDatePicker: $dateStart")
                Log.d("date", "addAnotherDatePicker: ${dateStart.firstDayOfWeek}")
                Log.d("date", "addAnotherDatePicker: $dateEnd.")
                Log.d("date", "addAnotherDatePicker: $dateStart")

                binding.etFirstKidnappedDate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        com.example.detectiveapplication.R.color.black
                    )
                )
            }
        }
    }

    private fun toDatePicker() {
        CalendarSheet().show(requireActivity()) {
            title("التاريخ الى") // Set the title of the sheet
            selectionMode(SelectionMode.DATE)
            onPositive { dateStart, dateEnd ->
                binding.etSecondKidnappedDate.setText("${timeInMillisTOString(dateStart.timeInMillis)}")
                Log.d("date", "addAnotherDatePicker: $dateStart")
                Log.d("date", "addAnotherDatePicker: ${dateStart.firstDayOfWeek}")
                Log.d("date", "addAnotherDatePicker: $dateEnd.")
                Log.d("date", "addAnotherDatePicker: $dateStart")

                binding.etSecondKidnappedDate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        com.example.detectiveapplication.R.color.black
                    )
                )
            }
        }
    }
    private fun timeInMillisTOString(timeInMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${
            calendar.get(
                Calendar.YEAR
            )
        }"
    }
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etCityAutoCompleteTextView.windowToken, 0)
    }
    private fun validateFormData(): Boolean {
        var isValid = true
        var message = "الرجاء إدخال جميع الحقول"
        if (binding.etSecondKidnappedDate.text.toString().isEmpty()) {
            binding.etSecondKidnappedDate.error = "الرجاء إدخال تاريخ الى"
            message = "الرجاء إدخال تاريخ من"
            isValid = false
        }
        if (binding.etFirstKidnappedDate.text.toString().isEmpty()) {
            binding.etFirstKidnappedDate.error = "الرجاء إدخال تاريخ من"
            message = "الرجاء إدخال تاريخ من"
            isValid = false
        }
        if (binding.etAge.text.toString().isEmpty()) {
            binding.etAge.error = "الرجاء إدخال عمر الشخص"
            message = "الرجاء إدخال عمر الشخص"
            isValid = false
        }
        if (binding.etCityAutoCompleteTextView.text.toString().isEmpty()) {
            binding.etCityAutoCompleteTextView.error = "الرجاء إدخال مدينة الشخص"
            message = "الرجاء إدخال مدينة الشخص"
            isValid = false
        }
        if (binding.etName.text.toString().isEmpty()) {
            binding.etName.error = "الرجاء إدخال الاسم"
            message = "الرجاء إدخال الاسم"
            isValid = false
        }
        if (!isValid) {
            missingInputDialog(title = "تأكد من ملئ البيانات بشكل صحيح", message = message)
            return false
        }
        return true
    }

    fun missingInputDialog(title: String, message: String) {
        InfoSheet().show(requireActivity()) {
            title(title)
            content(message)
            onPositive("") {
                // Handle event
            }
            onNegative("حسنا") {
                // Handle event
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}