package com.amolina.epic.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.WindowManager.LayoutParams
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Extensions for simpler launching of Activities
 */

inline fun <reified T : Any> Activity.startActivity(
  requestCode: Int = -1,
  options: Bundle? = null,
  noinline init: Intent.() -> Unit = {}
) {
  val intent = intent<T>(this)
  intent.init()
  startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.startActivity(
  options: Bundle? = null,
  noinline init: Intent.() -> Unit = {}
) {
  val intent = intent<T>(this)
  intent.init()
  startActivity(intent, options)
}

inline fun <reified T : Any> intent(context: Context): Intent = Intent(context, T::class.java)

/**
 * Set the appearance of the status bar via the [WindowInsetsControllerCompat.setAppearanceLightNavigationBars]
 * function. This does not change the background color of the status bar.
 *
 * @param isLight whether the apperance of the status bar should be light (dark text) or dark (light text)
 * @param view mandatory view for constructor of [WindowInsetsControllerCompat]. Not used in this context.
 * */
fun Activity.setStatusBarAppearance(isLight: Boolean, view: View) {
  val windowInsetsControllerCompat = WindowInsetsControllerCompat(window, view)
  windowInsetsControllerCompat.isAppearanceLightStatusBars = isLight
}

/**
 * Set light bars with [Color.WHITE] background.
 */
fun Activity.setLightBar() {
  if (VERSION.SDK_INT >= VERSION_CODES.M) {
    var flags = window.decorView.systemUiVisibility

    if (!flagIsActive(flags, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)) {
      flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
      window.statusBarColor = Color.WHITE
    }

    if (VERSION.SDK_INT >= VERSION_CODES.O &&
      !flagIsActive(flags, View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    ) {
      flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
      window.navigationBarColor = Color.WHITE
    }
    window.decorView.systemUiVisibility = flags
  }
}

/**
 * Clear light bars and set background back to [Color.WHITE].
 */
fun Activity.clearLightBar() {
  if (VERSION.SDK_INT >= VERSION_CODES.M) {
    var flags = window.decorView.systemUiVisibility

    if (flagIsActive(flags, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)) {
      flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
      window.statusBarColor = Color.TRANSPARENT
    }

    if (VERSION.SDK_INT >= VERSION_CODES.O &&
      flagIsActive(flags, View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    ) {
      flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
      window.navigationBarColor = Color.TRANSPARENT
    }

    window.decorView.systemUiVisibility = flags
  }
}

/**
 * Set light navigation bar with [Color.WHITE] background.
 */
fun Activity.setLightNavigationBar() {
  if (VERSION.SDK_INT >= VERSION_CODES.M) {
    var flags = window.decorView.systemUiVisibility
    if (VERSION.SDK_INT >= VERSION_CODES.O &&
      !flagIsActive(flags, View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    ) {
      flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
      window.navigationBarColor = Color.WHITE
    }
    window.decorView.systemUiVisibility = flags
  }
}

fun Activity.setFullScreen() {
  window.apply {
    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
    clearFlags(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    statusBarColor = Color.TRANSPARENT
    navigationBarColor = Color.TRANSPARENT
  }
}

fun Activity.clearFullScreen() {
  var flags = window.decorView.systemUiVisibility

  val fullScreenFlags = listOf(
    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,
    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
  )

  fullScreenFlags.forEach { flag ->
    if (flagIsActive(flags, flag)) {
      flags = flags xor flag
    }
  }
  window.decorView.systemUiVisibility = flags
}

@ColorInt
private fun Context.getColorFromAttr(
  @AttrRes attrColor: Int,
  typedValue: TypedValue = TypedValue(),
  resolveRefs: Boolean = true
): Int {
  theme.resolveAttribute(attrColor, typedValue, resolveRefs)
  return typedValue.data
}

private fun flagIsActive(flags: Int, flag: Int): Boolean = (flags and flag) == flag

/**
 * Extension method to provide simpler access to {@link ContextCompat#getColor(int)}.
 */
fun Resources.getColorCompat(@ColorRes color: Int, theme: Theme? = null): Int {
  return ResourcesCompat.getColor(this, color, theme)
}