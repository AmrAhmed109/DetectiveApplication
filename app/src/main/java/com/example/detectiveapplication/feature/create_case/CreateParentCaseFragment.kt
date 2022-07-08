package com.example.detectiveapplication.feature.create_case

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentCreateParentCaseBinding
import com.example.detectiveapplication.feature.home.faceDetection.FaceDetectionFragment
import com.example.detectiveapplication.feature.settings.SettingViewModel
import com.example.detectiveapplication.utils.City
import com.example.detectiveapplication.utils.Constants.Companion.anotherImageBody
import com.example.detectiveapplication.utils.Constants.Companion.imageBody
import com.example.detectiveapplication.utils.FileUtil
import com.example.detectiveapplication.utils.NetworkResult
import com.maxkeppeler.sheets.calendar.CalendarSheet
import com.maxkeppeler.sheets.calendar.SelectionMode
import com.nguyenhoanglam.imagepicker.model.GridCount
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.model.RootDirectory
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log


class CreateParentCaseFragment : Fragment() {

    private var _binding: FragmentCreateParentCaseBinding? = null
    private val binding get() = _binding!!
    var subCity: List<String> = listOf()
    lateinit var createKidViewModel: CreateKidViewModel
    private var mainImage: Uri? = null
    private var birthImage: Uri? = null
    private var parentImage: Uri? = null
    private lateinit var dialogLoader: Dialogloader
    private val mainLauncher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            mainImage = images.first().uri
            binding.ivMain.apply {
                setImageURI(mainImage)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
//            lifecycleScope.launch(Dispatchers.IO) {
//                mainImage = compressedImageFile(images.first().uri)
//            }
        }
    }
    private val birthDateLauncher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            birthImage = images.first().uri
            binding.ivBirthDate.load(birthImage)
        }
    }
    private val parentLauncher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            parentImage = images.first().uri
            binding.ivParentImage.load(parentImage)
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
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.etKidnappedTimeInputLayout.setOnClickListener {
//            addDatePicker()
            addAnotherDatePicker()
        }


        val name = arrayListOf<String>()
        val city = arrayListOf<City>()
        city.add(
            City(
                "بنى سويف", listOf(
                    "مدينة بني سويف",
                    "الواسطى",
                    "ناصر",
                    "إهناسيا",
                    "ببا",
                    "سمسطا"
                )
            )
        )
        city.add(
            City(
                "الجيزة",
                listOf("البدرشين", "الصف", "أطفيح", "العياط" ,"الباويطي")
            )
        )
        city.add(
            City(
                "الفيوم",
                listOf("الفيوم", "طامية", " سنورس", "إطسا", "إطسا")
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
//            permReqLauncher.launch(FaceDetectionFragment.PERMISSIONS)
        }

        binding.ivBirthDate.setOnClickListener {
            birthDateLauncher.launch(config)
        }
        binding.ivParentImage.setOnClickListener {
            parentLauncher.launch(config)
        }

        binding.btnCreateCase.setOnClickListener {
            if (validateFormData()) {
                requestApiData()
            }
        }

        return binding.root
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

    private fun addAnotherDatePicker() {
        CalendarSheet().show(requireActivity()) {
            title("ما هو تاريخ الاختفاء؟") // Set the title of the sheet
            selectionMode(SelectionMode.DATE)
            onPositive { dateStart, dateEnd ->
                binding.etKidnappedDate.setText("${timeInMillisTOString(dateStart.timeInMillis)}")
                Log.d("date", "addAnotherDatePicker: $dateStart")
                Log.d("date", "addAnotherDatePicker: ${dateStart.firstDayOfWeek}")
                Log.d("date", "addAnotherDatePicker: $dateEnd.")
                Log.d("date", "addAnotherDatePicker: $dateStart")

                binding.etKidnappedDate.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
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

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun validateFormData(): Boolean {
        var isValid = true
        if (binding.etName.text.toString().isEmpty()) {
            binding.etNameInputLayout.error = "الرجاء إدخال اسم الشخص"
            isValid = false
        }
        if (mainImage == null) {
            toast("يرجى اضافة صورة للشخص المفقود")
            isValid = false
        }
        if (birthImage == null) {
            toast("يرجى اضافة صورة لشهادة ميلاد الشخص المفود")
            isValid = false
        }
        if (parentImage == null) {
            toast("يرجى اضافة صورة لشهادة ميلاد الشخص المفود")
            isValid = false
        }
        if (binding.etKidNationalId.text.toString().isEmpty()) {
            binding.etKidNationalIdLayout.error = "الرجاء إدخال الرقم القومي الخاص بالشخص"
            isValid = false
        }
        if (binding.etKidnappedDate.text.toString().isEmpty()) {
            toast("يرجى إدخال تاريخ الاختفاء")
            isValid = false
        }
        if (binding.etDescription.text.toString().isEmpty()) {
            binding.etDesriptionLayout.error = "الرجاء إدخل تفاصيل الحالة"
            isValid = false
        }
        if (binding.etAge.text.toString().isEmpty()) {
            binding.etAgeInputLayout.error = "الرجاء إدخل عمر الشخص"
            isValid = false
        }
        if (binding.etSubCityAutoCompleteTextView.text.isEmpty()) {
            binding.etSubCityInputLayout.error = "الرجاء إدخال المدينة"
            isValid = false
        }
        if (binding.etCityAutoCompleteTextView.text.isEmpty()) {
            binding.etCityInputLayout.error = "الرجاء إدخال المحافظة"
            isValid = false
        }
        if (binding.etParentAddress.text.toString().isEmpty()) {
            binding.etParentAddressLayout.error = "الرجاء إدخال عنوان ولي الامر"
            isValid = false
        }
        if (binding.etParentName.text.toString().isEmpty()) {
            binding.etParentNameLayout.error = "الرجاء إدخال اسم ولي الامر"
            isValid = false
        }
        if (binding.etParentID.text.toString().isEmpty()) {
            binding.etParentIDLayout.error = "الرجاء إدخال الرقم القومي"
            isValid = false
        }
        if (binding.etParentPhone.text.toString().isEmpty()) {
            binding.etParentPhoneLayout.error = "الرجاء إدخال رقم هاتف ولي الامر"
            isValid = false
        }
        if (!isValid) {
            return false
        }
        return true
    }
    fun showLoader(){
        dialogLoader.show()
    }

    fun hideLoader(){
        dialogLoader.hide()
    }
    private fun requestApiData() {
        Log.v("recipesFragment", "requestApiData called!")

//        showLoader()
        if (mainImage != null ||
            birthImage != null ||
            parentImage != null
        ) {
            val TAG = "imagePicker"
            Log.e(TAG, "mainImage : ${mainImage.toString()}")
            Log.e(TAG, "birthImage: ${birthImage.toString()}")
            createKidViewModel.createKidnappedKid(
                name = binding.etName.text.toString(),
                image = imageBody(requireContext(), mainImage!!, "image"),
                other_info = binding.etDescription.text.toString(),
                birth_image = anotherImageBody(requireContext(), birthImage!!, "birth_image"),
                status = "not_found",
                kid_national_id = binding.etKidNationalId.text.toString(),
                city = binding.etName.text.toString(),
                sub_city = binding.etSubCityAutoCompleteTextView.text.toString(),
                parent_name = binding.etParentName.text.toString(),
                parent_address = binding.etParentAddress.text.toString(),
                parent_national_id = binding.etParentID.text.toString(),
                parent_phone_number = binding.etParentPhone.text.toString(),
                parent_other_info = "هذة الحقل لا يتم الاستخدامة في المشروع التخرج",
                kidnap_date = binding.etKidnappedDate.text.toString(),
                age = binding.etAge.text.toString(),
                parent_image = anotherImageBody(requireContext(), parentImage!!, "parent_image")
            )
            createKidViewModel.createKidnappedKidResponse.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            hideLoader()
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
                        hideLoader()
                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        Log.d(
                            "NetworkResult.Error",
                            "requestApiData: ${response.message.toString()}"
                        )
                    }
                    is NetworkResult.Loading -> {
                        showLoader()
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


    private suspend fun compressedImageFile(uri: Uri): Uri {
        val compressedImageFile =
            Compressor.compress(
                requireContext(),
                FileUtil.getFileFromUri(requireContext(), uri),
                Dispatchers.IO
            ) {
                resolution(1280, 1280)
                quality(80)
                format(Bitmap.CompressFormat.PNG)
                size(2_097_152 / 2) // 2 MB
            }

        return compressedImageFile.toUri()
    }
    companion object {
        var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
    val CAPTURE_REQUEST_CODE = 5
    private val permReqLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all {
            it.value == true
        }
        if (granted) {
            dispatchTakePictureIntent()
        }


    }
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    @SuppressLint("MissingPermission")
    private fun dispatchTakePictureIntent() {
        if (hasPermissions(activity as Context, FaceDetectionFragment.PERMISSIONS)) {
            try {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, CAPTURE_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
            }
        } else {
            permReqLauncher.launch(
                FaceDetectionFragment.PERMISSIONS
            )
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val image = data?.data
            if (image != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    mainImage = compressedImageFile(image)
                    binding.ivMain.load(mainImage)
                }
            }
        }

    }
}