package com.bda.finance_test.utils.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive

fun CoroutineScope.getScope(): CoroutineScope {
    return if (!this.isActive) {
        val job = SupervisorJob()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        uiScope
    } else this
}

fun CoroutineScope.openNewScope(): CoroutineScope {
    val job = SupervisorJob()
    return CoroutineScope(Dispatchers.Main + job)
}