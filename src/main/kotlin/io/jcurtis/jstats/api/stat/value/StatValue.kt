package io.jcurtis.jstats.api.stat.value

interface StatValue<T> {
    var value: T
    val type: Class<T>?

    fun serialize(): String
    fun deserialize(value: String): StatValue<T>
}