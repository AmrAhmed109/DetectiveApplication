package com.example.detectiveapplication.feature.home.faceDetection

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentFaceDetectionBinding
import com.example.detectiveapplication.dto.recognition.RecognitionData
import com.example.detectiveapplication.utils.Constants.Companion.imageBody
import com.example.detectiveapplication.utils.NetworkResult
import com.google.android.material.snackbar.Snackbar
import com.maxkeppeler.sheets.info.InfoSheet
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import java.io.ByteArrayOutputStream
import java.io.File


@AndroidEntryPoint
class FaceDetectionFragment : Fragment() {
    private var _binding: FragmentFaceDetectionBinding? = null
    private val binding get() = _binding!!
    lateinit var faceDetectionModel: FaceDetectionModel
    val CAPTURE_REQUEST_CODE = 5
    private lateinit var dialogLoader: Dialogloader

    companion object {
        var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
    fun showLoader(){
        dialogLoader.show()
    }

    fun hideLoader(){
        dialogLoader.hide()
    }
    val text : MutableLiveData<String> = MutableLiveData()
    var image : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faceDetectionModel = ViewModelProvider(this)[FaceDetectionModel::class.java]
    }
    fun addSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.green)).show()
    }
    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                dispatchTakePictureIntent()
            }
        }

    fun apiCall(image: Uri) {
        faceDetectionModel.recognize(image = imageBody(requireContext(), image!!, "image"))
        faceDetectionModel.recognitionResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
//                    Toast.makeText(
//                        requireContext(),
//                        response.data?.code.toString(),
//                        Toast.LENGTH_LONG
//                    ).show()
                    hideLoader()
                    val action = FaceDetectionFragmentDirections.actionFaceDetectionFragmentToResultFragment(response.data)
                    findNavController().navigate(action)

                }
                is NetworkResult.Error -> {
                    hideLoader()
                    if (response.message == "error-face-not-found-or-many-faces"){
                        missingInputDialog("خطأ في الصورة المختارة","يرجى التأكد من الصورة الختارة للشخص")
                    }else if (response.message == "we can not found any kid in database"){
                        missingInputDialog("لم يتم التعرف علية","لا يوجد هذا الشخص في قاعدة بياناتنا, يمكنك إنشاء قضية")
                    }
                    else{
                        missingInputDialog("حدث خطأ",response.message.toString())
                    }
//                    toast(response.message.toString())
//                    Toast.makeText(requireContext(), "Error: ${response.message.toString()}", Toast.LENGTH_LONG).show()
                    Log.d("NetworkResult.Error", "requestApiData: ${response.message.toString()}")
                }
                is NetworkResult.Loading -> {
                    showLoader()
//                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                }
            }
        }
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
    fun GOInputDialog(title: String, message: String) {
        InfoSheet().show(requireActivity()) {
            title(title)
            content(message)
            onPositive("لا") {
                // Handle event
            }
            onNegative("حسنا") {

            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaceDetectionBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        text.value = ""
        binding.btnTakePicture.setOnClickListener {
            permReqLauncher.launch(PERMISSIONS)
        }
        text.observe(viewLifecycleOwner) {
//            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            image?.let {
                apiCall(getImageUri(it))
            }
        }
        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun dispatchTakePictureIntent() {
        if (hasPermissions(activity as Context, PERMISSIONS)) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, CAPTURE_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                log("error ${e.message}")
            }
        } else {
            permReqLauncher.launch(
                PERMISSIONS
            )
        }
    }


    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun log(message: String) {
        Log.d("image", "log: $message")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

           // getSelectedImageHere
            image = imageBitmap
            log(imageBitmap.toString())
//            getImageUri(imageBitmap)
            log(getImageUri(imageBitmap).toString())
            text.value = "Image captured"
//            log("went here")
//            toast(imageBitmap.toString())

        }
    }

    private suspend fun compressedImageFile(uri: Uri): Uri {
        val compressedImageFile = Compressor.compress(requireContext(), File(uri.toString())) {
            resolution(1280, 1280)
            quality(80)
            format(Bitmap.CompressFormat.PNG)
            size(2_097_152/2) // 2 MB
        }

        return compressedImageFile.toUri()
    }


    fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            requireActivity().contentResolver,
            inImage,
            inImage.toString(),
            null
        )
        return Uri.parse(path)
    }


    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        _binding = null

    }

}