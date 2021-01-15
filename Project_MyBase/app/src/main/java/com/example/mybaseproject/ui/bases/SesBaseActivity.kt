package com.example.mybaseproject.ui.bases

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.mybaseproject.R
import com.example.mybaseproject.bind.ParseViewer
import com.example.mybaseproject.di.Common
import com.example.mybaseproject.ui.dialogs.ConfirmDialog
import com.example.mybaseproject.ui.dialogs.LoadingDialog
import com.example.mybaseproject.utils.LocaleManager
import com.example.mybaseproject.utils.MySharedPreferences
import com.example.mybaseproject.utils.extensions.setSafeOnClickListener
import kotlinx.android.synthetic.main.ses_title_bar.*

abstract class SesBaseActivity : AppCompatActivity(){

    lateinit var spf: MySharedPreferences

    abstract val model: SesBaseViewModel

    @get:LayoutRes
    abstract val layoutId: Int

    @get:StringRes
    abstract val titleId: Int
    open val strTitle: String = ""

    open fun initView() {}

    open fun initListener() {}

    open fun observerLiveData() {}
    private lateinit var loading: LoadingDialog
    private lateinit var confirm: ConfirmDialog

    fun getSPF(): MySharedPreferences {
        if (!::spf.isInitialized) {
            spf = MySharedPreferences(this)
        }
        return spf
    }

    fun getFullScreen() {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.ses_transparent)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(
            LocaleManager.setLocale(
                newBase
            )
        )
    }
    fun getConfirm() : ConfirmDialog{
        if (!::confirm.isInitialized){
            confirm = ConfirmDialog(this)
        }
        return confirm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Common.baseActivity = this
        if (layoutId != 0)
            setContentView(layoutId)
        observerLiveData()

        model?.apply {
            isLoading.observe(this@SesBaseActivity, Observer {
                handleShowLoading(it!!)
            })
            message.observe(this@SesBaseActivity, Observer {
                onError(it)
            })
            noInternetConnectionEvent.observe(this@SesBaseActivity, Observer {
                //getConfirm().newBuild().setNotice(R.string.error_no_network)

            })
            connectTimeoutEvent.observe(this@SesBaseActivity, Observer {
                getConfirm().newBuild().setNotice(R.string.error_at_server)


            })
            serverErrorEvent.observe(this@SesBaseActivity, Observer {
                getConfirm().newBuild().setNotice(R.string.error_at_server)


            })
            errorToHomeMesssage.observe(this@SesBaseActivity, Observer {
                getConfirm().newBuild().setNotice(R.string.error_at_server)


            })
            expireSessionEvent.observe(this@SesBaseActivity, Observer {
                getConfirm().newBuild().setNotice(it).addButtonRight {
                    ActivityCompat.finishAffinity(this@SesBaseActivity)
                    setResult(Activity.RESULT_OK)
                    finish()
                    getConfirm().dismiss()
                }.setAction(true)
            })

            successFinishMessage.observe(this@SesBaseActivity, Observer {
                getConfirm().newBuild().setNotice(it).addButtonRight {
                    setResult(Activity.RESULT_OK)
                    finish()
                    getConfirm().dismiss()
                }.setAction(true)
            })
            errorFinishMessage.observe(this@SesBaseActivity, Observer {
                getConfirm().newBuild().setNotice(it).addButtonRight {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                    getConfirm().dismiss()
                }.setAction(true)
            })
            errorToHomeMesssage.observe(this@SesBaseActivity, Observer {
                getConfirm().newBuild().setNotice(it).addButtonRight {
                    setResult(Activity.RESULT_CANCELED)
                    ActivityCompat.finishAffinity(this@SesBaseActivity)
                    finish()
                    getConfirm().dismiss()
                }.setAction(true)
            })
            invalidCertificateEvent.observe(this@SesBaseActivity, Observer {
                getConfirm().newBuild().setNotice(R.string.error_cerpinning)
            })
        }

        initView()
        initListener()
    }


    protected open fun setFullScreen(colorStatusBar: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = colorStatusBar
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    override fun onResume() {
        super.onResume()
        Common.baseActivity = this
    }

    fun onError(it: String?) {
        getConfirm().newBuild().setNotice(it)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ParseViewer.getInstance().bind(this)
        bus_title_left?.apply {
            setSafeOnClickListener {
                onBackPressed()
            }
            if (titleId != 0) {
                title = getString(titleId).replace("\n", " ")
            } else {
                title = strTitle
            }
        }
    }

    fun setLeft(@DrawableRes icon: Int, onClickListener: () -> Unit) {
        bus_title_left.setSafeOnClickListener { onClickListener.invoke() }
        if (icon != 0)
            bus_title_left.setImageDrawable(ContextCompat.getDrawable(this, icon))
    }

    fun setRight(@DrawableRes icon: Int, onClickListener: () -> Unit) {
        bus_title_right.setSafeOnClickListener { onClickListener.invoke() }
        if (icon != 0)
            bus_title_right.setImageDrawable(ContextCompat.getDrawable(this, icon))
    }

    override fun setTitle(text: CharSequence) {
        super.setTitle(text)
        bus_title_name?.text = text.toString().replace("\n", " ")
    }


    override fun setTitle(@StringRes id: Int) {
        super.setTitle(id)
        bus_title_name?.text = getString(id).replace("\n", " ")
    }

    open fun handleShowLoading(isLoading: Boolean) {
        runOnUiThread {
            if (isLoading) showLoading() else hideLoading()
        }
    }

    fun hideLoading() {
        if(::loading.isInitialized){
            loading.dismiss()
        }
    }

    fun showLoading() {
        if (!::loading.isInitialized){
            loading = LoadingDialog(this)
        }
        loading.show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isFinishing) {
            return super.dispatchTouchEvent(ev)
        }
        var end = false
        try {
            val currentFocus = currentFocus
            end = super.dispatchTouchEvent(ev)
            val currentTime = System.currentTimeMillis()

            if (currentFocus != null && (currentFocus is EditText)) {
                val w = getCurrentFocus() ?: return end
                val scrcoords = IntArray(2)
                w.getLocationOnScreen(scrcoords)
                val x = ev.rawX + w.left.toFloat() - scrcoords[0].toFloat()
                val y = ev.rawY + w.top.toFloat() - scrcoords[1].toFloat()
                if (ev.action == 1 && (x < w.left.toFloat() || x >= w.right.toFloat() || y < w.top.toFloat() || y > w.bottom.toFloat())) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
                }
            }
        } catch (e: Exception) {

        }
        return end
    }
}
