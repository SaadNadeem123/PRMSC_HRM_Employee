package com.lmkr.prmscemployeeapp.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData
import com.lmkr.prmscemployeeapp.databinding.ActivityCameraXBinding
import com.lmkr.prmscemployeeapp.ui.cameraxUtils.FaceContourDetectionProcessor
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraXActivity : AppCompatActivity() {

    private var userData: UserData? = null
    private lateinit var viewBinding: ActivityCameraXBinding
    private var imageCapture: ImageCapture? = null

    private var imageAnalyzer: ImageAnalysis? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraXBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        userData = SharedPreferenceHelper.getLoggedinUser(this@CameraXActivity)

        viewBinding.imageCaptureButton.isEnabled = false

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PRMSCEmployeeApp")
            }
        }


        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val path = getRealPathFromURI(output.savedUri!!)

                    val msg = "Photo capture succeeded: $path"

//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

                    try {
                        SharedPreferenceHelper.saveString(AppWideWariables.FACE_LOCK_PATH,path.toString(),this@CameraXActivity)

                        SharedPreferenceHelper.saveString(
                            AppWideWariables.ATTENDANCE_TIME,
                            AppUtils.getAttendanceTime(),
                            this@CameraXActivity
                        );
                        finish()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Log.d(TAG, msg)
                }
            })
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        //Log.e("in","conversion"+contentURI.getPath());
        val path: String?
        val cursor = contentResolver
            .query(contentURI, null, null, null, null)
        path = if (cursor == null) contentURI.path else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
        cursor?.close()
        return path
    }

    fun importFile(returnIntent: Intent) {
        Thread {
            val returnUri = returnIntent.data
            //        String fileName = getFileName(returnUri);

            /*
                 * Get the file's content URI from the incoming Intent,
                 * then query the server app to get the file's display name
                 * and size.
                 */
            val returnCursor = contentResolver.query(returnUri!!, null, null, null, null)
            /*
                 * Get the column indexes of the data in the Cursor,
                 * move to the first row in the Cursor, get the data,
                 * and display it.
                 */
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            val fileName = returnCursor.getString(nameIndex)
            val mimeType = contentResolver.getType(returnUri)
            val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
           /* val image = Image()
            image.setFileName(fileName)
            image.setUri(returnUri)
            image.setThumbnailBase64(FileUtils.fileUriToThumbnail(returnUri, contentResolver))
            //                image.setFileContentBase64(FileUtils.fileUriToBase64refined(returnUri, getContentResolver()));
//                image.setBytes(FileUtils.fileUriTobytesrefined(returnUri, getContentResolver()));
            image.setFileContentBase64(FileUtils.fileUriToBase64(returnUri, contentResolver))
            image.setBytes(FileUtils.fileUriToBytes(returnUri, contentResolver))
            image.setOrderId(orderId)
            image.setOrderDetailId(detailId)
            image.setShipperId(shipperId)
            image.setProductId(orderSku.getProductId())
            image.setImageType(AppWideWariables.IMAGE_TYPE_SKU)
            images.add(image)
            updateImages()*/
        }.run()
    }


    private fun captureVideo() {}

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetResolution(Size(720,1280))
                .build().also {

                it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(720,1280))
                .build()
            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA


            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also {
                    it.setAnalyzer(cameraExecutor, selectAnalyzer())
                }



            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun selectAnalyzer(): ImageAnalysis.Analyzer {
        return FaceContourDetectionProcessor(
            viewBinding.graphicOverlayFinder, viewBinding.imageCaptureButton
        )
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS = mutableListOf(
            android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle Permission granted/rejected
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in REQUIRED_PERMISSIONS && it.value == false) permissionGranted = false
        }
        if (!permissionGranted) {
            Toast.makeText(
                baseContext, "Permission request denied", Toast.LENGTH_SHORT
            ).show()
        } else {
            startCamera()
        }
    }
}