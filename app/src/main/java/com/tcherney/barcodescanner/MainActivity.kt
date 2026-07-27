package com.tcherney.barcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.tcherney.barcodescanner.ui.theme.BarcodeScannerTheme
import scanBarcode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val total = remember {mutableFloatStateOf(0f)}
            val lastScan = remember {mutableListOf("")}
            val lastScanStatus = remember {mutableStateOf("")}
            BarcodeScannerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(total.floatValue.toString(),
                        modifier = Modifier.padding(innerPadding))
                    Text(lastScanStatus.value,
                        modifier = Modifier.padding(innerPadding))
                    IconButton(
                        onClick = {
                            scanBarcode(context, { barcode ->
                                //Decode and add to total
                                // keep building last scan
                                // add to last scane list, if too big remove fifo, recreate lastscanstatus
                                val barcodeString = barcode.rawValue


                            }, {}, {})
                        },
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Scan new",
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BarcodeScannerTheme {
    }
}