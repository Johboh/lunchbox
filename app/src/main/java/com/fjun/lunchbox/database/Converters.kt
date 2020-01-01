package com.fjun.lunchbox.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun toState(value: Int?): State? {
        return value?.let { State.values().find { state -> state.state == value } }
    }

    @TypeConverter
    fun fromState(state: State?): Int? {
        return state?.state
    }
}