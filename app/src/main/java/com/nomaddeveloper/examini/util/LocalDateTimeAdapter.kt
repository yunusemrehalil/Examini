package com.nomaddeveloper.examini.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: LocalDateTime) {
        out.value(value.format(formatter))
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): LocalDateTime {
        return LocalDateTime.parse(`in`.nextString(), formatter)
    }
}
