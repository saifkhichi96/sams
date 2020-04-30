package co.aspirasoft.sams.utils

import android.graphics.Color
import android.view.View
import co.aspirasoft.sams.R
import com.google.android.material.snackbar.Snackbar

object Utils {

    fun showError(v: View, message: String) {
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(v.context.getColor(R.color.colorWarning))
                .setTextColor(Color.WHITE)
                .show()
    }

}