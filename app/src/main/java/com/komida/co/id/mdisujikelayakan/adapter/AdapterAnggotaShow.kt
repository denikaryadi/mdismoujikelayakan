package com.komida.co.id.mdisujikelayakan.adapter

import AnggotaList
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.komida.co.id.mdisujikelayakan.R
import com.komida.co.id.mdisujikelayakan.Tools


class AdapterAnggotaShow(private val context: Context, private var items: List<AnggotaList>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: AnggotaList, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    fun setItems(items: List<AnggotaList>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class OriginalViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        val image: ImageView = v.findViewById(R.id.image)
        val name: TextView = v.findViewById(R.id.name)
       val email: TextView = v.findViewById(R.id.nik_ktp)
        val lytParent: View = v.findViewById(R.id.lyt_parent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_people_contacts,
            parent,
            false
        )
        return OriginalViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OriginalViewHolder) {
            val view = holder

            val person = items[position]
            view.name.text = person.name
            Tools.displayImageRound(context, view.image, person.image)

            view.lytParent.setOnClickListener {
                mOnItemClickListener?.onItemClick(it, items[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnLoadMoreListener {
        fun onLoadMore(current_page: Int)
    }
}
