package me.pengj.arcompose

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import me.pengj.arcompose.ui.theme.ARComposeTheme

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<LiveTextViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (allPermissionsGranted()) {
            setViewContent()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }


    override fun onResume() {
        super.onResume()
        window.decorView.postDelayed({
            window.decorView.systemUiVisibility = FLAGS_FULLSCREEN
        }, IMMERSIVE_FLAG_TIMEOUT)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setViewContent()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_message),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun setViewContent() {
        setContent {
            ARComposeTheme {
                SimpleCameraPreview(
                    TextAnalyzer{ viewModel.updateMLText(it) })
                CanvasTextView(viewModel.detectedTextBlock){

                }
                ActionButton(viewModel.detectedTextBlock.isNotEmpty()) {
                    startActivity(viewModel.shareTextIntent())
                }
            }
        }
    }



    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val IMMERSIVE_FLAG_TIMEOUT = 500L
        const val FLAGS_FULLSCREEN =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}
