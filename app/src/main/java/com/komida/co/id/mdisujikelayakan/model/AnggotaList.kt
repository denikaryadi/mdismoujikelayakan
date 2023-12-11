import android.graphics.drawable.Drawable

data class AnggotaList(
    var image: Int = 0,
    var imageDrw: Drawable? = null,
    var name: String? = null,
    var email: String? = null,
    var section: Boolean = false
) {
    constructor(name: String, section: Boolean) : this() {
        this.name = name
        this.section = section
    }
}
