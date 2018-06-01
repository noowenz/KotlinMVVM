package au.com.official.nwz.ui.landing

import android.content.res.Resources
import android.databinding.ObservableField
import android.util.Log
import au.com.official.nwz.R
import au.com.official.nwz.base.model.BaseResponse
import au.com.official.nwz.base.viewModel.BaseViewModel
import au.com.official.nwz.data.DataManager
import au.com.official.nwz.data.model.db.Competitions
import au.com.official.nwz.data.model.db.Person
import au.com.official.nwz.data.model.rest.Landing
import au.com.official.nwz.utils.bindings.SingleLiveEvent
import au.com.official.nwz.utils.rx.IScheduleProvider
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class LandingViewModel
@Inject constructor(private val scheduleProvider: IScheduleProvider,
                    private val dataManager: DataManager,
                    resources: Resources) : BaseViewModel(resources) {

    val personName = ObservableField<String>()
    val personList = ObservableField<List<Person>>()
    //Click Events
    val landingClickEvent = SingleLiveEvent<String>()

    fun saveToDB() {
        if (personName.get().isNullOrEmpty()){
            showAlertDialog("Field is empty")
            return
        }

        val person = Person(Math.random().toLong(), personName.get().toString(), "")
        compositeDisposable.add(
                dataManager.insertPeople(person)
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribeWith(object : DisposableObserver<Long>() {
                            override fun onNext(t: Long) {
                                Log.d("Inserted data", t.toString())
                                getFromDB()
                            }

                            override fun onError(e: Throwable) {
                                handleError(e)
                                setErrorMessage()
                            }

                            override fun onComplete() {

                            }

                        }))
    }

    fun getFromDB() {
        compositeDisposable.add(
                dataManager.getAllPeople()
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribeWith(object : DisposableObserver<MutableList<Person>>() {
                            override fun onNext(t: MutableList<Person>) {
                                Log.d("Database", t.toString())
                                personList.set(t)
                            }

                            override fun onError(e: Throwable) {
                                handleError(e)
                                setErrorMessage()
                            }

                            override fun onComplete() {

                            }

                        }))
    }

    fun deleteByUserId(uid: Long) {
        compositeDisposable.add(
                dataManager.deleteByUserId(uid)
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribeWith(object : DisposableObserver<Int>() {
                            override fun onNext(t: Int) {
                                Log.d("deleted user", t.toString())
                                getFromDB()
                            }

                            override fun onError(e: Throwable) {
                                handleError(e)
                                setErrorMessage()
                            }

                            override fun onComplete() {

                            }

                        }))
    }

    fun fetchCompetitionsFromServer() {
        showProgressDialog(R.string.msg_loading)
        compositeDisposable.add(
                dataManager.getCompetitions()
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribeWith(object : DisposableObserver<List<Competitions>>() {
                            override fun onNext(t: List<Competitions>) {
                                hideProgressDialog()
                                landingClickEvent.value = t.size.toString()

                                Log.d("Fetched competitions", t.toString())
                                insertCompetitionsToDB(t)
                            }

                            override fun onError(e: Throwable) {
                                hideProgressDialog()
                                handleError(e)
                            }

                            override fun onComplete() {

                            }

                        }))
    }

    fun insertCompetitionsToDB(t: List<Competitions>) {
        compositeDisposable.add(
                dataManager.insertCompetitions(t)
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribeWith(object : DisposableObserver<Long>() {
                            override fun onNext(t: Long) {
                                Log.d("Inserted competitions", t.toString())
                                getFromDB()
                            }

                            override fun onError(e: Throwable) {
                                handleError(e)
                                setErrorMessage()
                            }

                            override fun onComplete() {

                            }

                        }))
    }

    fun fetchFromServer() {
        val landingRequest = Landing(
                firstName = personName.get()
        )
        showProgressDialog(R.string.msg_loading)
        compositeDisposable.add(
                dataManager.registerUser(landingRequest)
                        .subscribeOn(scheduleProvider.io())
                        .observeOn(scheduleProvider.ui())
                        .subscribeWith(object : DisposableObserver<BaseResponse<Landing>>() {
                            override fun onNext(t: BaseResponse<Landing>) {
                                hideProgressDialog()
                                landingClickEvent.value = t.message
                            }

                            override fun onError(e: Throwable) {
                                hideProgressDialog()
                                handleError(e)
                            }

                            override fun onComplete() {

                            }

                        }))
    }
}
