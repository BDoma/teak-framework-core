package com.tea.kotlin.helper

interface Updater<Model, Msg> {
    fun update(model: Model, message: Msg): Pair<Model, List<() -> Msg>>
}