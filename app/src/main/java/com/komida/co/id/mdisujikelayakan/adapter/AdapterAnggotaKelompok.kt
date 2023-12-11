package com.komida.co.id.mdisujikelayakan.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.komida.co.id.mdisujikelayakan.R
import com.komida.co.id.mdisujikelayakan.model.ModelListKelompok


class AdapterAnggotaKelompok(private val itemList: List<ModelListKelompok>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_ITEM = 1
        private const val VIEW_SECTION = 0
    }

    private var ctx: Context? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: ModelListKelompok?, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View?, obj: ModelListKelompok?, position: Int)
    }

    fun setOnItemLongClickListener(mItemLongClickListener: OnItemLongClickListener?) {
        mOnItemLongClickListener = mItemLongClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_SECTION) {
            val sectionView = inflater.inflate(R.layout.item_section, parent, false)
            SectionViewHolder(sectionView)
        } else {
            val itemView = inflater.inflate(R.layout.item_data_agt_kelompok, parent, false)
            OriginalViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val p: ModelListKelompok = itemList[position]
        if (holder is OriginalViewHolder) {
            val view = holder as OriginalViewHolder
            view.bind(p)
            view.lyt_parent.setOnClickListener { view ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(view, p, position)
                }
            }
            view.lyt_parent.setOnLongClickListener { view ->
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener!!.onItemLongClick(view, p, position)
                }
                true // Mengembalikan true untuk menunjukkan bahwa item long click telah ditangani
            }
        } else if (holder is SectionViewHolder) {
            val view = holder as SectionViewHolder
            view.title_section.text = p.label
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isHeader) VIEW_SECTION else VIEW_ITEM
    }

    inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val info_nama_agt_kelompok: TextView = view.findViewById(R.id.info_nama_agt_kelompok)
        val tanggal_input_kelompok: TextView = view.findViewById(R.id.tanggal_input_kelompok)
        val kode_nama_center: TextView = view.findViewById(R.id.kode_nama_center)
        val kode_nama_kelompok: TextView = view.findViewById(R.id.kode_nama_kelompok)
        val tempat_waktu_lwk: TextView = view.findViewById(R.id.tempat_waktu_lwk)
        val kategori_lwk: TextView = view.findViewById(R.id.kategori_lwk)
        val ketuacenter: TextView = view.findViewById(R.id.ketuacenter)
        val wakil_ketua_center: TextView = view.findViewById(R.id.wakil_ketua_center)

        val bt_toggle: ImageButton = view.findViewById(R.id.bt_toggle_passenger)
        val lyt_expand_item: View = view.findViewById(R.id.lyt_expand_items)
        val lyt_parent: View = view.findViewById(R.id.lyt_parent)

        init {
            bt_toggle.setOnClickListener {
                val item: ModelListKelompok = itemList[adapterPosition]
                toggleSection(bt_toggle, lyt_expand_item, item)
            }
        }

        fun bind(item: ModelListKelompok) {
            info_nama_agt_kelompok.text = Html.fromHtml(item.label)
            tanggal_input_kelompok.text = Html.fromHtml(item.tgl_input)
            kode_nama_center.text = Html.fromHtml(item.no_center+" - "+item.nama_center)
            kode_nama_kelompok.text = Html.fromHtml(item.no_kelompok+" - "+item.nama_kelompok)
            tempat_waktu_lwk.text = Html.fromHtml(item.tempat+" ,"+item.waktu)
            kategori_lwk.text = Html.fromHtml(item.kategori_lwk)
            ketuacenter.text = Html.fromHtml(item.ketua_center)
            wakil_ketua_center.text = Html.fromHtml(item.wakil_ketua_center)
        }
    }

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_section: TextView = view.findViewById(R.id.title_section)
    }

    private fun toggleSection(bt: View, lyt: View, item: ModelListKelompok) {
        val show = toggleArrow(bt)
        if (show) {
            ViewAnimation.expand(lyt, object : ViewAnimation.AnimListener {
                override fun onFinish() {
                    // Handle animation completion if needed
                }
            })
        } else {
            ViewAnimation.collapse(lyt)
            item.isExpanded = !item.isExpanded
        }
    }

    private fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }
}
