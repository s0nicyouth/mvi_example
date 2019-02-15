package com.syouth.basicrss.app

import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.jakewharton.rxbinding3.view.clicks
import com.syouth.basicrss.R
import com.syouth.basicrss.backend.Cache
import com.syouth.basicrss.wireframe.View
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference


/**
 * 1. Контекст никогда не утечет т.к. он внутри активити
 * 2. Автоматическое сохранение состояния отображения т.к. пресентер не зависит от вью и может оставаться жить
 * (решается "проблема крутилок"). Это достигается простым созданием пресентера вне активити.
 * 3. Нет проблемы конфликтующих состояний вью(показывается, что человек печатает когда он не печатает потому,
 * что где-то не сменился стейт)
 * 4. Как видно Room можно использовать на main
 * 5. Кажется можно отдавать первый элемент при подписке на main просто с помощью Handler, но это не точно(нужно удостоверится)
 * ...
 * Профит
 *
 * ПС. Это модельный и крайне упрощеный пример с недостатками, среди которых тем не менее, кажется, нет неустранимых.
 */
class MainView : AppCompatActivity(), View<AppModel> {

    private lateinit var button: Button
    private lateinit var list: RecyclerView
    private lateinit var presenter: AppPresenter
    private lateinit var disposables: List<Disposable>
    private val adapter: Adapter = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.load_button)
        list = findViewById(R.id.rss_feed)
        presenter = AppPresenter(Cache(applicationContext))
        disposables = presenter.bind(WeakReference(this))
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.forEach { it.dispose() }
    }

    override fun render(model: AppModel) =
        when (model) {
            is AppModel.Initial -> renderInitial()
            is AppModel.EmptyList -> renderEmptyList()
            is AppModel.List -> renderList(model.data)
        }

    private fun renderInitial() {
        button.visibility = android.view.View.VISIBLE
        list.visibility = android.view.View.GONE
    }

    private fun renderEmptyList() {
        button.visibility = android.view.View.GONE
        list.visibility = android.view.View.VISIBLE
    }

    private fun renderList(cursor: Cursor) {
        button.visibility = android.view.View.GONE
        list.visibility = android.view.View.VISIBLE
        adapter.cursor = cursor
    }

    override fun wantToLoadIntent(): Observable<Unit> = button.clicks()
}
