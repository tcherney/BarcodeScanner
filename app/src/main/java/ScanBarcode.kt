import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

fun scanBarcode(context: Context, onSuccess: (Barcode) -> Unit, onCancel: () -> Unit, onFailure: (Exception) -> Unit) {
    val scanner = GmsBarcodeScanning.getClient(context)
    scanner.startScan().addOnSuccessListener { barcode ->
        onSuccess(barcode)
    }.addOnCanceledListener {
        onCancel()
    }.addOnFailureListener { exception ->
        onFailure(exception)
    }
}