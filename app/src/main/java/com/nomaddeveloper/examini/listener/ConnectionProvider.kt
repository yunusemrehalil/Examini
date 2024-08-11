package com.nomaddeveloper.examini.listener

import com.nomaddeveloper.examini.network.Connection

interface ConnectionProvider {
    fun getConnection(): Connection
}