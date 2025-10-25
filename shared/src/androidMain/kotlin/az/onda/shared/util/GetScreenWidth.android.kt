// kotlin
package az.onda.shared.util

import android.content.res.Resources

actual fun getScreenWidth(): Float =
    Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density