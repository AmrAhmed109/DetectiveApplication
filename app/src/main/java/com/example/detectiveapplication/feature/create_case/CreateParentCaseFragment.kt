package com.example.detectiveapplication.feature.create_case

import android.app.DatePickerDialog
import android.media.Image
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
import com.example.detectiveapplication.databinding.FragmentCreateParentCaseBinding
import com.example.detectiveapplication.feature.settings.SettingViewModel
import com.example.detectiveapplication.utils.City
import com.example.detectiveapplication.utils.Constants.Companion.imageBody
import com.example.detectiveapplication.utils.NetworkResult
import com.nguyenhoanglam.imagepicker.model.GridCount
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.model.RootDirectory
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import java.util.*
import kotlin.collections.ArrayList


class CreateParentCaseFragment : Fragment() {

    private var _binding: FragmentCreateParentCaseBinding? = null
    private val binding get() = _binding!!
    var subCity: List<String> = listOf()
   lateinit var createKidViewModel: CreateKidViewModel
    private var mainImage: Uri? = null
    private var birthImage: Uri? = null
    private val mainLauncher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            mainImage = images.first().uri
            binding.ivMain.load(mainImage)
        }
    }
    private val birthDateLauncher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            birthImage = images.first().uri
            binding.ivBirthDate.load(birthImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createKidViewModel = ViewModelProvider(requireActivity())[CreateKidViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateParentCaseBinding.inflate(inflater, container, false)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.etKidnappedTimeInputLayout.setOnClickListener {
            addDatePicker()
        }


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
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, name)
        var secondArrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, subCity)
        binding.etCityAutoCompleteTextView.setAdapter(arrayAdapter)
        binding.etCityAutoCompleteTextView.addTextChangedListener { edit ->
            subCity = city.filter { it.name == edit.toString() }.first().branches
            binding.etSubCityAutoCompleteTextView.text = null
            secondArrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, subCity)
            binding.etSubCityAutoCompleteTextView.setAdapter(secondArrayAdapter)
        }
        binding.etSubCityAutoCompleteTextView.setAdapter(secondArrayAdapter)
        binding.etSubCityAutoCompleteTextView.addTextChangedListener {

            Log.d("TAG", "onCreateView: ${binding.etSubCityAutoCompleteTextView.text}")
        }


        binding.ivMain.setOnClickListener {
            mainLauncher.launch(config)
        }

        binding.ivBirthDate.setOnClickListener {
            birthDateLauncher.launch(config)
        }

        binding.btnCreateCase.setOnClickListener {
            requestApiData()
        }

        return binding.root
    }


    private fun addDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                binding.etKidnappedDate.setText("$day-$month-$year")
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

    private fun requestApiData() {
        Log.v("recipesFragment", "requestApiData called!")

        if (mainImage != null ||
            birthImage != null
        ) {
            createKidViewModel.createKidnappedKid(
                name = binding.etName.text.toString(),
                image = imageBody(requireContext(), mainImage!!),
                other_info = binding.etDescription.text.toString(),
                status = "not_found",
                city = binding.etName.text.toString(),
                sub_city = binding.etSubCityAutoCompleteTextView.text.toString(),
                parent_name = binding.etParentName.text.toString(),
                parent_address = binding.etParentAddress.text.toString(),
                parent_national_id = binding.etParentID.text.toString(),
                parent_phone_number = binding.etParentPhone.text.toString(),
                parent_other_info = "other Info",
                birth_image = imageBody(requireContext(),birthImage!!),
                kidnap_date =binding.etKidnappedDate.text.toString() ,
                age = binding.etAge.text.toString(),
            )
            createKidViewModel.createKidnappedKidResponse.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            findNavController().navigate(R.id.action_createParentCaseFragment_to_watingCasesFragment)
                            Toast.makeText(
                                requireContext(),
                                response.data.message,
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                            .show()
                        Log.d("NetworkResult.Error", "requestApiData: ${response.message.toString()}")
                    }
                    is NetworkResult.Loading -> {
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    val config = ImagePickerConfig(
        statusBarColor = "#FF6F00",
        isLightStatusBar = false,
        isFolderMode = true,
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
        // See more at configuration attributes table below
    )
}