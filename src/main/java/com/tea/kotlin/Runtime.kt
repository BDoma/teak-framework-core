package com.tea.kotlin

import kotlinx.coroutines.*

class Runtime<Model, Msg>(
    init: () -> Pair<Model, List<() -> Msg>>,
    private val view: (Model, (Msg) -> Unit) -> Unit,
    private val update: (Model, Msg) -> Pair<Model, List<() -> Msg>>,
    private val done: ((Model) -> Unit)? = null
) {

    private val scope = MainScope()
    private var isRunning = true
    private var state: Model? = null
    //TODO: nullable

    init {
        change(init())
    }

    private fun change(change: Pair<Model, List<() -> Msg>>) {
        state = change.first
        for (command in change.second) {
            commandRunner(command, ::dispatch)
        }

        state?.let { model ->
            view(model, ::dispatch)
        }
    }

    private fun dispatch(message: Msg) {
        state?.let {
            change(update(it, message))
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            scope.cancel()
            done?.let { state?.let(it) }
        }
    }

    private fun commandRunner(command: () -> Msg, dispatch: (Msg) -> Unit) {
        scope.launch(Dispatchers.Default) {
            val result = command()
            withContext(Dispatchers.Main) {
                dispatch(result)
            }
        }
    }
}