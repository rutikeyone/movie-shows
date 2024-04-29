package com.ru.movieshows.app.utils

import android.text.Editable
import android.text.TextWatcher

abstract class TextChangedWatcher : TextWatcher {

    abstract override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun afterTextChanged(s: Editable?) = Unit

}