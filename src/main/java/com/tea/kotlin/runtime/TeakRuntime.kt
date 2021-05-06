package com.tea.kotlin.runtime

import kotlinx.coroutines.*

class TeakRuntime<Model : Any, Msg>(
    init: () -> Pair<Model, List<() -> Msg>>,
    private val view: (Model, (Msg) -> Unit) -> Unit,
    private val update: (Model, Msg) -> Pair<Model, List<() -> Msg>>,
    private val done: ((Model) -> Unit)? = null
) {

    private val scope = MainScope()
    private var isRunning = true
    private lateinit var state: Model

    init {
        change(init())
    }

    private fun change(change: Pair<Model, List<() -> Msg>>) {
        state = change.first
        for (command in change.second) {
            commandRunner(command, ::dispatch)
        }

        view(state, ::dispatch)
    }

    private fun dispatch(message: Msg) {
        change(update(state, message))
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            scope.cancel()
            done?.invoke(state)
        }
    }

    private fun commandRunner(command: () -> Msg, dispatch: (Msg) -> Unit) {
        scope.launch(Dispatchers.IO) {
            val result = command()
            launch(Dispatchers.Main) {
                dispatch(result)
            }
        }
    }
}