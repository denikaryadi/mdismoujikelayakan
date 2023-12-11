package com.komida.co.id.mdisujikelayakan.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.komida.co.id.mdisujikelayakan.R
import com.komida.co.id.mdisujikelayakan.model.ModelListJawabanPPi


class AdapterInfoJawabanPPI(private val itemList: List<ModelListJawabanPPi>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_ITEM = 1
        private const val VIEW_SECTION = 0
    }

    private var ctx: Context? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: ModelListJawabanPPi?, position: Int)
    }


    interface OnItemLongClickListener {
        fun onItemLongClick(view: View?, obj: ModelListJawabanPPi?, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_SECTION) {
            val sectionView = inflater.inflate(R.layout.item_section, parent, false)
            SectionViewHolder(sectionView)
        } else {
            val itemView = inflater.inflate(R.layout.item_data_ppi_jawaban, parent, false)
            OriginalViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val p: ModelListJawabanPPi = itemList[position]
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
            view.title_section.text = p.AnswerDesc
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isHeader) VIEW_SECTION else VIEW_ITEM
    }

    inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
      //  val recyclerViewJawaban: RecyclerView = view.findViewById(R.id.recyclerViewFormPPIJawaban)
        val tv_answer_id: TextView = view.findViewById(R.id.tv_answer_id)
        val tv_answer_desc: TextView = view.findViewById(R.id.tv_answer_desc)
        val tv_answer_poin: TextView = view.findViewById(R.id.tv_answer_poin)
        val tv_answer_score: TextView = view.findViewById(R.id.tv_answer_score)
        val tv_question_id: TextView = view.findViewById(R.id.tv_question_id)

        // val bt_show_dialog: ImageButton = view.findViewById(R.id.bt_show_dialog)
        val lyt_parent: View = view.findViewById(R.id.lyt_answer_ppi)



        fun bind(item: ModelListJawabanPPi) {
            tv_answer_id.text = Html.fromHtml(item.Answer)
            tv_answer_desc.text = Html.fromHtml(item.AnswerDesc)
            tv_answer_poin.text = Html.fromHtml(item.Poin)
            tv_answer_score.text = Html.fromHtml(item.Score)
            tv_question_id.text = Html.fromHtml(item.QuestionID)
        }
    }

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_section: TextView = view.findViewById(R.id.title_section)
    }
}
