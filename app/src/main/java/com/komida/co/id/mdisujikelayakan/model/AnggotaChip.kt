package com.komida.co.id.mdisujikelayakan.model
import android.graphics.drawable.Drawable
import com.pchmn.materialchips.model.ChipInterface

class AnggotaChip(
    private val id: String,
    private val image: Drawable,
    private val name: String,
    private val info: String
) : ChipInterface {

    override fun getId(): Any {
        return id
    }

    override fun getAvatarUri(): Nothing? {
        return null
    }

    override fun getAvatarDrawable(): Drawable {
        return image
    }

    override fun getLabel(): String {
        return name
    }

    override fun getInfo(): String {
        return info
    }
}
