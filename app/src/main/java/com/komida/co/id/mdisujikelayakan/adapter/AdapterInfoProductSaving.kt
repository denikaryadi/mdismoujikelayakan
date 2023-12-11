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
import com.komida.co.id.mdisujikelayakan.model.ModelListProductSaving


class AdapterInfoProductSaving(private val itemList: List<ModelListProductSaving>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_ITEM = 1
        private const val VIEW_SECTION = 0
    }

    private var ctx: Context? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: ModelListProductSaving?, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View?, obj: ModelListProductSaving?, position: Int)
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
            val itemView = inflater.inflate(R.layout.item_data_product_saving, parent, false)
            OriginalViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val p: ModelListProductSaving = itemList[position]
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
            view.title_section.text = p.product_name
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isHeader) VIEW_SECTION else VIEW_ITEM
    }

    inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_product_id_saving: TextView = view.findViewById(R.id.tv_product_id_saving)
        val tv_product_name_saving: TextView = view.findViewById(R.id.tv_product_name_saving)
        val tv_deposit_saving: TextView = view.findViewById(R.id.tv_deposit_saving)
        val tv_flag_saving: TextView = view.findViewById(R.id.tv_flag_saving)
        val tv_kode_ukagt: TextView = view.findViewById(R.id.tv_kode_ukagt)
        val tv_kode_ukprodsav: TextView = view.findViewById(R.id.tv_kode_ukprodsav)

        // val bt_show_dialog: ImageButton = view.findViewById(R.id.bt_show_dialog)
        val lyt_parent: View = view.findViewById(R.id.lyt_parent)



        fun bind(item: ModelListProductSaving) {
            tv_product_id_saving.text = Html.fromHtml(item.product_id)
            tv_product_name_saving.text = Html.fromHtml(item.product_name)
            tv_deposit_saving.text = Html.fromHtml(item.deposit)
            tv_flag_saving.text = Html.fromHtml(item.flag)
            tv_kode_ukagt.text = Html.fromHtml(item.kode_uk)
            tv_kode_ukprodsav.text = Html.fromHtml(item.kode_ukprodsav)
        }
    }

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_section: TextView = view.findViewById(R.id.title_section)
    }
}
