package com.syouth.basicrss.app

import com.syouth.basicrss.backend.Cache
import com.syouth.basicrss.wireframe.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

const val URL = "https://jsonplaceholder.typicode.com/posts"

class AppPresenter(val cache: Cache) : Presenter<MainView> {

    override fun bind(view: WeakReference<MainView>): List<Disposable> {

        val result = mutableListOf<Disposable>()

        view.get()?.render(AppModel.Initial)
        val d = view.get()?.wantToLoadIntent()?.subscribe {
            view.get()?.render(AppModel.EmptyList)
            cache.subscribe(URL).observeOn(AndroidSchedulers.mainThread()).subscribe {
                view.get()?.render(AppModel.List(it))
            }
        } as Disposable

        result.add(d)

        return result
    }

}
