package com.syouth.basicrss.wireframe

import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

interface Presenter<View> {
    /**
     * Returns list of disposables which are subscriptions to view events.
     */
    fun bind(view: WeakReference<View>): List<Disposable>
}
