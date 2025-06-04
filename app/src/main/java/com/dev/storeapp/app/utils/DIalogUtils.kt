import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout

object DialogUtils {
    fun showQuantityDialog(
        context: Context,
        currentQuantity: Int,
        onQuantityUpdated: (newQuantity: Int) -> Unit
    ) {
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val quantityInput = EditText(context).apply {
            hint = "Enter Quantity"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(currentQuantity.toString())
        }

        layout.addView(quantityInput)

        AlertDialog.Builder(context)
            .setTitle("Edit Quantity")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val newQuantity = quantityInput.text.toString().toIntOrNull() ?: currentQuantity
                onQuantityUpdated(newQuantity)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    fun showCommonDialog(
        context: Context,
        title: String? = null,
        message: String,
        onYes: () -> Unit,
        onNo: (() -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(context)
        if (!title.isNullOrEmpty()) {
            builder.setTitle(title)
        }
        builder.setMessage(message)
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                onYes()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                onNo?.invoke()
            }
            .setCancelable(false)
            .show()
    }
}
