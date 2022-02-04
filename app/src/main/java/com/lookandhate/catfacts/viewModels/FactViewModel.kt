package com.lookandhate.catfacts.viewModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fact(val factText: String, var isFavorite: Boolean) : Parcelable
