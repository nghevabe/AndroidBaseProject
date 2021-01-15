package com.example.mybaseproject.utils.extensions
import android.app.Activity
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import com.example.mybaseproject.BuildConfig
import com.example.mybaseproject.R
import com.example.mybaseproject.utils.SesUIModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

import org.json.JSONObject
import java.math.BigDecimal
import java.util.regex.Matcher
import java.util.regex.Pattern

// # Kotlin Extensions

//- [View](#view)
//- [Context](#context)
//- [Fragment](#fragment)
//- [Activity](#activity)
//- [ViewGroup](#viewgroup)
//- [TextView](#textview)
//- [String](#string)
//- [Other](#other)


// ## View
//SesExtensions
fun Exception.safeLog() {
    if (BuildConfig.DEBUG) printStackTrace()
}

fun Throwable.safeLog() {
    if (BuildConfig.DEBUG) printStackTrace()
}


fun EditText.str(): String {
    return text.toString()
}

fun String.getAmount(): String {
    return SesUIModel.getInstance().getDotMoney(this)
}
fun String.getAmountVND(): String {

    return SesUIModel.getInstance().getDotMoney(this) +" VND"
}
fun EditText.getAmountServer(): String {
    return text.toString().replace("[^\\d-.]".toRegex(), "")
}
fun String.getAmountServer(): String {
    return this.replace("[^\\d-.]".toRegex(), "")
}

fun String.getAmountBig(): BigDecimal {
    return BigDecimal(this.getAmountServer())
}

fun String.html(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}
/**
 * Extension method to provide simpler access to {@link View#getResources()#getString(int)}.
 */
fun View.getString(stringResId: Int): String = resources.getString(stringResId)

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun JSONObject.getSafeString(key:String):String{
    if(this.has(key))
        return getString(key)
    return ""
}

/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        try {
            onSafeClick(it)
        } catch (e: java.lang.Exception) {
            Log.wtf("EX", e)
        }
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun String.convertServerDate(): String {
    return SesUIModel.getInstance().convertDate("dd/MM/yyyy", "ddMMyyyy", this)
}

fun String.convertSavingDate(): String {
    return SesUIModel.getInstance().convertDate("yyyyMMdd", "dd/MM/yyyy", this)
}

fun String.convertDynamicDate(from:String, to:String): String {
    return SesUIModel.getInstance().convertDate(from, to, this)
}

fun String.parseInt():Int{
    return Integer.parseInt(this)
}
fun isValidPassword(password: String): Boolean {
    val pattern: Pattern
    val matcher: Matcher
    val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$"
    pattern = Pattern.compile(PASSWORD_PATTERN)
    matcher = pattern.matcher(password)
    return matcher.matches()
}

fun gotoStore(context: Activity) {
    try {
        val appId = context.getPackageName()
        val rateIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$appId")
        )
        var marketFound = false
        val otherApps = context.getPackageManager()
            .queryIntentActivities(rateIntent, 0)
        for (otherApp in otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName == "com.android.vending") {
                val otherAppActivity = otherApp.activityInfo
                val componentName = ComponentName(
                    otherAppActivity.applicationInfo.packageName,
                    otherAppActivity.name
                )
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                rateIntent.component = componentName
                context.startActivity(rateIntent)
                marketFound = true
                break

            }
        }


        if (!marketFound) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appId")
            )
            context.startActivity(webIntent)
        }
    }catch (e:Exception){
        Log.wtf("EX", e)
    }
}


fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun AppCompatTextView.setTextFutureExt(text: String) =
    setTextFuture(
        PrecomputedTextCompat.getTextFuture(
            text,
            TextViewCompat.getTextMetricsParams(this),
            null
        )
    )

fun AppCompatEditText.setTextFutureExt(text: String) =
    setText(
        PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))
    )
fun Window.addStatusBarColor(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.statusBarColor = ContextCompat.getColor(this.context, color)
    }
}
inline fun <reified T> genericType() = object: TypeToken<T>() {}.type
inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

fun TextView.copyOnHold(customText: String? = null, context: Context) {
    setOnLongClickListener {
        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied String", customText ?: text)
        clipboardManager.setPrimaryClip(clip)
        true // Or false if not consumed
    }
}

fun View.slideUp(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, -220f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideDown(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, -220f, 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}
val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadUrl(url: String) {
    //Picasso.get().load(url).placeholder(R.drawable.ses_img_fail).into(this)
    Picasso.get().load(url).into(this)
}
fun Int.toPx(): Int = (this* Resources.getSystem().displayMetrics.density).toInt()

@ColorInt
fun Context.getColorCompat(@ColorRes resourceId: Int) = ContextCompat.getColor(this, resourceId)

fun Context.getDrawableCompat(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)

fun Context.getDimension(@DimenRes resourceId: Int) = resources.getDimension(resourceId)

fun Context.getPxFromDp(dp: Float) = TypedValue
    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

fun TextView.bottomDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, id)
}
fun Uri.addUriParameter(key: String, newValue: String): Uri {
    val params = queryParameterNames
    val newUri = buildUpon().clearQuery()
    var isSameParamPresent = false
    for (param in params) {
        // if same param is present override it, otherwise add the old param back
        newUri.appendQueryParameter(param,
            if (param == key) newValue else getQueryParameter(param))
        if (param == key) {
            // make sure we do not add new param again if already overridden
            isSameParamPresent = true
        }
    }
    if (!isSameParamPresent) {
        // never overrode same param so add new passed value now
        newUri.appendQueryParameter(key,
            newValue)
    }
    return newUri.build()
}