package au.com.official.nwz.base.viewModel

import android.arch.lifecycle.ViewModel
import android.content.res.Resources
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.annotation.StringRes
import au.com.official.nwz.R
import au.com.official.nwz.utils.bindings.SingleLiveEvent
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel constructor(val resources: Resources) : ViewModel() {
    protected var compositeDisposable: CompositeDisposable = CompositeDisposable()
    internal val progressDialogEvent = SingleLiveEvent<Int>()
    internal val alertMessageEvent = SingleLiveEvent<String>()
    internal val confirmationDialogEvent = SingleLiveEvent<String>()

    val mainContentVisibility = ObservableBoolean(false)
    val progressBarVisibility = ObservableBoolean(false)
    val errorMessageVisibility = ObservableBoolean(false)
    val errorMessage = ObservableField<Int>(R.string.err_no_data_available)

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    protected fun showProgressDialog(@StringRes message: Int = 0) {
        progressDialogEvent.value = message
    }

    protected fun hideProgressDialog() {
        progressDialogEvent.value = -1
    }

    protected fun showAlertDialog(@StringRes message: Int) {
        alertMessageEvent.value = resources.getString(message)
    }

    protected fun showAlertDialog(message: String) {
        alertMessageEvent.value = message
    }

    protected fun showMainContent() {
        mainContentVisibility.set(true)
    }

    protected fun hideMainContent() {
        mainContentVisibility.set(false)
    }

    protected fun showProgressBar() {
        progressBarVisibility.set(true)
    }

    protected fun hideProgressBar() {
        progressBarVisibility.set(false)
    }

    private fun showErrorMessage() {
        errorMessageVisibility.set(true)
    }

    protected fun hideErrorMessage() {
        errorMessageVisibility.set(false)
    }

    protected fun setErrorMessage(@StringRes message: Int = R.string.err_no_data_available) {
        showErrorMessage()
        errorMessage.set(message)
    }

    protected fun showConfirmationDialog(message: String) {
        confirmationDialogEvent.value = message
    }

    protected fun handleError(throwable: Throwable?, block: (message: String) -> Unit = {}) {
        hideProgressDialog()
        throwable?.apply {
            when (this) {
                is UnknownHostException -> showAlertDialog(R.string.err_no_internet)
                is SocketTimeoutException -> showAlertDialog(R.string.err_connection_time_out)
                is NoSuchElementException -> block("")
                is HttpException -> handleHttpException(this, block)
                else -> showAlertDialog(resources.getString(R.string.err_something_went_wrong))
            }
        }
    }

    /*
    *  {"message":"The email has already been taken.","results":{}}*/
    private fun handleHttpException(throwable: HttpException, block: (message: String) -> Unit = {}) {
        try {
            val responseBody = throwable.response().errorBody()
            val jsonObject = JSONObject(responseBody?.string())

            when (throwable.code()) {
                HttpURLConnection.HTTP_FORBIDDEN -> {
                    block(jsonObject.optString("message"))
                }
                else -> {
                    //{"errors":{"routing_number":["is not a valid routing number"]}}
                    if (jsonObject.has("message")) {
                        showAlertDialog(jsonObject.optString("message"))
                    } else if (jsonObject.has("errors")) {//Handle assembly error
                        val json = JSONObject(jsonObject.optString("errors"))
                        if (json.has("routing_number")) {
                            showAlertDialog(R.string.err_invalid_bsb)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            showAlertDialog(R.string.err_something_went_wrong)
        }
    }
}