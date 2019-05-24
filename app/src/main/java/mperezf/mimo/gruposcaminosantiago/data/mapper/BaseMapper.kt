package mperezf.mimo.gruposcaminosantiago.data.mapper

import io.reactivex.functions.Function
import java.util.*

abstract class BaseMapper<T, S> {

    protected abstract fun map(dataModel: T): S
    protected abstract fun reverseMap(model: S): T

    fun getMapper(): Function<T, S> {
        return Function { T ->
            map(T)
        }
    }

    fun getListMapper(): Function<List<T>, List<S>> {
        return Function { list ->
            val resultList = list.mapTo(ArrayList()) { map(it) }
            resultList
        }
    }
}