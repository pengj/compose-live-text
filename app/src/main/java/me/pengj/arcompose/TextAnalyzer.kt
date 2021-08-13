package me.pengj.arcompose

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.text.MLLocalTextSetting
import com.huawei.hms.mlsdk.text.MLText

private const val TAG = "TextAnalyzer"
class TextAnalyzer(private val onTextDetected: (MLText) -> Unit) : ImageAnalysis.Analyzer {

    private val  setting = MLLocalTextSetting.Factory()
        .setOCRMode(MLLocalTextSetting.OCR_TRACKING_MODE)
        .setLanguage("en")
        .create()

    private val analyzer =  MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting)

    private var inprogress = false

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        if (inprogress) {
            return
        }

        imageProxy.image?.let { image ->
            inprogress = true

            analyzer.asyncAnalyseFrame(MLFrame.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees))
                .addOnSuccessListener { mlText ->
                    mlText?.let {
                        onTextDetected.invoke(it)
                    }
                    inprogress = false
                    imageProxy.close()
                }.addOnFailureListener {
                    Log.e(TAG, it.toString())
                    inprogress = false
                    imageProxy.close()
                }
        }
    }
}