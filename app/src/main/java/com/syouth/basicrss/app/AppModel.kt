package com.syouth.basicrss.app

import android.database.Cursor


sealed class AppModel {
    object Initial : AppModel()

    object EmptyList : AppModel()

    data class List(val data: Cursor) : AppModel()
}
