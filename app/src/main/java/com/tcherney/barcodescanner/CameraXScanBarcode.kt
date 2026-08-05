package com.tcherney.barcodescanner

import android.graphics.Rect
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import kotlinx.coroutines.delay

//TODO put this into the home screen
@Composable
fun BarcodeScanner(onBarodeScan: (String) -> Unit, modifier: Modifier = Modifier) {
    var barcode by remember {mutableStateOf<String?>(null)}
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var barcodeScanned by remember { mutableStateOf(false)}
    var boundingRect by remember { mutableStateOf<Rect?>(null)}
    val cameraController = remember {LifecycleCameraController(context)}
    AndroidView(modifier = modifier.fillMaxSize(),
    factory = { ctx ->
        PreviewView(ctx).apply {
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_EAN_13,
                    Barcode.FORMAT_EAN_8,
                    Barcode.FORMAT_AZTEC,
                    Barcode.FORMAT_CODE_39,
                    Barcode.FORMAT_CODE_93,
                    Barcode.FORMAT_CODE_128
                )
                .build()
            val barcodeScanner = BarcodeScanning.getClient(options)
            cameraController.setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(ctx),
                MlKitAnalyzer(
                    listOf(barcodeScanner),
                    ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                    ContextCompat.getMainExecutor(ctx)
                ) { result: MlKitAnalyzer.Result? ->
                    val barcodeResults = result?.getValue(barcodeScanner)
                    if(!barcodeResults.isNullOrEmpty()) {
                        barcode = barcodeResults.first().rawValue
                        barcodeScanned = true
                        boundingRect = barcodeResults.first().boundingBox
                    }

                }
            )
            cameraController.bindToLifecycle(lifecycleOwner)
            this.controller = cameraController
        }
    })
    if (barcodeScanned) {
        LaunchedEffect(Unit) {
            delay(100)
            onBarodeScan(barcode ?: "")
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