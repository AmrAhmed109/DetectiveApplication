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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentFaceDetectionBinding
import com.example.detectiveapplication.utils.Constants.Companion.imageBody
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
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
                    Toast.makeText(
                        requireContext(),
                        response.data?.code.toString(),
                        Toast.LENGTH_LONG
                    ).show()
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

        permReqLauncher.launch(PERMISSIONS)
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
            log(imageBitmap.toString())
//            getImageUri(imageBitmap)
            log(getImageUri(imageBitmap).toString())
            apiCall(getImageUri(imageBitmap))
            log("went here")
            toast(imageBitmap.toString())

        }
    }



    fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            requireActivity().contentResolver,
            inImage,
            "Title",
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
        _binding = null

    }

}