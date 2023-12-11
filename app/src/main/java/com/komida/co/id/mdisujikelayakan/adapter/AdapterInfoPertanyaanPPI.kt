package com.komida.co.id.mdisujikelayakan.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.komida.co.id.mdisujikelayakan.R
import com.komida.co.id.mdisujikelayakan.model.ModelListPertanyaanPPi

    class AdapterInfoPertanyaanPPI(private val itemList: MutableList<ModelListPertanyaanPPi>, private val parentLayout: View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            private const val VIEW_ITEM = 1
            private const val VIEW_SECTION = 0
        }

        private var database: SQLiteDatabase? = null
        private var ctx: Context? = null
        private var mOnItemClickListener: OnItemClickListener? = null
        private var mOnItemLongClickListener: OnItemLongClickListener? = null

        interface OnItemClickListener {
            fun onItemClick(view: View?, obj: ModelListPertanyaanPPi?, position: Int)
        }

        interface OnItemLongClickListener {
            fun onItemLongClick(view: View?, obj: ModelListPertanyaanPPi?, position: Int)
        }
        fun updateList(newList: List<ModelListPertanyaanPPi>) {
            val diffResult = DiffUtil.calculateDiff(AdapterDiffCallback(itemList, newList))
            itemList.clear()
            itemList.addAll(newList)
            diffResult.dispatchUpdatesTo(this)
        }

        class AdapterDiffCallback(
            private val oldList: List<ModelListPertanyaanPPi>,
            private val newList: List<ModelListPertanyaanPPi>
        ) : DiffUtil.Callback() {

            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldList[oldItemPosition]
                val newItem = newList[newItemPosition]
                // Assuming no_urutan is a unique identifier
                return oldItem.no_urutan == newItem.no_urutan
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldList[oldItemPosition]
                val newItem = newList[newItemPosition]
                // Compare other properties to determine if the content is the same
                return oldItem == newItem
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return if (viewType == VIEW_SECTION) {
                val sectionView = inflater.inflate(R.layout.item_section, parent, false)
                SectionViewHolder(sectionView)
            } else {
                val itemView = inflater.inflate(R.layout.item_data_ppi_pertanyaan, parent, false)
                OriginalViewHolder(itemView)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item: ModelListPertanyaanPPi = itemList[position]
            if (holder is OriginalViewHolder) {
                val view = holder as OriginalViewHolder
                view.bind(item)
                view.lyt_parent.setOnClickListener { _ ->
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener!!.onItemClick(view.itemView, item, position)
                    }
                }
            } else if (holder is SectionViewHolder) {
                val view = holder as SectionViewHolder
                view.title_section.text = item.QuestionDesc
            }
        }

        override fun getItemCount(): Int {
            return itemList.size
        }
        fun setDatabase(db: SQLiteDatabase) {
            database = db
        }
        override fun getItemViewType(position: Int): Int {
            return if (itemList[position].isHeader) VIEW_SECTION else VIEW_ITEM
        }

        inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val radioGroupAnswers: RadioGroup = itemView.findViewById(R.id.radioGroupAnswers)
            val lay_cent: LinearLayout = view.findViewById(R.id.laycen) // Replace 'R.id.yourLinearLayout' with the actual ID
            val lin_pilihan_ganda: LinearLayout = itemView.findViewById(R.id.lin_pilihan_ganda)

            val tv_flag_view: TextView = view.findViewById(R.id.tv_flag_view)
            val tv_comp_id: TextView = view.findViewById(R.id.tv_comp_id)
            val tv_question_id: TextView = view.findViewById(R.id.tv_question_id)
            val tv_tipe_pertanyaan: TextView = view.findViewById(R.id.tv_tipe_pertanyaan)
            val tv_status_pertanyaan: TextView = view.findViewById(R.id.tv_status_pertanyaan)
            val tv_question_desc: TextView = view.findViewById(R.id.tv_question_desc)
            val tv_no_urutan: TextView = view.findViewById(R.id.tv_no_urutan)
            val lyt_parent: View = view.findViewById(R.id.lyt_parent)
            val tv_poin_answer: TextView = view.findViewById(R.id.tv_poin_answer)
            val tv_score_poin = parentLayout.findViewById<TextView>(R.id.total_skor_ppi)
            val tv_id_jawaban: TextView = view.findViewById(R.id.tv_id_jawaban)
            val tv_id_soal: TextView = view.findViewById(R.id.tv_id_soal)
            val tv_total_score_poin: TextView = view.findViewById(R.id.tv_total_score_poin)



            fun bind(item: ModelListPertanyaanPPi) {
                tv_no_urutan.text = item.no_urutan.toString()
                tv_comp_id.text = item.Comp_ID
                tv_question_id.text = item.QuestionID
                tv_tipe_pertanyaan.text = item.tipePertanyaan
                tv_status_pertanyaan.text = item.status
                tv_question_desc.text = item.QuestionDesc
              //  tv_score_answer.text = "100"
                tv_flag_view.text = item.flag_view
                radioGroupAnswers.removeAllViews()
                lin_pilihan_ganda.removeAllViews()
                val str_tv_flag_view = tv_flag_view.text.toString()
                if (str_tv_flag_view == "view") {
                    lay_cent.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.amber_100))
                }
                for ((index, answer) in item.pg.withIndex()) {

                    val radioButton = RadioButton(itemView.context)
                    val textView = TextView(itemView.context)

                    val parts = answer.split("^")
                    val pg = parts.getOrNull(0) ?: ""
                    val jawaban = parts.getOrNull(1) ?: ""
                    val poin = parts.getOrNull(2) ?: ""
                    val score = parts.getOrNull(3) ?: ""
                    val id_soal = parts.getOrNull(4) ?: ""
                    val flag_view = parts.getOrNull(5) ?: ""


                    if (str_tv_flag_view == "view") {
                        textView.text = "$pg . $jawaban"
                        textView.id = index
                        textView.textSize = 14f // Set the text size as needed
                        textView.setTypeface(null, Typeface.BOLD) // Set text to bold
                        textView.setTextColor(Color.RED)// Set text color
                        if (flag_view == "checked") {
                            lin_pilihan_ganda.addView(textView)
                            // radioButton.isEnabled = true
                            // radioButton.isChecked = true
                            tv_poin_answer.text = poin
                            tv_id_jawaban.text = pg
                            tv_id_soal.text = id_soal
                            item.selectedAnswerIndex = index
                            // getTotalScore()  // Uncomment this line if needed
                        }
                        //else {
                            //radioButton.isEnabled = false
                            // radioButton.isChecked = false
                           // item.selectedAnswerIndex = index
                        //}


                    }

                    else {
                        radioButton.text = "$pg . $jawaban"
                        radioButton.id = index
                        radioGroupAnswers.addView(radioButton)

                        if (flag_view == "checked") {
                            radioButton.isEnabled = true // Enable the RadioButton
                            radioButton.isChecked = true
                            tv_poin_answer.text = poin
                            tv_id_jawaban.text = pg
                            tv_id_soal.text = id_soal
                            item.selectedAnswerIndex = index
                            getTotalScore()

                        }
                        else
                        {

                            radioButton.isEnabled = true // Enable the RadioButton


                        }

                        radioButton.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                item.selectedAnswerIndex = index
                                tv_poin_answer.text = poin
                                tv_id_jawaban.text = pg
                                tv_id_soal.text = id_soal
                                getTotalScore()
                                // Calculate the total score when a radio button is checked
                            }
                        }

                    }


                }
            }
        fun getTotalScore() {
                // Initialize a variable to store the total score
                var totalScore = 0
                tv_score_poin.text="0"
                // Assuming you have access to the selected answers and their corresponding points
                for (item in itemList) {
                        val selectedAnswerIndex = item.selectedAnswerIndex
                        if (selectedAnswerIndex >= 0 && selectedAnswerIndex < item.pg.size) {
                        val parts = item.pg[selectedAnswerIndex].split("^")
                        val poin = parts.getOrNull(2)?.toIntOrNull() ?: 0
                        totalScore += poin
                        }
                } // return totalScore
            // Set the total score in the tv_score_answer TextView
            tv_score_poin.text = totalScore.toString()
        }

}

inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title_section: TextView = view.findViewById(R.id.title_section)
    }
}
