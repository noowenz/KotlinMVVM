package au.com.official.nwz.utils.rx

import io.reactivex.Scheduler

interface IScheduleProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
    fun trampoline(): Scheduler
}