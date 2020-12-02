package org.vernality.profitclub.utils.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import org.vernality.profitclub.rx.SchedulerProvider

fun ImageView.setImageToImageView(array: ByteArray): View {
    val bmp = BitmapFactory.decodeByteArray(array, 0, array.size)

    setImageBitmap(
        Bitmap.createBitmap(
            bmp
        )
    )

    return this
}

fun <T> Observable<T>.addStreamsIO_UI():Observable<T> {
    return this.compose(object : ObservableTransformer<T, T> {
        override fun apply(upstream: Observable<T>): ObservableSource<T> {
            return upstream
                .subscribeOn(SchedulerProvider().io())
                .observeOn(SchedulerProvider().ui())
        }
    })
}


fun <T> Single<T>.addStreamsIO_UI():Single<T> {
    return this.compose (object : SingleTransformer<T, T> {
        override fun apply(upstream: Single<T>): SingleSource<T> {
            return upstream
                .subscribeOn(SchedulerProvider().io())
                .observeOn(SchedulerProvider().ui())
        }
    })
}

fun Completable.addStreamsIO_UI():Completable {
    return this.compose(object : CompletableTransformer {
        override fun apply(upstream: Completable): Completable {
            return upstream
                .subscribeOn(SchedulerProvider().io())
                .observeOn(SchedulerProvider().ui())
        }
    })
}

