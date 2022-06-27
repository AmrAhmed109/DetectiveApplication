package com.example.detectiveapplication.feature.create_case

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentCreateStrangerCaseBinding
import com.example.detectiveapplication.utils.City
import com.example.detectiveapplication.utils.Constants.Companion.imageBody
import com.example.detectiveapplication.utils.NetworkResult
import com.nguyenhoanglam.imagepicker.model.GridCount
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.model.RootDirectory
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import java.util.*


class CreateStrangerCaseFragment : Fragment() {
    private var _binding: FragmentCreateStrangerCaseBinding? = null
    private val binding get() = _binding!!
    private var mainImage : Uri? = null
    var subCity: List<String> = listOf()
   lateinit var createStrangerCaseViewModel: CreateStrangerCaseViewModel
    val TAG = "CreateStranger"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createStrangerCaseViewModel = ViewModelProvider(requireActivity())[CreateStrangerCaseViewModel::class.java]
    }

    private val mainLauncher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            mainImage = images.first().uri
            binding.ivMain.load(mainImage)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateStrangerCaseBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etKidnappedTimeInputLayout.setOnClickListener {
            addDatePicker()
        }
        binding.ivMain.setOnClickListener {
            mainLauncher.launch(config)
        }

        binding.btnCreateCase.setOnClickListener {
            if (validateFormData()){
                requestApiCall()
            }
        }
        handleSpinner()

        return binding.root
    }

    private fun requestApiCall() {
        if (mainImage != null){
            createStrangerCaseViewModel.createFoundKid(
                name = binding.etName.text.toString(),
                image = imageBody(requireContext(),mainImage!!,"image"),
                other_info = binding.etDescription.text.toString(),
                city = binding.etCityAutoCompleteTextView.text.toString(),
                sub_city = binding.etSubCityAutoCompleteTextView.text.toString(),
                parent_name = binding.etParentName.text.toString(),
                parent_address = binding.etParentAddress.text.toString(),
                parent_national_id = binding.etParentID.text.toString(),
                parent_other_info = "لا يتم اسنخدام هذا الحقل في المشروع",
                parent_phone_number = binding.etParentPhone.text.toString(),
                kidnap_date = binding.etKidnappedDate.text.toString(),
            )

            createStrangerCaseViewModel.createFoundKidResponse.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is NetworkResult.Success -> {
                        log("Success")
                        response.data?.let {
                            toast(it.message)
                        }
                        findNavController().navigate(R.id.action_createStrangerCaseFragment_to_casesFragment)
                    }
                    is NetworkResult.Loading -> {
                        toast("Loading")
                    }
                    is NetworkResult.Error -> {
                        toast("Error : ${response.message.toString()}")
                    }
                }

            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun log(message: String) {
        Log.d(TAG, message)
    }

    private fun validateFormData(): Boolean {
        var isValid = true
        if (binding.etName.text.toString().isEmpty()) {
            binding.etNameInputLayout.error = "الرجاء إدخال اسم الشخص"
            isValid =  false
        }
        if (mainImage == null) {
            toast("يرجى اضافة صورة للشخص المعثور علية")
            isValid =  false
        }
        if (binding.etKidnappedDate.text.toString().isEmpty()) {
            toast("يرجى إدخال تاريخ الاختفاء")
            isValid =  false
        }
        if (binding.etDescription.text.toString().isEmpty()) {
            binding.etDesriptionLayout.error = "الرجاء إدخل تفاصيل الحالة"
            isValid =  false
        }

        if (binding.etSubCityAutoCompleteTextView.text.isEmpty()) {
            binding.etSubCityInputLayout.error = "الرجاء إدخال المدينة"
            isValid =  false
        }
        if (binding.etCityAutoCompleteTextView.text.isEmpty()) {
            binding.etCityInputLayout.error = "الرجاء إدخال المحافظة"
            isValid =  false
        }
        if (binding.etParentAddress.text.toString().isEmpty()) {
            binding.etParentAddressLayout.error = "الرجاء إدخال عنوان ولي الامر"
            isValid =  false
        }
        if (binding.etParentName.text.toString().isEmpty()) {
            binding.etParentNameLayout.error = "الرجاء إدخال اسم ولي الامر"
            isValid =  false
        }
        if (binding.etParentID.text.toString().isEmpty()) {
            binding.etParentIDLayout.error = "الرجاء إدخال الرقم القومي"
            isValid =  false
        }
        if (binding.etParentPhone.text.toString().isEmpty()) {
            binding.etParentPhoneLayout.error = "الرجاء إدخال رقم هاتف ولي الامر"
            isValid =  false
        }
        if (!isValid){
            return false
        }
        return true
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

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, name)
        var secondArrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, subCity)

        binding.etCityAutoCompleteTextView.setAdapter(arrayAdapter)

        binding.etCityAutoCompleteTextView.addTextChangedListener { edit ->
            subCity = city.filter { it.name == edit.toString() }.first().branches
            binding.etSubCityAutoCompleteTextView.text = null
            secondArrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, subCity)
            binding.etSubCityAutoCompleteTextView.setAdapter(secondArrayAdapter)
        }
        binding.etSubCityAutoCompleteTextView.setAdapter(secondArrayAdapter)
        binding.etSubCityAutoCompleteTextView.addTextChangedListener {
            Log.d("TAG", "onCreateView: ${binding.etSubCityAutoCompleteTextView.text}")
        }
    }

    private fun addDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                binding.etKidnappedDate.setText("$dayOfMonth-$month-$year")
                binding.etKidnappedDate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    val config = ImagePickerConfig(
        statusBarColor = "#FF6F00",
        isLightStatusBar = false,
        isFolderMode = true,
        maxSize = 1,
        isMultipleMode = true,
        backgroundColor = "#FFFFFFFF",
        isShowCamera = true,
        toolbarColor = "#FF6F00",
        doneTitle = "تم",
        limitMessage = "كفايا كدة هما 5 حلوين مش هنطمع",
        folderTitle = "نقي براحتك",
        rootDirectory = RootDirectory.DCIM,
        selectedIndicatorColor = "#FF6F00",
        subDirectory = "Photos",
        folderGridCount = GridCount(2, 4),
        imageGridCount = GridCount(3, 5),
    )
}