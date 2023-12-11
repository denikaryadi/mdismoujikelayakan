package com.komida.co.id.mdisujikelayakan.adapter

import ViewAnimation
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.komida.co.id.mdisujikelayakan.R
import com.komida.co.id.mdisujikelayakan.model.ModelListAgtUKAll

class AdapterInfoAnggota(private val itemList: List<ModelListAgtUKAll>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_ITEM = 1
        private const val VIEW_SECTION = 0
    }

    private var ctx: Context? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: ModelListAgtUKAll?, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, obj: ModelListAgtUKAll?, position: Int)
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
            val itemView = inflater.inflate(R.layout.item_data_agt_uk_all, parent, false)
            OriginalViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val p: ModelListAgtUKAll = itemList[position]
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
            view.title_section.text = p.nama
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isHeader) VIEW_SECTION else VIEW_ITEM
    }

    inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val infonamaanggota: TextView = view.findViewById(R.id.info_nama_anggota)
        val tanggal_bergabung: TextView = view.findViewById(R.id.tanggal_bergabung)
        val tanggal_lahir: TextView = view.findViewById(R.id.tanggal_lahir)
        val nik_ktp: TextView = view.findViewById(R.id.nik_ktp)
        val no_handphone: TextView = view.findViewById(R.id.no_handphone)
        val status_kawin: TextView = view.findViewById(R.id.statuskawin)
        val nama_suami: TextView = view.findViewById(R.id.nama_suami)
        val nama_ibu_kandung: TextView = view.findViewById(R.id.nama_ibu_kandung)
        val bt_toggle: ImageButton = view.findViewById(R.id.bt_toggle_passenger)
        val lyt_expand_item: View = view.findViewById(R.id.lyt_expand_items)
        val lyt_parent: View = view.findViewById(R.id.lyt_parent)

        init {
            bt_toggle.setOnClickListener {
                val item: ModelListAgtUKAll = itemList[adapterPosition]
                toggleSection(bt_toggle, lyt_expand_item, item)
            }
        }

        fun bind(item: ModelListAgtUKAll) {
            infonamaanggota.text = Html.fromHtml(item.label)
            tanggal_bergabung.text = Html.fromHtml(item.tgl_bergabung)
            tanggal_lahir.text = Html.fromHtml(item.tgl_lahir)
            nik_ktp.text = Html.fromHtml(item.nik)
            no_handphone.text = Html.fromHtml(item.handphone)
            status_kawin.text = Html.fromHtml(item.status_kawin)
            nama_suami.text = Html.fromHtml(item.nama_suami)
            nama_ibu_kandung.text = Html.fromHtml(item.nama_ibu_kandung)
        }
    }

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_section: TextView = view.findViewById(R.id.title_section)
    }

    private fun toggleSection(bt: View, lyt: View, item: ModelListAgtUKAll) {
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
