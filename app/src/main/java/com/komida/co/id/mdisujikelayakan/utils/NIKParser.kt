package com.komida.co.id.mdisujikelayakan.utils

import com.komida.co.id.mdisujikelayakan.model.NIKParserResponse

interface NIKParser {
    fun parseNik(nik: String): NIKParserResponse
}