package com.dongguninnovatiion.cameracapture

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dongguninnovatiion.cameracapture.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var photoUri: Uri? = null
    private lateinit var photoPath: String

    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var chooseImageLauncher: ActivityResultLauncher<Intent>

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>

    private lateinit var galleryLauncher:  ActivityResultLauncher<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)        // binding.root >> Root View 라는 레이아웃에서 가장 바깥쪽의 View Container 를 참조
                                            // 즉, 호출 시 XML에 있는 ConstraintLayout의 Root View를 반환한다는 의미이다.

        initClickListener()

        initLauncher()
    }

    private fun  initLauncher() {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                binding.imagePhoto.setImageURI(photoUri)        // imagePhoto : activity_main.xml ImageView id, setImageURI : 이미지 데이터 바인딩
            }
        }

        chooseImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                binding.imagePhoto.setImageURI(result.data?.data)        // imagePhoto : activity_main.xml ImageView id, setImageURI : 이미지 데이터 바인딩
            }
        }
    }
    private fun initClickListener() {
        binding.apply{
            btnCamera.setOnClickListener{
                openCamera()
            }
            
            btnGallery.setOnClickListener {
                openGallery()
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)     // ACTION_IMAGE_CAPTURE >> 카메라 앱을 시작시키고 찍은 사진을 받을 수 있게 해준
                                                                            // MediaStore에는 이미지, 비디오, 음악 등의 미디어를 처리하는 public 인터페이스가 정의되어 있음
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val photoFile = File.createTempFile(
            "PHOTO_${timeStamp}",
            ".bmp",
           storageDir
        ).apply {
            photoPath = absolutePath    // 일반적으로 파일을 저장하거나 읽을 때는 절대 경로를 기준으로 사용
        }

        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
             photoFile
        )

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)   // EXTRA_OUTPUT >> 사진 파일을 저장할 위치를 가리키는 Uri를 엑스트라의 값으로 설정해 인텐트에 전달
        takePictureLauncher.launch(takePictureIntent)
    }

    private fun openGallery() {
        val chooseImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        chooseImageLauncher.launch(chooseImageIntent)
    }
}