package com.syouth.basicrss.wireframe

import io.reactivex.Observable

interface View<Model> {

    fun wantToLoadIntent(): Observable<Unit>

    fun render(model: Model)
}
