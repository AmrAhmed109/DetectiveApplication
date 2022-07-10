package com.example.detectiveapplication.feature.create_case

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentCreateStrangerCaseBinding
import com.example.detectiveapplication.utils.City
import com.example.detectiveapplication.utils.Constants
import com.example.detectiveapplication.utils.Constants.Companion.imageBody
import com.example.detectiveapplication.utils.NetworkResult
import com.maxkeppeler.sheets.calendar.CalendarSheet
import com.maxkeppeler.sheets.calendar.SelectionMode
import com.maxkeppeler.sheets.info.InfoSheet
import com.nguyenhoanglam.imagepicker.model.GridCount
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.model.RootDirectory
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import java.util.*
import java.util.regex.Pattern


class CreateStrangerCaseFragment : Fragment() {
    private var _binding: FragmentCreateStrangerCaseBinding? = null
    private val binding get() = _binding!!
    private var mainImage : Uri? = null
    var subCity: List<String> = listOf()
   lateinit var createStrangerCaseViewModel: CreateStrangerCaseViewModel
    val TAG = "CreateStranger"
    private lateinit var dialogLoader: Dialogloader


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
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etKidnappedTimeInputLayout.setOnClickListener {
//            addDatePicker()
            addAnotherDatePicker()
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
        binding.etCityAutoCompleteTextView.setOnClickListener {
            hideCityKeyboard()
        }
        binding.etSubCityAutoCompleteTextView.setOnClickListener {
            hideSubCityKeyboard()
        }
        return binding.root
    }
    private fun hideCityKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etCityAutoCompleteTextView.windowToken, 0)
    }
    private fun hideSubCityKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSubCityAutoCompleteTextView.windowToken, 0)
    }
    fun showLoader(){
        dialogLoader.show()
    }
    fun hideLoader(){
        dialogLoader.hide()
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
                        hideLoader()
//                        log("Success")
                        response.data?.let {
                            toast(it.message)
                        }
                        missingInputDialog("تم إنشاء القضية بنجاح" , "انتظر حتى يتم مراجعة قضيتك والموافقة عليها")
                        findNavController().navigate(R.id.action_createParentCaseFragment_to_watingCasesFragment)
                    }
                    is NetworkResult.Loading -> {
                        showLoader()
//                        toast("Loading")
                    }
                    is NetworkResult.Error -> {
                        hideLoader()
                        if (response.message == "error-face-not-found-or-many-faces"){
                            missingInputDialog("خطأ في الصورة المختارة","يرجى التأكد من الصورة الختارة للشخص")
                        }else{
                            missingInputDialog("حدث خطأ",response.message.toString())
                        }
//                        toast("Error : ${response.message.toString()}")
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
        var message = "الرجاء إدخال جميع الحقول"
        if (binding.etName.text.toString().isEmpty()) {
            binding.etNameInputLayout.error = "الرجاء إدخال اسم الشخص"
            message = "الرجاء إدخال اسم الشخص"
            isValid =  false
        }
        if (mainImage == null) {
//            toast("يرجى اضافة صورة للشخص المعثور علية")
            message = "يرجى اضافة صورة للشخص المعثور علية"
            isValid =  false
        }
        if (binding.etKidnappedDate.text.toString().isEmpty()) {
//            toast("يرجى إدخال تاريخ الاختفاء")
            message = "يرجى إدخال تاريخ الذي وجدت فية الشخص"
            isValid =  false
        }

        if (binding.etDescription.text.toString().length < 50) {
            binding.etDesriptionLayout.error = "الرجاء إدخال تفاصيل اكثر عن الحالة"
            message = "الرجاء إدخال اكثر تفاصيل الحالة"
            isValid =  false
        }

        if (binding.etDescription.text.toString().isEmpty()) {
            binding.etDesriptionLayout.error = "الرجاء إدخل تفاصيل الحالة"
            message = "الرجاء إدخل تفاصيل الحالة"
            isValid =  false
        }

        if (binding.etSubCityAutoCompleteTextView.text.isEmpty()) {
            binding.etSubCityInputLayout.error = "الرجاء إدخال المدينة"
            message = "الرجاء إدخال المدينة"
            isValid =  false
        }
        if (binding.etCityAutoCompleteTextView.text.isEmpty()) {
            binding.etCityInputLayout.error = "الرجاء إدخال المحافظة"
            message = "الرجاء إدخال المحافظة"
            isValid =  false
        }
        if (binding.etParentAddress.text.toString().isEmpty()) {
            binding.etParentAddressLayout.error = "الرجاء إدخال عنوان ولي الامر"
            message = "الرجاء إدخال عنوان ولي الامر"
            isValid =  false
        }
        if (binding.etParentName.text.toString().isEmpty()) {
            binding.etParentNameLayout.error = "الرجاء إدخال اسم ولي الامر"
            message = "الرجاء إدخال اسم ولي الامر"
            isValid =  false
        }
        if (binding.etParentID.text.toString().isEmpty()) {
            binding.etParentIDLayout.error = "الرجاء إدخال الرقم القومي"
            message = "الرجاء إدخال الرقم القومي"
            isValid =  false
        }
        if (!validateNationalId(binding.etParentID.text.toString())) {
            binding.etParentIDLayout.error = "الرجاء إدخال الرقم القومي صحيح"
            message = "الرجاء إدخال الرقم القومي صحيح"
            isValid =  false
        }
        if (binding.etParentPhone.text.toString().isEmpty()) {
            binding.etParentPhoneLayout.error = "الرجاء إدخال رقم هاتف ولي الامر"
            message = "الرجاء إدخال رقم هاتف ولي الامر"
            isValid =  false
        }
        if (!validatePhoneNumber(binding.etParentPhone.text.toString())) {
            binding.etParentPhoneLayout.error = "الرجاء إدخال رقم هاتف صحيح"
            message = "الرجاء إدخال رقم هاتف صحيح"
            isValid =  false
        }
        if (!isValid){
            missingInputDialog(title = "تأكد من ملئ البيانات بشكل صحيح", message = message)
            return false
        }
        return true
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
    private fun handleSpinner(){
        val name = arrayListOf<String>()
        val city = Constants.listOfCites()

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
}