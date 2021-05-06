package teak.framework.core

import teak.framework.core.runtime.TeakRuntime
import teak.framework.core.TeakComponentContract.*


class TeakComponent<Model : Any, Msg>(private val implementation: Impl<Model, Msg>) {

    private lateinit var teakRuntime: TeakRuntime<Model, Msg>
    private lateinit var updater: Updater<Model, Msg>
    private lateinit var initer: Initializer<Model, Msg>
    private var dispatcher: ((Msg) -> Unit)? = null

    fun dispatch(message: Msg) {
        if (dispatcher == null) {
            throw IllegalStateException("Tea Component is not created yet. Did you try to use dispatch() before Fragment onViewCreated() called?")
        } else {
            dispatcher?.invoke(message)
        }
    }

    fun onCreate() {
        updater = implementation.updater()
        initer = implementation.initializer()
        teakRuntime = TeakRuntime(::init, view = ::view, ::update)
    }

    fun onDestroy() {
        teakRuntime.stop()
        dispatcher = null
    }

    private fun init(): Pair<Model, List<() -> Msg>> {
        return initer.init()
    }

    private fun update(model: Model, message: Msg): Pair<Model, List<() -> Msg>> {
        return updater.update(model, message)
    }

    private fun view(model: Model, dispatch: (Msg) -> Unit) {
        implementation.view(model)
        dispatcher = dispatch
    }
}