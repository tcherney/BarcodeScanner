import android.content.Context
import com.google.android.gms.vision.barcode.Barcode

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