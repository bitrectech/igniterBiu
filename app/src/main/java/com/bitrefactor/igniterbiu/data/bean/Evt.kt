package com.bitrefactor.igniterbiu.data.bean

data class Evt(
    var title: Int,
    var image: Int,
    var evt: (() -> Unit)?,
)
