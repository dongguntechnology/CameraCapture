package com.dongguninnovatiion.cameracapture

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dongguninnovatiion.cameracapture.databinding.ActivityPermissionBinding
import com.dongguninnovatiion.cameracapture.utils.Permissions
import kotlinx.coroutines.flow.internal.NoOpContinuation.context


class PermissionActivity : AppCompatActivity() {
    private val binding: ActivityPermissionBinding by lazy {
        ActivityPermissionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkNRequetPremission()
    }

    private fun checkNRequetPremission(){
        if(Permissions.hasPermissions(this)) startAct()
        else Permissions.requestPermission(this, this)
    }

    private fun startAct() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }

    }
}