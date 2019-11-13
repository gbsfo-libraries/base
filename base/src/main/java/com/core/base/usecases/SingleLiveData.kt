package com.core.base.usecases

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import java.util.concurrent.atomic.AtomicBoolean
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber


open class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Timber.w("Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner, Observer<T> {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(it: T?) {
        pending.set(true)
        super.setValue(it)
    }

    @MainThread
    operator fun invoke() {
        value = null
    }
}
