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
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.example.detectiveapplication.utils.Constants.Companion.aanotherImageBody
import com.example.detectiveapplication.utils.Constants.Companion.anotherImageBody
import com.example.detectiveapplication.utils.Constants.Companion.imageBody
import com.example.detectiveapplication.utils.Constants.Companion.listOfCites
import com.example.detectiveapplication.utils.Constants.Companion.secondTryBody
import com.example.detectiveapplication.utils.Constants.Companion.therdTryBody
import com.example.detectiveapplication.utils.Constants.Companion.tryBody
import com.example.detectiveapplication.utils.FileUtil
import com.example.detectiveapplication.utils.NetworkResult
import com.maxkeppeler.sheets.calendar.CalendarSheet
import com.maxkeppeler.sheets.calendar.SelectionMode
import com.maxkeppeler.sheets.info.InfoSheet
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
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.math.log


class CreateParentCaseFragment : Fragment() {

    private var _binding: FragmentCreateParentCaseBinding? = null
    private val binding get() = _binding!!
    var subCity: List<String> = listOf()
    lateinit var createKidViewModel: CreateKidViewModel
    private var mainImage: Uri? = null
    private var mainImageFile: File? = null
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
        }
    }
    fun prepareBitmap(uri: Uri):File{
        val fullSizeBitmap = getImageFromUri(uri,requireContext())
        val reduceBitmap = FileUtil.reduceBitmapSize(fullSizeBitmap,307200)
        val file = getBitmapFile(reduceBitmap)
        return file
    }
    private val birthDateLauncher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            birthImage = images.first().uri
            binding.ivBirthDate.apply {
                load(birthImage)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }
    }
    private val parentLauncher = registerImagePicker { images ->
        // Selected images are ready to use
        if (images.isNotEmpty()) {
            parentImage = images.first().uri
            binding.ivParentImage.apply {
                load(parentImage)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createKidViewModel = ViewModelProvider(requireActivity())[CreateKidViewModel::class.java]
    }
    fun getImageFromUri(imageUri: Uri?, context: Context): Bitmap? {
        imageUri?.let {
            return if (Build.VERSION.SDK_INT < 28) {
                MediaStore
                    .Images
                    .Media
                    .getBitmap(context.contentResolver, imageUri)
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        return null
    }
    fun convertImagePathToBitmap(uri: Uri?): Bitmap? = BitmapFactory.decodeFile(uri?.path)
    fun convertBitmapToFile(bitmap: Bitmap): java.io.File {
        val file = java.io.File.createTempFile("image", ".jpg")
        val outputStream: java.io.OutputStream =
            java.io.BufferedOutputStream(java.io.FileOutputStream(file))
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 30, outputStream)
        outputStream.close()
        return file
    }

    fun getBitmapFile(bitmap: Bitmap):File{
        val file = java.io.File.createTempFile("image", ".jpg")

//        val file = File("${Environment.DIRECTORY_DCIM}${File.separator}", "reduced_file")

        val bos = ByteArrayOutputStream()
//        file.createNewFile()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val bitmapData = bos.toByteArray()
        try {
            file.createNewFile()
            val fos = file.outputStream()
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            return file
        }catch (e:Exception){
            Log.d("CreateParentCaseFragment", "getBitmapFile: $e")
        }
        return file
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
            addAnotherDatePicker()
        }

        val name = arrayListOf<String>()
        val city = listOfCites()
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

        binding.etCityAutoCompleteTextView.setOnClickListener {
            hideCityKeyboard()
        }
        binding.etSubCityAutoCompleteTextView.setOnClickListener {
            hideSubCityKeyboard()
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
    private fun hideCityKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etCityAutoCompleteTextView.windowToken, 0)
    }
    private fun hideSubCityKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSubCityAutoCompleteTextView.windowToken, 0)
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


    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun validateFormData(): Boolean {
        var isValid = true
        var message = "الرجاء إدخال جميع الحقول"
        if (binding.etName.text.toString().isEmpty()) {
            binding.etNameInputLayout.error = "الرجاء إدخال اسم الشخص"
            isValid = false
        }
        if (mainImage == null) {
//            toast("يرجى اضافة صورة للشخص المفقود")
            message= "يرجى اضافة صورة للشخص المفقود"
            isValid = false
        }
        if (birthImage == null) {
//            toast("يرجى اضافة صورة لشهادة ميلاد الشخص المفود")
            message= "يرجى اضافة صورة لشهادة ميلاد الشخص المفقود"
            isValid = false
        }
        if (parentImage == null) {
//            toast("يرجى اضافة صورة لشهادة ميلاد الشخص المفود")
            message= "يرجى اضافة صورة لشهادة ميلاد الشخص المفقود"
            isValid = false
        }
        if (binding.etKidNationalId.text.toString().isEmpty()) {
            binding.etKidNationalIdLayout.error = "الرجاء إدخال الرقم القومي الخاص بالشخص"
            message= "الرجاء إدخال الرقم القومي الخاص بالشخص"
            isValid = false
        }
        if (binding.etKidNationalId.text.toString().length != 14) {
            binding.etParentIDLayout.error = "الرجاء إدخال الرقم القومي صحيح"
            message = "الرجاء إدخال الرقم القومي صحيح"
            isValid =  false
        }
        if (binding.etKidnappedDate.text.toString().isEmpty()) {
//            toast("يرجى إدخال تاريخ الاختفاء")
            message= "يرجى إدخال تاريخ الاختفاء"
            isValid = false
        }
        if (binding.etDescription.text.toString().length < 50) {
            binding.etDesriptionLayout.error = "الرجاء إدخال تفاصيل اكثر عن الحالة"
            message = "الرجاء إدخال اكثر تفاصيل الحالة"
            isValid =  false
        }
        if (binding.etDescription.text.toString().isEmpty()) {
            binding.etDesriptionLayout.error = "الرجاء إدخل تفاصيل الحالة"
            message= "الرجاء إدخل تفاصيل الحالة"
            isValid = false
        }
        if (binding.etAge.text.toString().isEmpty()) {
            binding.etAgeInputLayout.error = "الرجاء إدخل عمر الشخص"
            message= "الرجاء إدخل عمر الشخص"
            isValid = false
        }
        if (binding.etAge.text.toString().length > 2) {
            binding.etAgeInputLayout.error = "الرجاء إدخل عمر صحيح"
            message= "الرجاء إدخل عمر صحيح"
            isValid = false
        }
        if (binding.etSubCityAutoCompleteTextView.text.isEmpty()) {
            binding.etSubCityInputLayout.error = "الرجاء إدخال المدينة"
            message= "الرجاء إدخال المدينة"
            isValid = false
        }

        if (binding.etCityAutoCompleteTextView.text.isEmpty()) {
            binding.etCityInputLayout.error = "الرجاء إدخال المحافظة"
            message= "الرجاء إدخال المحافظة"
            isValid = false
        }
        if (binding.etParentAddress.text.toString().isEmpty()) {
            binding.etParentAddressLayout.error = "الرجاء إدخال عنوان ولي الامر"
            message= "الرجاء إدخال عنوان ولي الامر"
            isValid = false
        }
        if (binding.etParentName.text.toString().isEmpty()) {
            binding.etParentNameLayout.error = "الرجاء إدخال اسم ولي الامر"
            message= "الرجاء إدخال اسم ولي الامر"
            isValid = false
        }
        if (binding.etParentID.text.toString().isEmpty()) {
            binding.etParentIDLayout.error = "الرجاء إدخال الرقم القومي"
            message= "الرجاء إدخال الرقم القومي"
            isValid = false
        }
        if (binding.etParentPhone.text.toString().isEmpty()) {
            binding.etParentPhoneLayout.error = "الرجاء إدخال رقم هاتف ولي الامر"
            message= "الرجاء إدخال رقم هاتف ولي الامر"
            isValid = false
        }
        if (binding.etParentID.text.toString().length != 14) {
            binding.etParentIDLayout.error = "الرجاء إدخال الرقم القومي صحيح"
            message = "الرجاء إدخال الرقم القومي صحيح"
            isValid =  false
        }
        if (!validatePhoneNumber(binding.etParentPhone.text.toString())) {
            binding.etParentPhoneLayout.error = "الرجاء إدخال رقم هاتف صحيح"
            message = "الرجاء إدخال رقم هاتف صحيح"
            isValid =  false
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
    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        val PHONE_PATTERN = "^01[0-2,5]\\d{1,8}$"
        val pattern = Pattern.compile(PHONE_PATTERN)
        val matcher = pattern.matcher(phoneNumber)
        return matcher.matches()
    }
    private fun validateNationalId(nationalId: String): Boolean {
        val NATIONAL_ID_PATTERN = "^[0-9]{14}$"
        val pattern = Pattern.compile(NATIONAL_ID_PATTERN)
        val matcher = pattern.matcher(nationalId)
        return matcher.matches()
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
//            imageBody(requireContext(), mainImage!!, "image")
            val TAG = "imagePicker"
            Log.e(TAG, "mainImage : ${mainImage.toString()}")
            Log.e(TAG, "birthImage: ${birthImage.toString()}")
            createKidViewModel.createKidnappedKid(
                name = binding.etName.text.toString(),
                image = tryBody(prepareBitmap(mainImage!!),"image"),
                other_info = binding.etDescription.text.toString(),
                birth_image = secondTryBody(prepareBitmap(birthImage!!), "birth_image"),
                status = "not_found",
                kid_national_id = binding.etKidNationalId.text.toString(),
                city = binding.etCityAutoCompleteTextView.text.toString(),
                sub_city = binding.etSubCityAutoCompleteTextView.text.toString(),
                parent_name = binding.etParentName.text.toString(),
                parent_address = binding.etParentAddress.text.toString(),
                parent_national_id = binding.etParentID.text.toString(),
                parent_phone_number = binding.etParentPhone.text.toString(),
                parent_other_info = "هذة الحقل لا يتم الاستخدامة في المشروع التخرج",
                kidnap_date = binding.etKidnappedDate.text.toString(),
                age = binding.etAge.text.toString(),
                parent_image = therdTryBody(prepareBitmap(parentImage!!), "parent_image")
            )
//            parent_image = aanotherImageBody(requireContext(), parentImage!!, "parent_image")

            createKidViewModel.createKidnappedKidResponse.observe(viewLifecycleOwner) { response ->

                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            hideLoader()
                            findNavController().navigate(R.id.action_createParentCaseFragment_to_watingCasesFragment)
                            missingInputDialog("تم إنشاء القضية بنجاح" , "انتظر حتى يتم مراجعة قضيتك والموافقة عليها")
                            Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_LONG).show()
                        }
                    }
                    is NetworkResult.Error -> {
                        hideLoader()
                        if (response.message == "يرجى التأكد من الصورة الختارة للشخص"){
                            missingInputDialog("خطأ في الصورة المختارة","يرجى التأكد من الصورة الختارة للشخص")
                        }else{
                            missingInputDialog("حدث خطأ",response.message.toString())
                        }
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
        statusBarColor = "#1877F2",
        isLightStatusBar = false,
        isFolderMode = true,
        maxSize = 1,
        isMultipleMode = true,
        backgroundColor = "#FFFFFFFF",
        isShowCamera = false,
        toolbarColor = "#1877F2",
        doneTitle = "تم",
        limitMessage = "الحد الاقصى للتحميل هو صورة واحدة فقط",
        folderTitle = "اختر الصورة الذي تريدها",
        rootDirectory = RootDirectory.DCIM,
        selectedIndicatorColor = "#1877F2",
        subDirectory = "Photos",
        folderGridCount = GridCount(2, 4),
        imageGridCount = GridCount(3, 5),
    )


//    private suspend fun compressedImageFile(uri: Uri): Uri {
//        val compressedImageFile =
//            Compressor.compress(
//                requireContext(),
//                FileUtil.getFileFromUri(requireContext(), uri),
//                Dispatchers.IO
//            ) {
//                resolution(1280, 1280)
//                quality(80)
//                format(Bitmap.CompressFormat.PNG)
//                size(2_097_152 / 2) // 2 MB
//            }
//
//        return compressedImageFile.toUri()
//    }
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
//                    mainImage = compressedImageFile(image)
                    binding.ivMain.load(mainImage)
                }
            }
        }

    }
}