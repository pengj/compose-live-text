package me.pengj.arcompose


import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.huawei.hms.mlsdk.text.MLText

class LiveTextViewModel : ViewModel() {

    var detectedTextBlock: List<MLText.Block> by mutableStateOf(emptyList())
        private set

    private var detectedText = ""

    fun updateMLText(mlText: MLText) {
        if(mlText.stringValue.isEmpty()) {
            detectedTextBlock = emptyList()
            return
        }
        val textBlocks = ArrayList<MLText.Block>()
        mlText.blocks.forEach {
            textBlocks.add(it)
        }
        detectedTextBlock = textBlocks
        detectedText = mlText.stringValue
    }

    fun shareTextIntent(): Intent {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, detectedText)
            type = "text/plain"
        }

        return Intent.createChooser(sendIntent, null)
    }
}