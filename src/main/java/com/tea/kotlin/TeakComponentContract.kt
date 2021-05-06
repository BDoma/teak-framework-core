package com.tea.kotlin

interface TeakComponentContract {
    interface Initializer<Model, Msg> {
        fun init(): Pair<Model, List<() -> Msg>>
    }

    interface Updater<Model, Msg> {
        fun update(model: Model, message: Msg): Pair<Model, List<() -> Msg>>
    }

    interface Impl<Model : Any, Msg> {
        fun initializer(): Initializer<Model, Msg>
        fun view(model: Model)
        fun updater(): Updater<Model, Msg>
    }
}