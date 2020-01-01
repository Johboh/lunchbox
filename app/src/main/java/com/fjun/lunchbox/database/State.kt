package com.fjun.lunchbox.database

enum class State constructor(val state: Int) {
    FREEZER(1), FRIDGE(2), STORAGE(3), ELSE(0)
}