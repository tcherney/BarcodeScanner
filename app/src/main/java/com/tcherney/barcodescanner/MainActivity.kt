package com.tcherney.barcodescanner

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tcherney.barcodescanner.ui.theme.BarcodeScannerTheme
import scanBarcode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Home()
        }
    }
}
//TODO test camerax implmentation
@Composable
fun AreaView(area: Area, onDelete: () -> Unit) {
    val areaAsString = remember {mutableStateOf(area.number.toString())}
    Card(modifier = Modifier.size(200.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Text("Area: ", modifier = Modifier.padding(16.dp))
            TextField(
                areaAsString.value,
                onValueChange = { it ->
                    val changed = it.toIntOrNull()
                    if (changed != null) {
                        area.number = changed
                        areaAsString.value = it
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.width(75.dp)
            )
            IconButton(
                onClick = onDelete,
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Delete",
                )
            }
        }
        HorizontalDivider(thickness = 6.dp)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                area.total.toString(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
        }
    }
}
@Composable
fun Home() {
    val context = LocalContext.current
    val TOTAL_SCANS = 10
    val currTotal = remember {mutableIntStateOf(0)}
    val currScan = remember { mutableIntStateOf(0) }
    val lastScan = remember {mutableListOf("")}
    val lastScanStatus = remember {mutableStateOf("")}
    val totals = remember { mutableStateListOf(Area(0f,0)) }
    BarcodeScannerTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding), verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
                ) {
                    for (i in totals.reversed()) {
                        AreaView(i, {
                            totals.remove(i)
                        })
                    }
                    IconButton(
                        onClick = {
                            totals.add(Area(0f,0))
                            currScan.intValue = totals.size-1
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "new area",
                        )
                    }
                    Text(
                        lastScanStatus.value,
                    )
                }
                IconButton(
                    onClick = {
                        scanBarcode(context, { barcode ->
                            //Decode and add to total
                            // keep building last scan
                            // add to last scan list, if too big remove fifo, recreate lastscanstatus
                            val barcodeString = barcode.rawValue!!
                            var startPos = 0
                            if (barcodeString[0] == '0') {
                                startPos = 1
                            }
                            val plu = barcodeString.substring(startPos + 1, startPos + 6)
                            val value = if (startPos == 0) barcodeString.substring(
                                7,
                                9
                            ) + "." + barcodeString.substring(
                                9,
                                11
                            ) else barcodeString.substring(
                                7,
                                10
                            ) + "." + barcodeString.substring(10, 12)
                            val newScanStatus = "Scanned $plu at value $$value"
                            totals[currTotal.intValue].total += value.toFloat()
                            if (currScan.intValue == TOTAL_SCANS) {
                                lastScan.removeAt(0)
                                lastScan.add(newScanStatus)
                            } else {
                                lastScan.add(newScanStatus)
                                currScan.intValue += 1
                            }
                            lastScanStatus.value =
                                lastScan.joinToString(separator = "\n") { it }
                        }, {}, {})
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Scan new",
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Home()
}