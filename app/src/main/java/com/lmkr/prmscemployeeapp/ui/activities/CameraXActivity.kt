package com.lmkr.prmscemployeeapp.ui.activities

import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import com.lmkr.prmscemployeeapp.data.webservice.api.ApiCalls
import com.lmkr.prmscemployeeapp.data.webservice.api.JsonObjectResponse
import com.lmkr.prmscemployeeapp.data.webservice.api.Urls
import com.lmkr.prmscemployeeapp.data.webservice.models.ApiBaseResponse
import com.lmkr.prmscemployeeapp.data.webservice.models.UserData
import com.lmkr.prmscemployeeapp.databinding.ActivityCameraXBinding
import com.lmkr.prmscemployeeapp.ui.cameraxUtils.FaceContourDetectionProcessor
import com.lmkr.prmscemployeeapp.ui.utilities.AppUtils
import com.lmkr.prmscemployeeapp.ui.utilities.AppWideWariables
import com.lmkr.prmscemployeeapp.ui.utilities.SharedPreferenceHelper
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


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
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
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
                    val msg =
                        "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()


                    try {
                        callCheckInApi(name, output.savedUri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    Log.d(TAG, msg)
                }
            })
    }

    // And to convert the image URI to the direct file system path of the image file
    open fun getRealPathFromURI(contentUri: Uri?): String? {

        // can post image
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(
            contentUri!!,
            proj,  // Which columns to return
            null,  // WHERE clause; which rows to return (all rows)
            null,  // WHERE clause selection arguments (none)
            null
        ) // Order-by clause (ascending by name)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor!!.moveToFirst()
        return cursor!!.getString(column_index)
    }

    private fun callCheckInApi(name: String, savedUri: Uri?) {

        val user = SharedPreferenceHelper.getLoggedinUser(this@CameraXActivity)

        /*
         * Get the column indexes of the data in the Cursor,
         * move to the first row in the Cursor, get the data,
         * and display it.
         */
//        val file = File("/storage/emulated/0/Pictures/CameraX-Image/$name")
//        val file = FileUtils.getPath(this@CameraXActivity, savedUri)
        val file = getRealPathFromURI(savedUri)

        val filePart = MultipartBody.Part.createFormData(
            "file", name, RequestBody.create(
                MediaType.parse("image/*"), file
            )
        )

        val httpClient = OkHttpClient.Builder().retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.MINUTES)
            .writeTimeout(
                30,
                TimeUnit.MINUTES
            ) //                .addInterceptor(new NetInterceptor())
            /*                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String token = user.getToken();
                        request = request.newBuilder()
                                .addHeader("Authorization", token)
                                .build();
                        return chain.proceed(request);
                    }
                })*/
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiCalls.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
        val urls = retrofit.create(Urls::class.java)
        /* val body = JsonObject()
         body.addProperty("employee_id", userData!!.basicData[0].id)
         body.addProperty("checkin_time", AppUtils.getCurrentDateTimeGMT5String())
         body.addProperty("lat", SharedPreferenceHelper.getString("lat", this@CameraXActivity))
         body.addProperty(
             "longitude", SharedPreferenceHelper.getString("long", this@CameraXActivity)
         )
         body.addProperty("source", AppWideWariables.SOURCE_MOBILE)
         body.addProperty("file_name", name)
         body.addProperty("file_path", "")*/

        val employee_id: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            user.basicData.get(0).id.toString() + ""
        )
        val checkin_time: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            AppUtils.getCurrentDateTimeGMT5String()
        )
        val lat: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            SharedPreferenceHelper.getString("lat", this@CameraXActivity)
        )
        val longitude: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            SharedPreferenceHelper.getString("long", this@CameraXActivity)
        )
        val source: RequestBody =
            RequestBody.create(MediaType.parse("text/plain"), AppWideWariables.SOURCE_MOBILE)
        val file_name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), name)
        val file_path: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "path")


        val call = urls.checkInMultipart(
            AppUtils.getStandardHeaders(SharedPreferenceHelper.getLoggedinUser(this@CameraXActivity)),
            filePart,
            employee_id,
            checkin_time,
            lat,
            longitude,
            source,
            file_name,
            file_path
        )
        call.enqueue(object : Callback<ApiBaseResponse?> {
            override fun onResponse(
                call: Call<ApiBaseResponse?>, response: Response<ApiBaseResponse?>
            ) {
                Log.i("response", response.toString())

                if (!AppUtils.isErrorResponse(response,this@CameraXActivity)) {

                    if (!response.isSuccessful) {
//                    tv.setText("Code :" + response.code());
                        return
                    }
                    SharedPreferenceHelper.saveBoolean(
                        AppWideWariables.IS_CHECKED_IN, true, this@CameraXActivity
                    )
                }
            }

            override fun onFailure(call: Call<ApiBaseResponse?>, t: Throwable) {
                t.printStackTrace()
                AppUtils.makeNotification(t.toString(), this@CameraXActivity)
                Log.i("response", t.toString())
                //                tv.setText(t.getMessage());
            }
        })
    }

    private fun captureVideo() {}

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()
            // Select back camera as a default
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA


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