package com.example.detectiveapplication.feature.home.faceDetection

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentFaceDetectionBinding
import com.example.detectiveapplication.dto.recognition.RecognitionData
import com.example.detectiveapplication.utils.Constants.Companion.imageBody
import com.example.detectiveapplication.utils.NetworkResult
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

    companion object {
        var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }

    val text : MutableLiveData<String> = MutableLiveData()
    var image : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faceDetectionModel = ViewModelProvider(this)[FaceDetectionModel::class.java]
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
                    val action = FaceDetectionFragmentDirections.actionFaceDetectionFragmentToResultFragment(response.data)
                    findNavController().navigate(action)

                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), "Error: ${response.message.toString()}", Toast.LENGTH_LONG).show()
                    Log.d("NetworkResult.Error", "requestApiData: ${response.message.toString()}")
                }
                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaceDetectionBinding.inflate(inflater, container, false)

        text.value = ""
        binding.btnTakePicture.setOnClickListener {
            permReqLauncher.launch(PERMISSIONS)
        }
        text.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
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