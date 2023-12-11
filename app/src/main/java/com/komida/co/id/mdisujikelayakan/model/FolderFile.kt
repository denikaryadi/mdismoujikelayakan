package com.komida.co.id.mdisujikelayakan.model

import android.graphics.drawable.Drawable


class FolderFile {
    var image = 0
    var imageDrw: Drawable? = null
    var name: String? = null
    var date: String? = null
    var section = false
    var folder = true

    constructor() {}
    constructor(name: String?, date: String?, image: Int, folder: Boolean) {
        this.image = image
        this.name = name
        this.date = date
        this.folder = folder
    }

    constructor(name: String?, section: Boolean) {
        this.name = name
        this.section = section
    }
}
