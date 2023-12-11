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
import com.komida.co.id.mdisujikelayakan.model.ModelListAgtPendapatan
import com.komida.co.id.mdisujikelayakan.model.ModelListIndexRumah


class AdapterAnggotaPendapatan(private val itemList: List<ModelListAgtPendapatan>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_ITEM = 1
        private const val VIEW_SECTION = 0
    }

    private var ctx: Context? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: ModelListAgtPendapatan?, position: Int)
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
            val itemView = inflater.inflate(R.layout.item_data_agt_pendapatan, parent, false)
            OriginalViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val p: ModelListAgtPendapatan = itemList[position]
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
            view.title_section.text = p.kode_uk_pendapatan
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isHeader) VIEW_SECTION else VIEW_ITEM
    }

    inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tv_kode_uk_pen:TextView = view.findViewById(R.id.tv_kode_uk_pen)
        val tv_kode_uk_agt:TextView = view.findViewById(R.id.tv_kode_uk_agt)
        val tv_pendapatan_suami_tetap:TextView = view.findViewById(R.id.tv_pendapatan_suami_tetap)
        val tv_pendapatan_suami_tidak_tetap:TextView = view.findViewById(R.id.tv_pendapatan_suami_tidak_tetap)
        val tv_total_pendapatan_suami_per_bulan:TextView = view.findViewById(R.id.tv_total_pendapatan_suami_per_bulan)
        val tv_pendapatan_istri_tetap:TextView = view.findViewById(R.id.tv_pendapatan_istri_tetap)
        val tv_pendapatan_istri_tidak_tetap:TextView = view.findViewById(R.id.tv_pendapatan_istri_tidak_tetap)
        val tv_total_pendapatan_istri_per_bulan:TextView = view.findViewById(R.id.tv_total_pendapatan_istri_per_bulan)
        val tv_pendapatan_lainnya_tetap:TextView = view.findViewById(R.id.tv_pendapatan_lainnya_tetap)
        val tv_pendapatan_lainnya_tidak_tetap:TextView = view.findViewById(R.id.tv_pendapatan_lainnya_tidak_tetap)
        val tv_total_pendapatan_lainnya_per_bulan:TextView = view.findViewById(R.id.tv_total_pendapatan_lainnya_per_bulan)
        val tv_total_pendapatan_per_bulan:TextView = view.findViewById(R.id.tv_total_pendapatan_per_bulan)
        val tv_pengeluaran_rt:TextView = view.findViewById(R.id.tv_pengeluaran_rt)
        val tv_pengeluaran_lainnya:TextView = view.findViewById(R.id.tv_pengeluaran_lainnya)
        val tv_total_pengeluaran_per_bulan:TextView = view.findViewById(R.id.tv_total_pengeluaran_per_bulan)
        val tv_total_pendapatan_bersih:TextView = view.findViewById(R.id.tv_total_pendapatan_bersih)
        val lyt_parent: View = view.findViewById(R.id.lyt_parent)

        fun bind(item: ModelListAgtPendapatan) {
            tv_kode_uk_pen.text = Html.fromHtml(item.kode_uk_pendapatan)
            tv_kode_uk_agt.text = Html.fromHtml(item.kode_uk)
            tv_pendapatan_suami_tetap.text = Html.fromHtml(item.suami_tetap)
            tv_pendapatan_suami_tidak_tetap.text = Html.fromHtml(item.suami_tidak_tetap)
            tv_total_pendapatan_suami_per_bulan.text = Html.fromHtml(item.suami_per_bulan)
            tv_pendapatan_istri_tetap.text = Html.fromHtml(item.istri_tetap)
            tv_pendapatan_istri_tidak_tetap.text = Html.fromHtml(item.istri_tidak_tetap)
            tv_total_pendapatan_istri_per_bulan.text = Html.fromHtml(item.istri_per_bulan)
            tv_pendapatan_lainnya_tetap.text = Html.fromHtml(item.pendapatan_lainnya_tetap)
            tv_pendapatan_lainnya_tidak_tetap.text = Html.fromHtml(item.pendapatan_lainnya_tdk_tetap)
            tv_total_pendapatan_lainnya_per_bulan.text = Html.fromHtml(item.pendapatan_lainnya_per_bulan)
            tv_total_pendapatan_per_bulan.text = Html.fromHtml(item.total_pendapatan_per_bulan)
            tv_pengeluaran_rt.text = Html.fromHtml(item.total_pengeluaran_rt)
            tv_pengeluaran_lainnya.text = Html.fromHtml(item.total_pengeluaran_lain)
            tv_total_pengeluaran_per_bulan.text = Html.fromHtml(item.total_pengeluaran_per_bulan)
            tv_total_pendapatan_bersih.text = Html.fromHtml(item.total_pendapatan_bersih_per_bulan)

        }
    }

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_section: TextView = view.findViewById(R.id.title_section)
    }


}
