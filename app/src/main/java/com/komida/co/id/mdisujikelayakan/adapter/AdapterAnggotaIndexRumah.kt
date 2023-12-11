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
import com.komida.co.id.mdisujikelayakan.model.ModelListAgtKeluarga
import com.komida.co.id.mdisujikelayakan.model.ModelListIndexRumah


class AdapterAnggotaIndexRumah(private val itemList: List<ModelListIndexRumah>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_ITEM = 1
        private const val VIEW_SECTION = 0
    }

    private var ctx: Context? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: ModelListIndexRumah?, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_SECTION) {
            val sectionView = inflater.inflate(R.layout.item_section, parent, false)
            SectionViewHolder(sectionView)
        } else {
            val itemView = inflater.inflate(R.layout.item_data_agt_index_rumah, parent, false)
            OriginalViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val p: ModelListIndexRumah = itemList[position]
        if (holder is OriginalViewHolder) {
            val view = holder as OriginalViewHolder
            view.bind(p)
            view.lyt_parent.setOnClickListener { view ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(view, p, position)
                }
            }
        } else if (holder is SectionViewHolder) {
            val view = holder as SectionViewHolder
            view.title_section.text = p.kode_uk_rumah
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isHeader) VIEW_SECTION else VIEW_ITEM
    }

    inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_index_dinding: TextView = view.findViewById(R.id.tv_index_dinding)
        val tv_index_atap: TextView = view.findViewById(R.id.tv_index_atap)
        val tv_index_lantai: TextView = view.findViewById(R.id.tv_index_lantai)
        val tv_kondisi_dinding: TextView = view.findViewById(R.id.tv_kondisi_dinding)
        val tv_kondisi_atap: TextView = view.findViewById(R.id.tv_kondisi_atap)
        val tv_kondisi_lantai: TextView = view.findViewById(R.id.tv_kondisi_lantai)
        val tv_poin_dinding:TextView = view.findViewById(R.id.tv_poin_dinding)
        val tv_poin_atap:TextView = view.findViewById(R.id.tv_poin_atap)
        val tv_poin_lantai:TextView = view.findViewById(R.id.tv_poin_lantai)
        val tv_status_milik:TextView = view.findViewById(R.id.tv_status_milik)
        val lyt_parent: View = view.findViewById(R.id.lyt_parent)

        fun bind(item: ModelListIndexRumah) {
            tv_index_dinding.text = Html.fromHtml(item.index_dinding)
            tv_index_atap.text = Html.fromHtml(item.index_atap)
            tv_index_lantai.text = Html.fromHtml(item.index_lantai)
            tv_kondisi_atap.text = Html.fromHtml(item.kondisi_atap)
            tv_kondisi_lantai.text = Html.fromHtml(item.kondisi_lantai)
            tv_kondisi_dinding.text = Html.fromHtml(item.kondisi_dinding)
            tv_poin_dinding.text = Html.fromHtml(item.poin_dinding)
            tv_poin_atap.text = Html.fromHtml(item.poin_atap)
            tv_poin_lantai.text = Html.fromHtml(item.poin_lantai)
            tv_status_milik.text = Html.fromHtml(item.status_milik)
        }
    }

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_section: TextView = view.findViewById(R.id.title_section)
    }


}
