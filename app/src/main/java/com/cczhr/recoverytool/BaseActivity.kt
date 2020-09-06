package com.cczhr.recoverytool

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.cczhr.recoverytool.utils.FileUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.main.*


/**
 * @author cczhr
 * @since  2020/9/6
 * @description https://github.com/cczhr
 */
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var filePathResult: (String) -> Unit
    private lateinit var permissionsResult: (Boolean) -> Unit
    protected abstract val layoutId: Int
    protected abstract fun init()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        init()
    }

    fun selectFile(result: (String) -> Unit) {
        filePathResult = result
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(this, 1)
        }
    }


    fun requestPermissions(result: (Boolean) -> Unit) {
        permissionsResult = result
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        var isGetAllPermissions = true
        for (p in permissions) {
            if (ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_DENIED) {
                isGetAllPermissions = false
                break
            }
        }
        if (!isGetAllPermissions) {
            ActivityCompat.requestPermissions(this, permissions, 100)
        } else {
            permissionsResult.invoke(true)
        }
    }


    fun fabAnimate(fab:FloatingActionButton){
        fab.rotation = 0f
        ViewCompat.animate(fab)
            .rotation(360f)
            .withLayer()
            .setDuration(1000)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        permissionsResult.invoke(false)
                        return
                    }
                }
                permissionsResult.invoke(true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == RESULT_OK) {
                    val uri = data?.data
                    if (uri != null) {
                        filePathResult.invoke(FileUtils(this).getPath(uri) ?: "")
                    }
                }
            }
        }
    }
}