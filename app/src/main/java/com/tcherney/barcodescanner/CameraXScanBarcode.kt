package com.tcherney.barcodescanner

import android.graphics.Rect
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

//TODO build version using camerax that scans without leaving app, can have a preview window but not necessary
@Composable
fun BarcodeScanner(onBarodeScan: (String) -> Unit, modifier: Modifier = Modifier) {
    var barcode by remember {mutableStateOf("")}
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var barcodeScanned by remember { mutableStateOf(false)}
    var boundingRect by remember { mutableStateOf<Rect?>(null)}
    val cameraController = remember {LifecycleCameraController(context)}
    AndroidView(modifier = modifier.fillMaxSize(),
    factory = { ctx ->
        PreviewView(ctx).apply {
            //TODO
        }
    })
    if (barcodeScanned) {
        LaunchedEffect(Unit) {
            delay(100)
            onBarodeScan(barcode)
        }
        DrawPreview(rect = boundingRect)
    }
}
@Composable
fun DrawPreview(rect: Rect?) {
    val composeRect = rect?.toComposeRect()
    composeRect?.let {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = Color.Red,
                topLeft = Offset(it.left, it.top),
                size = Size(it.width, it.height),
                style = Stroke(width = 5f)
            )
        }
    }
}