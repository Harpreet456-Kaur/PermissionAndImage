package com.example.permissionandimage

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.permissionandimage.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import java.security.Permission

class MainActivity : AppCompatActivity() {
    var storageRef=FirebaseStorage.getInstance()
     var binding: ActivityMainBinding?=null

    var imagePermission=registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Permission not granted",Toast.LENGTH_LONG).show()
        }

    }
    var pickImage=registerForActivityResult(ActivityResultContracts.GetContent()){
        System.out.println("in PickImage $it")
        binding?.ivPic?.setImageURI(it)
        it?.let { it1->
            storageRef.getReference("Gallery").putFile(it1).addOnSuccessListener {
                    UploadTask->System.out.println("UploadTask ${UploadTask.storage.downloadUrl}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnGet?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                pickImage.launch("image/*")
            }else{
                imagePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}