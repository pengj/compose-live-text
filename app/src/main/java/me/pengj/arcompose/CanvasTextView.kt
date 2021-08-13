package me.pengj.arcompose

import android.graphics.Color
import android.graphics.Path
import android.graphics.Point
import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.huawei.hms.mlsdk.text.MLText

@ExperimentalAnimationApi
@Composable
fun CanvasTextView (
    text: List<MLText.Block>,
    onClick: (String) -> Unit) {
    AnimatedVisibility(visible = text.isNotEmpty()) {
        val paint = Paint().asFrameworkPaint()
        Canvas(modifier = Modifier.fillMaxSize()) {
            paint.apply {
                isAntiAlias = true
                textSize = 48f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                color = Color.WHITE
            }

            text.forEach {
                drawText(this.drawContext.canvas, it, paint)
            }
        }
    }
}

private fun drawText(canvas: Canvas, text: MLText.Block, paint: NativePaint) {
    val points: Array<Point> = text.vertexes
    if (points.size == 4) {
        for (i in points.indices) {
            points[i].x = translateX(points[i].x).toInt()
            points[i].y = translateY(points[i].y).toInt()
        }
        val pts = floatArrayOf(
            points[0].x.toFloat(), points[0].y.toFloat(), points[1].x.toFloat(), points[1].y.toFloat(),
            points[1].x.toFloat(), points[1].y.toFloat(), points[2].x.toFloat(), points[2].y.toFloat(),
            points[2].x.toFloat(), points[2].y.toFloat(), points[3].x.toFloat(), points[3].y.toFloat(),
            points[3].x.toFloat(), points[3].y.toFloat(), points[0].x.toFloat(), points[0].y.toFloat()
        )
        val averageHeight: Float =
            (points[3].y - points[0].y + (points[2].y - points[1].y)) / 2.0f
        val textSize = averageHeight * 0.7f
        val offset = averageHeight / 4
        paint.textSize = textSize
        canvas.nativeCanvas.drawLines(pts, paint)
        val path = Path()
        path.moveTo(points[3].x.toFloat(), points[3].y - offset)
        path.lineTo(points[2].x.toFloat(), points[2].y - offset)
        canvas.nativeCanvas.drawLines(pts, paint)
        canvas.nativeCanvas.drawTextOnPath(text.getStringValue(), path, 0f, 0f, paint)
    }
}

private fun translateX(x: Int) : Double {
    //TODO: the ratio need to be updated based on the screen ratio
    return x * 1.4
}

private fun translateY(y: Int) : Double {
    //TODO: the ratio need to be updated based on the screen ratio
    return y * 1.4
}