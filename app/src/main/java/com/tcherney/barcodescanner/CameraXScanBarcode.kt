package com.tcherney.barcodescanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

//TODO build version using camerax that scans without leaving app, can have a preview window but not necessary
@Composable
fun BarcodeScanner() {
    var barcode by remember {mutableStateOf("")}
}