package org.vernality.profitclub.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.vernality.profitclub.rx.SchedulerProvider
import org.vernality.profitclub.interactors.MainInteractor

abstract class BaseViewModel<T>(
    appContext: Application,
    protected val liveDataForViewToObserve: MutableLiveData<T> = MutableLiveData(),
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    protected val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : AndroidViewModel(appContext), KoinComponent{

    val interactor: MainInteractor by inject()

//    var _appContext  = appContext

}