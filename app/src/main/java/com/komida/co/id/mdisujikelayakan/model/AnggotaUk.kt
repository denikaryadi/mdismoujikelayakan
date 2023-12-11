package com.komida.co.id.mdisujikelayakan.model

import android.graphics.drawable.Drawable

class AnggotaUk {
    var image = 0
    var imageDrw: Drawable? = null
    var name: String? = null
    var email: String? = null
    var section = false

    constructor() {}

    constructor(name: String, section: Boolean) {
        this.name = name
        this.section = section
    }
}
