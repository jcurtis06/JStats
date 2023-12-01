package io.jcurtis.jstats.api.cache

import io.jcurtis.jstats.api.stat.value.StatValue

data class DataCacheEntry(
    var statValue: StatValue<*>,
    var timestamp: Long
)
