package com.example.mybaseproject.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.mybaseproject.constants.Tags
import com.example.mybaseproject.networks.SesCommonData
import com.example.mybaseproject.networks.cryptoflight.AESService
import com.example.mybaseproject.utils.extensions.toDate
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

typealias CallDate = (date: String) -> Unit

class Utils {
    internal val OSINFO_MANUFACTURER = 24
    internal val OSINFO_MODEL = 22
    internal val REGEX_NONDOUBLE = "[^\\d.-]"
    private var keys: HashMap<String, String>? = null
    private val JPEG_FILE_SUFFIX = ".jpg"
    val SCREEN_WIDTH: String = "screen_width"
    val SCREEN_HEIGHT: String = "screen_height"

    fun getBitmapFromXmlDrawable(context: Context, drawableRes: Int): Bitmap {
        val drawable = context.resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    private object HOLDER {
        val INSTANCE = Utils()
    }

    companion object {
        @kotlin.jvm.JvmStatic
        val instance: Utils by lazy { HOLDER.INSTANCE }
    }

    fun toDip(value: Int, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }



    fun convertDpToPx(dp: Int, context: Context): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun getAlbumDir(): File? {
        var storageDir: File? = null

        if (Environment.MEDIA_MOUNTED == Environment
                .getExternalStorageState()
        ) {
            val root =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString() + "/Camera/bav_IMG"
            storageDir = File(root)
            storageDir.mkdirs()

        } else {
        }

        return storageDir
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(Date())
        val imageFileName = "vna$timeStamp"
        val albumF = getAlbumDir()
        return File.createTempFile(
            imageFileName, JPEG_FILE_SUFFIX,
            albumF
        )
    }


    fun toPx(value: Int, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun getPixelsFromDPs(activity: Context, dps: Int): Int {
        val r = activity.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics
        ).toInt()
    }


    internal fun getDInfo(context: Context, infoType: Int): String {
        val act = context as Activity
        var result: String? = null
        val tm = act.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        when (infoType) {
            OSINFO_MODEL -> result = "" + Build.MODEL
            OSINFO_MANUFACTURER -> result = "" + Build.MANUFACTURER
        }
        if (result == null)
            result = ""
        return result
    }

    fun checkGPS(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun getWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        return display.width // deprecated
    }

    fun getHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        return display.height // deprecated
    }

    internal fun getVersionApp(context: Context): String {
        val pinfo: PackageInfo
        var versionName = ""
        try {
            pinfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionName = pinfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("Error", e.toString())
        }

        return versionName
    }


