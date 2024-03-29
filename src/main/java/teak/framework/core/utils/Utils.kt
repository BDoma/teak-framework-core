package teak.framework.core.utils

fun <Model, Msg> pair(
    model: Model,
    commands: List<() -> Msg> = listOf()
): Pair<Model, List<() -> Msg>> {
    return Pair(model, commands)
}