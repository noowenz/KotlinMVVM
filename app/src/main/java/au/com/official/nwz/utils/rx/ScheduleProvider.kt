package au.com.official.nwz.utils.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleProvider @Inject constructor() : IScheduleProvider {
    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun trampoline(): Scheduler = Schedulers.trampoline()
}