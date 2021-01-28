package com.tea.kotlin.helper

abstract class Updater<Model, Msg> {
    abstract fun update(model: Model, message: Msg): Pair<Model, List<() -> Msg>>
}