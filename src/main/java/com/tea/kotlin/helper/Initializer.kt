package com.tea.kotlin.helper

interface Initializer<Model, Msg> {
    fun init(): Pair<Model, List<() -> Msg>>
}