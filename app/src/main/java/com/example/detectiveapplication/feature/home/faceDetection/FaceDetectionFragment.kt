package com.example.detectiveapplication.feature.home.faceDetection

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.example.detectiveapplication.databinding.FragmentFaceDetectionBinding
import com.priyankvasa.android.cameraviewex.ErrorLevel
import com.priyankvasa.android.cameraviewex.Image
import com.priyankvasa.android.cameraviewex.Modes


class FaceDetectionFragment : Fragment() {
    private var _binding: FragmentFaceDetectionBinding? = null
    private val binding get() = _binding!!

    val CAPTURE_REQUEST_CODE = 5


    companion object {
        var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
    private val permReqLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all {
            it.value == true
        }
        if (granted) {
            dispatchTakePictureIntent()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaceDetectionBinding.inflate(inflater, container, false)



        toast( binding.camera.isCameraOpened.toString())
        binding.camera.addCameraOpenedListener { /* Camera opened. */
            toast("opened")
        }
            .addCameraErrorListener { t: Throwable, errorLevel: ErrorLevel -> /* Camera error! */ }
            .addCameraClosedListener { /* Camera closed. */ }
        binding.camera.addPictureTakenListener { image: Image ->
        toast(image.toString())
        }

       binding.camera.apply {
           setCameraMode(Modes.CameraMode.SINGLE_CAPTURE)
           enableCameraMode(Modes.CameraMode.SINGLE_CAPTURE)
           addPictureTakenListener { image: Image -> /* Picture taken. */ }
           disableCameraMode(Modes.CameraMode.SINGLE_CAPTURE)
       }

        log("asdasd")
        permReqLauncher.launch(PERMISSIONS)



        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun dispatchTakePictureIntent(){
        if (hasPermissions(activity as Context, PERMISSIONS)) {

//            binding.camera.capture()
            val takePictureIntent  = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent , CAPTURE_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                log("error ${e.message}")
            }
        }else {
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
        if (requestCode == CAPTURE_REQUEST_CODE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
                log(imageBitmap.toString())
                log("went here")
                toast(imageBitmap.toString())

        }
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