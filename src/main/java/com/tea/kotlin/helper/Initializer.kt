package com.tea.kotlin.helper

abstract class Initializer<Model, Msg> {
    abstract fun init(): Pair<Model, List<() -> Msg>>
}