package com.komida.co.id.mdisujikelayakan.model

class ModelListProductSaving(
    var product_id: String? = null,
    var product_name: String? = null,
    var deposit: String? = null,
    var flag: String? = null,
    var kode_uk: String? = null,
    var kode_ukprodsav: String? = null,
    var section: Boolean = false,
    var isExpanded: Boolean = false,
    var isHeader: Boolean = false
) {
    constructor(
        product_id: String,
        product_name:String,
        deposit: String,
        flag: String,
        kode_uk: String,
        kode_ukprodsav: String,
        isHeader: Int,
        isExpanded: Boolean,
        section: Boolean,
        additionalParameter: Int
    ) : this(
        "$product_id<br>",
        product_name,
        deposit,
        flag,
        kode_uk,
        kode_ukprodsav,
        isHeader != 0,
        isExpanded,
        section
    )
}
