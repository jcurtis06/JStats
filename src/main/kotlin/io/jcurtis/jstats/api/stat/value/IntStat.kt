package io.jcurtis.jstats.api.stat.value

class IntStat(override var value: Int) : StatValue<Int> {
    override val type = Int::class.java

    override fun serialize(): String {
        return value.toString()
    }

    override fun deserialize(value: String): StatValue<Int> {
        return IntStat(value.toInt())
    }
}