    fun setClickableString(
        wholeValue: String,
        textView: TextView,
        clickableValue: Array<String>,
        clickableSpans: Array<ClickableSpan>
    ) {
        val spannableString = SpannableString(wholeValue)

        for (i in clickableValue.indices) {
            val clickableSpan = clickableSpans[i]
            val link = clickableValue[i]

            val startIndexOfLink = wholeValue.indexOf(link)
            spannableString.setSpan(
                clickableSpan,
                startIndexOfLink,
                startIndexOfLink + link.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        textView.highlightColor =
            Color.TRANSPARENT // prevent TextView change background when highlight
        textView.movementMethod = LinkMovementMethod.getInstance()

        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        textView.setLinkTextColor(Color.parseColor("#c49a54"))
    }


    internal fun getUniquePsuedoID(): String {
        val m_szDevIDShort = "35" + // we make this look like bav_boder_up valid IMEI

                Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID
            .length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 // 13 digits;

        var serial: String? = null
        try {
            serial = Build::class.java.getField("SERIAL").get(null).toString()

            // Go ahead and return the serial for api => 9
            return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (exception: Exception) {
            // String needs to be initialized
            serial = "serial" // some value
        }

        return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
    }

    internal fun getSDKVersionNumber(): Int {
        var version_sdk: Int
        try {
            version_sdk = Integer.valueOf(Build.VERSION.SDK)
        } catch (e: NumberFormatException) {
            version_sdk = 0
        }

        return version_sdk
    }


    fun getHHmm(duration: Long): String {
        return if (duration != 0L) {
            (duration / 60).toString() + "h" + duration % 60 + "m"
        } else "0h0m"
    }

    private fun genKeys() {
        if (keys == null) {
            keys = HashMap()
            keys!!["Ê"] = "EE"
            keys!!["É"] = "ES"
            keys!!["È"] = "EF"
            keys!!["Ẹ"] = "EJ"
            keys!!["Ẻ"] = "ER"
            keys!!["Ẽ"] = "EX"
            keys!!["Ý"] = "YS"
            keys!!["Ỳ"] = "YF"
            keys!!["Ỵ"] = "YJ"
            keys!!["Ỷ"] = "YR"
            keys!!["Ỹ"] = "YX"
            keys!!["Ư"] = "UW"
            keys!!["Ú"] = "US"
            keys!!["Ù"] = "UF"
            keys!!["Ụ"] = "UJ"
            keys!!["Ủ"] = "UR"
            keys!!["Ũ"] = "UX"
            keys!!["Ô"] = "OO"
            keys!!["Ơ"] = "OW"
            keys!!["Ó"] = "OS"
            keys!!["Ò"] = "OF"
            keys!!["Ọ"] = "OJ"
            keys!!["Ỏ"] = "OR"
            keys!!["Õ"] = "OX"
            keys!!["ƯƠ"] = "UOW"
            keys!!["Â"] = "AA"
            keys!!["Á"] = "AS"
            keys!!["À"] = "AF"
            keys!!["Ạ"] = "AJ"
            keys!!["Ả"] = "AR"
            keys!!["Ã"] = "AX"
            keys!!["Í"] = "IS"
            keys!!["Ì"] = "IF"
            keys!!["Ị"] = "IJ"
            keys!!["Ỉ"] = "IR"
            keys!!["Ĩ"] = "IX"
            keys!!["ê"] = "ee"
            keys!!["é"] = "es"
            keys!!["è"] = "ef"
            keys!!["ẹ"] = "ej"
            keys!!["ẻ"] = "er"
            keys!!["ẽ"] = "ex"
            keys!!["ý"] = "ys"
            keys!!["ỳ"] = "yf"
            keys!!["ỵ"] = "yj"
            keys!!["ỷ"] = "yr"
            keys!!["ỹ"] = "yx"
            keys!!["ư"] = "uw"
            keys!!["ú"] = "us"
            keys!!["ù"] = "uf"
            keys!!["ụ"] = "uj"
            keys!!["ủ"] = "ur"
            keys!!["ũ"] = "ux"
            keys!!["ô"] = "oo"
            keys!!["ơ"] = "ow"
            keys!!["ó"] = "os"
            keys!!["ò"] = "of"
            keys!!["ọ"] = "oj"
            keys!!["ỏ"] = "or"
            keys!!["õ"] = "ox"
            keys!!["ươ"] = "uow"
            keys!!["â"] = "aa"
            keys!!["ă"] = "aw"
            keys!!["á"] = "as"
            keys!!["à"] = "af"
            keys!!["ạ"] = "aj"
            keys!!["ả"] = "ar"
            keys!!["ã"] = "ax"
            keys!!["í"] = "is"
            keys!!["ì"] = "if"
            keys!!["ị"] = "ij"
            keys!!["ỉ"] = "ir"
            keys!!["ĩ"] = "ix"
        }
    }

    fun subStringForLength(input: String, len: Int): String? {
        return if (!TextUtils.isEmpty(input)) {
            input.substring(0, len) + "..."
        } else input
    }

    @SuppressLint("HardwareIds")
    fun getImei(context: Context): String? {
        if (SesCommonData.g().memei.isNullOrEmpty()) {

            val deviceUniqueId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            val serialNum: String = getUniquePsuedoID()
            if (deviceUniqueId.length > 0) {
                if (!SesCommonData.g().memei.isNullOrEmpty()) {
                    SesCommonData.g().memei =
                        SesCommonData.g().memei + "-" + deviceUniqueId + "-" + serialNum
                } else {
                    SesCommonData.g().memei = "$deviceUniqueId-$serialNum"
                }
            }
        }
        return SesCommonData.g().memei
    }

    fun getDeviceSizes(activity: Activity, whichSize: String): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return when (whichSize) {
            SCREEN_WIDTH -> displayMetrics.widthPixels
            SCREEN_HEIGHT -> displayMetrics.heightPixels
            else -> 0 // Error
        }
    }

    fun formatCurrency(amount: String, ccy: String?): String? {
        var pattern = ""
        val amountCheck: Double = if (!amount.isNullOrEmpty()) amount.toDouble() else 0.0
        pattern = if (!ccy.isNullOrEmpty()) {
            when (ccy) {
                "VND", "DKK", "CNY", "HKD", "IDR", "JPY", "KRW", "NOK", "TWD", "THB", "MOP" -> "###,###.##"
                else -> "###,###.00"
            }
        } else {
            "###,###.00"
        }
        if (amountCheck >= 1) {
            val otherSymbols = DecimalFormatSymbols()
            otherSymbols.decimalSeparator = '.'
            otherSymbols.groupingSeparator = ','
            val decimalFormat =
                DecimalFormat(pattern, otherSymbols)
            return decimalFormat.format(BigDecimal(amount)) + Tags.SPACE + ccy
        }
        return amount.replace(".0", "") + " " + ccy
    }

    fun formatCurrency2(amount: String, ccy: String?): String? {
        var pattern = ""
        val amountCheck: Double = if (!amount.isNullOrEmpty()) amount.toDouble() else 0.0
        pattern = if (!ccy.isNullOrEmpty()) {
            when (ccy) {
                "VND", "DKK", "CNY", "HKD", "IDR", "JPY", "KRW", "NOK", "TWD", "THB", "MOP" -> "###,###.##"
                else -> "###,###.00"
            }
        } else {
            "###,###.00"
        }
        if (amountCheck >= 1) {
            val otherSymbols = DecimalFormatSymbols()
            otherSymbols.decimalSeparator = '.'
            otherSymbols.groupingSeparator = ','
            val decimalFormat =
                DecimalFormat(pattern, otherSymbols)
            return decimalFormat.format(BigDecimal(amount))
        }
        return amount.replace(".0", "")
    }


    fun requestPermission(
        context: Activity,
        Permission: String,
        requestCode: Int
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Permission)) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Permission),
                requestCode
            )
        } else {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Permission),
                requestCode
            )
        }
    }


    fun showDate(
        context: Context,
        maxDate: Long = -1,
        minDate: Long = -1,
        format: String,
        datePick: String?,
        callDate: CallDate
    ) {
        var date = Date()
        if (!datePick.isNullOrEmpty()){
            date = datePick.toDate("dd.MM.yyyy")!!
        }
        val c = Calendar.getInstance()
        c.time = date
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var cal = Calendar.getInstance()
        val dialogPicker = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.getDefault())
                val test = sdf.format(cal.time)
                callDate.invoke(test)

            },
            year,
            month,
            day
        )
        if (maxDate != -1L) {
            dialogPicker.datePicker.maxDate = maxDate
        }
        if (minDate != -1L) {
            dialogPicker.datePicker.minDate = minDate
        }
        dialogPicker.show()
    }

    fun setE(data: String?, context: Context): String? {
        var result: String? = ""
        try {
            val iv = createInfo(context)
            val encryptByte = AESService.encrypt(SesCommonData.g().aesKey, data, iv)
            result = Base64.encodeToString(encryptByte, Base64.NO_PADDING)
        } catch (e: java.lang.Exception) {
        }
        return result
    }

    fun createInfo(context: Context): ByteArray {
        var result = ""
        var iv = ByteArray(AESService.getInstance().IV_LENGTH)
        val androidId =
            Settings.Secure.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            )
        try {
            result =
                if (!TextUtils.isEmpty(SesCommonData.g().password) && SesCommonData.g().password!!.length >= 16) {
                    SesCommonData.g().password!!.substring(0, 16)
                } else {
                    (SesCommonData.g().password + androidId).substring(0, 16)
                }
            iv = result.trim { it <= ' ' }.toByteArray(charset("UTF-8"))
        } catch (e: java.lang.Exception) {
        }
        return iv
    }

    fun getE(data: String, context: Context): String? {
        var resp: String? = ""
        try {
            val iv = createInfo(context)
            val array = Base64.decode(data.trim { it <= ' ' }, Base64.NO_PADDING)
            resp = AESService.decrypt(SesCommonData.g().aesKey, array, iv)
        } catch (e: java.lang.Exception) {
        }
        return resp
    }

}