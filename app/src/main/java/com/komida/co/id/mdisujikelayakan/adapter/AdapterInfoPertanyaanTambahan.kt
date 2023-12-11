package com.komida.co.id.mdisujikelayakan.adapter

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.komida.co.id.mdisujikelayakan.R
import com.komida.co.id.mdisujikelayakan.model.ModelListPertanyaanPPi
import com.komida.co.id.mdisujikelayakan.model.ModelListPertanyaanTambahan
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class AdapterInfoPertanyaanTambahan(private val itemList: MutableList<ModelListPertanyaanTambahan>, private val parentLayout: View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_ITEM = 1
        private const val VIEW_SECTION = 0
    }

    private var ctx: Context? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: ModelListPertanyaanTambahan?, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: AdapterInfoPertanyaanTambahan.OnItemClickListener?) {
        mOnItemClickListener = mItemClickListener
    }
    fun updateList(newList: List<ModelListPertanyaanTambahan>) {
        val diffResult = DiffUtil.calculateDiff(AdapterDiffCallback(itemList, newList))
        itemList.clear()
        itemList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class AdapterDiffCallback(
        private val oldList: List<ModelListPertanyaanTambahan>,
        private val newList: List<ModelListPertanyaanTambahan>
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
            return oldItem.id_soal == newItem.id_soal
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
            val itemView = inflater.inflate(R.layout.item_data_ppi_pertanyaan_tambahan, parent, false)
            OriginalViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: ModelListPertanyaanTambahan = itemList[position]
        if (holder is OriginalViewHolder) {
            val view = holder as OriginalViewHolder
            view.bind(item)
            view.lyt_parent.setOnClickListener {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(view.itemView, item, position)
                }
            }
        } else if (holder is SectionViewHolder) {
            val view = holder as SectionViewHolder
            view.title_section.text = item.pertanyaan
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].isHeader) VIEW_SECTION else VIEW_ITEM
    }

    inner class OriginalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val radioGroupAnswers: RadioGroup = itemView.findViewById(R.id.radioGroupAnswers)
        val lin_pilihan_ganda: LinearLayout = itemView.findViewById(R.id.lin_pilihan_ganda)



        val tv_urutan: TextView = view.findViewById(R.id.tv_urutan)
        val tv_tipe_pertanyaan: TextView = view.findViewById(R.id.tv_tipe_pertanyaan)
        val tv_status_pertanyaan: TextView = view.findViewById(R.id.tv_status_pertanyaan)
        val tv_pertanyaan: TextView = view.findViewById(R.id.tv_pertanyaan)
        val tv_tambahan_pertanyaan: TextView = view.findViewById(R.id.tv_tambahan_pertanyaan)
        val layout_keluarga: LinearLayout = view.findViewById(R.id.layout_keluarga)

        val v_tv_jml_pr : TextView = view.findViewById(R.id.v_jumlah_pr)
        val v_tv_jml_pr_sekolah : TextView = view.findViewById(R.id.v_jml_pr_sekolah)
        val v_tv_jml_lk : TextView = view.findViewById(R.id.v_jml_lk)
        val v_tv_jml_lk_sekolah : TextView = view.findViewById(R.id.v_jml_lk_sekolah)
        val v_tv_jml_total : TextView = view.findViewById(R.id.v_total_pr_lk)
        val v_tv_jml_total_sekolah : TextView = view.findViewById(R.id.v_total_pr_lk_sekolah)


        val lay_cent: LinearLayout = view.findViewById(R.id.laycen)
        val lyt_parent: View = view.findViewById(R.id.lyt_parent)
        val tv_jml_pr : TextView = parentLayout.findViewById(R.id.tv_jml_pr)
        val tv_jml_pr_sekolah : TextView = parentLayout.findViewById(R.id.tv_jml_pr_sekolah)
        val tv_jml_lk : TextView = parentLayout.findViewById(R.id.tv_jml_lk)
        val tv_jml_lk_sekolah : TextView = parentLayout.findViewById(R.id.tv_jml_lk_sekolah)
        val tv_jml_total : TextView = parentLayout.findViewById(R.id.tv_jml_total)
        val tv_jml_total_sekolah : TextView = parentLayout.findViewById(R.id.tv_jml_total_sekolah)
        val tv_id_jawaban: TextView = parentLayout.findViewById(R.id.tv_id_jawaban)
        val tv_id_soal: TextView = parentLayout.findViewById(R.id.tv_id_soal)
        val tv_pg_answer: TextView = parentLayout.findViewById(R.id.tv_pg_answer)
        val tv_flag_view: TextView = view.findViewById(R.id.tv_flag_view)



        fun bind(item: ModelListPertanyaanTambahan) {
            tv_urutan.text = item.urutan.toString()
            tv_id_soal.text = item.id_soal
            tv_tipe_pertanyaan.text = item.tipe_pertanyaan
            tv_status_pertanyaan.text = item.status
            tv_pertanyaan.text = item.pertanyaan
            tv_flag_view.text = item.flag_view

            // tv_score_answer.text = "100"
            val str_tv_flag_view = tv_flag_view.text.toString()
            if (str_tv_flag_view == "view") {
                lay_cent.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.amber_100))
                tv_tambahan_pertanyaan.visibility =View.GONE
            }
            radioGroupAnswers.removeAllViews()
            lin_pilihan_ganda.removeAllViews()
            val count = item.pg.count()
           // println("Count: $count")
            for ((index, answer) in item.pg.withIndex()) {

                val radioButton = RadioButton(itemView.context)
                val textView = TextView(itemView.context)

                val parts = answer.split("^")
                val pg = parts.getOrNull(0) ?: ""
                val jawaban = parts.getOrNull(1) ?: ""
                val urutan = parts.getOrNull(2) ?: ""
                val id_soal = parts.getOrNull(3) ?: ""
                val nourut = parts.getOrNull(4) ?: ""
                val flag_view = parts.getOrNull(5) ?: ""

                val s_jml_pr = parts.getOrNull(6) ?: ""
                val s_jml_pr_sklh = parts.getOrNull(7) ?: ""
                val s_jml_lk = parts.getOrNull(8) ?: ""
                val s_jml_lk_sklh = parts.getOrNull(9) ?: ""
                val s_total_jml = parts.getOrNull(10) ?: ""
                val s_total_jml_sklh = parts.getOrNull(11) ?: ""


                tv_tambahan_pertanyaan.visibility =View.GONE
                //println("TEST"+str_tv_flag_view+"-"+flag_view+"--"+id_soal+"--"+pg)
                if(str_tv_flag_view=="view")
                {
                    layout_keluarga.visibility =View.GONE
                    textView.text = "$pg . $jawaban"
                    textView.id = index
                    textView.textSize = 14f // Set the text size as needed
                    textView.setTypeface(null, Typeface.BOLD) // Set text to bold
                    textView.setTextColor(Color.RED)// Set text color
                    if (flag_view == "checked") {
                        lin_pilihan_ganda.addView(textView)

                        // radioButton.isEnabled = true // Enable the RadioButton
                        // radioButton.isChecked = true
                        // item.selectedAnswerIndex = index

                    }
                    /*
                    else
                    {
                        radioButton.isChecked = false
                        radioButton.isEnabled = false // Enable the RadioButton
                        item.selectedAnswerIndex = index

                    }

                     */
                    if(urutan=="14")
                    {
                        layout_keluarga.visibility =View.VISIBLE

                        v_tv_jml_pr.text =s_jml_pr.toString()
                        v_tv_jml_pr_sekolah.text =s_jml_pr_sklh.toString()
                        v_tv_jml_lk.text =s_jml_lk.toString()
                        v_tv_jml_lk_sekolah.text =s_jml_lk_sklh.toString()
                        v_tv_jml_total.text =s_total_jml.toString()
                        v_tv_jml_total_sekolah.text =s_total_jml_sklh.toString()
                    }
                    if (urutan == "16") {
                        tv_tambahan_pertanyaan.apply {
                            visibility = View.VISIBLE
                            text = "Informasi Tentang Kondisi Anggota"
                        }
                    }
                    val validValues = arrayOf("17", "18", "19")

                    if (urutan in validValues) {
                        tv_urutan.text = ""
                    }

                    if (urutan == "20") {
                        tv_urutan.text = "17"
                    }
                    if (urutan == "21") {
                        tv_urutan.text = "18"
                    }
                    if (urutan == "22") {
                        tv_urutan.text = "19"
                        tv_tambahan_pertanyaan.apply {
                            visibility = View.VISIBLE
                            text = "Ditanyakan pada pembiayaan kedua, dst"
                        }
                    }
                    if (urutan == "23") {
                        tv_urutan.text = "20"
                    }
                    if (urutan == "24") {
                        tv_urutan.text = ""
                    }
                }
                else {
                    radioButton.text = "$pg . $jawaban"
                    radioButton.id = index
                    radioGroupAnswers.addView(radioButton)
                    if (flag_view == "checked") {
                        radioButton.isEnabled = true // Enable the RadioButton
                        radioButton.isChecked = true
                        item.selectedAnswerIndex = index

                    }
                    else
                    {
                        radioButton.isChecked = false
                        radioButton.isEnabled = true // Enable the RadioButton
                        radioButton.isChecked = index == item.selectedAnswerIndex

                    }
                    if(urutan=="14"){
                        if(s_jml_pr.isNullOrEmpty())
                        {
                            tv_jml_pr.text = "0"
                            tv_jml_pr_sekolah.text = "0"
                            tv_jml_lk.text = "0"
                            tv_jml_lk_sekolah.text = "0"
                            tv_jml_total.text = "0"
                            tv_jml_total_sekolah.text = "0"
                            tv_pg_answer.text = "B"
                        }
                        else {
                            if (pg == "A") {
                                tv_jml_pr.text = s_jml_pr.toString()
                                tv_jml_pr_sekolah.text = s_jml_pr_sklh.toString()
                                tv_jml_lk.text = s_jml_lk.toString()
                                tv_jml_lk_sekolah.text = s_jml_lk_sklh.toString()
                                tv_jml_total.text = s_total_jml.toString()
                                tv_jml_total_sekolah.text = s_total_jml_sklh.toString()
                                tv_pg_answer.text = pg
                            }
                        }
                    }
                    if (urutan == "16") {
                            tv_tambahan_pertanyaan.apply {
                                visibility = View.VISIBLE
                                text = "Informasi Tentang Kondisi Anggota"
                            }
                        }
                        val validValues = arrayOf("17", "18", "19")

                        if (urutan in validValues) {
                            tv_urutan.text = ""
                        }

                        if (urutan == "20") {
                            tv_urutan.text = "17"
                        }
                        if (urutan == "21") {
                            tv_urutan.text = "18"
                        }
                        if (urutan == "22") {
                            tv_urutan.text = "19"
                            tv_tambahan_pertanyaan.apply {
                                visibility = View.VISIBLE
                                text = "Ditanyakan pada pembiayaan kedua, dst"
                            }
                        }
                        if (urutan == "23") {
                            tv_urutan.text = "20"
                        }
                        if (urutan == "24") {
                            tv_urutan.text = ""
                        }
                        radioButton.isChecked = index == item.selectedAnswerIndex
                        radioButton.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                val str_tv_jml_pr = tv_jml_pr.text.toString()
                                val str_tv_jml_pr_sekolah = tv_jml_pr_sekolah.text.toString()
                                val str_tv_jml_lk = tv_jml_lk.text.toString()
                                val str_tv_jml_lk_sekolah = tv_jml_lk_sekolah.text.toString()
                                val str_tv_jml_total = tv_jml_total.text.toString()
                                val str_tv_jml_total_sekolah = tv_jml_total_sekolah.text.toString()

                                item.selectedAnswerIndex = index

                                tv_id_jawaban.text = pg
                                tv_id_soal.text = id_soal

                                if (urutan == "14" && pg == "B") {
                                    tv_jml_pr.text = ""
                                    tv_jml_pr_sekolah.text = ""
                                    tv_jml_lk.text = ""
                                    tv_jml_lk_sekolah.text = ""
                                    tv_jml_total.text = ""
                                    tv_jml_total_sekolah.text = ""
                                    tv_pg_answer.text = "B"

                                }

                                if (urutan == "14" && pg == "A") {
                                    //showInputKeluargaPPIDialog(itemView.context,id_soal)
                                    val dialog = Dialog(itemView.context)
                                    dialog.setContentView(R.layout.dialog_input_keluarga_ppi)


                                    val btClose = dialog.findViewById<ImageButton>(R.id.bt_close)
                                    val btn_simpankeluarga =
                                        dialog.findViewById<Button>(R.id.btn_simpankeluarga)


                                    val tv_kode_uk_agt =
                                        dialog.findViewById<TextView>(R.id.tv_kode_uk_agt)
                                    val tilPerempuan =
                                        dialog.findViewById<TextInputLayout>(R.id.tilPerempuan)
                                    val jumlah_pr =
                                        dialog.findViewById<TextInputEditText>(R.id.jumlah_pr)
                                    val jml_pr_sekolah =
                                        dialog.findViewById<TextInputEditText>(R.id.jml_pr_sekolah)

                                    val tilLakiLaki =
                                        dialog.findViewById<TextInputLayout>(R.id.tilLakiLaki)
                                    val jml_lk = dialog.findViewById<TextInputEditText>(R.id.jml_lk)
                                    val jml_lk_sekolah =
                                        dialog.findViewById<TextInputEditText>(R.id.jml_lk_sekolah)

                                    val tilTotal = dialog.findViewById<TextInputLayout>(R.id.tilTotal)
                                    val total_pr_lk =
                                        dialog.findViewById<TextInputEditText>(R.id.total_pr_lk)
                                    val total_pr_lk_sekolah =
                                        dialog.findViewById<TextInputEditText>(R.id.total_pr_lk_sekolah)

                                    jumlah_pr.text =
                                        Editable.Factory.getInstance().newEditable(str_tv_jml_pr)
                                    jml_pr_sekolah.text = Editable.Factory.getInstance()
                                        .newEditable(str_tv_jml_pr_sekolah)
                                    jml_lk.text =
                                        Editable.Factory.getInstance().newEditable(str_tv_jml_lk)
                                    jml_lk_sekolah.text = Editable.Factory.getInstance()
                                        .newEditable(str_tv_jml_lk_sekolah)
                                    total_pr_lk.text =
                                        Editable.Factory.getInstance().newEditable(str_tv_jml_total)
                                    total_pr_lk_sekolah.text = Editable.Factory.getInstance()
                                        .newEditable(str_tv_jml_total_sekolah)


                                    val calculateTotal: () -> Unit = {
                                        val jumlahPrText = jumlah_pr.text.toString().replace(",", "")
                                        val jmlLkText = jml_lk.text.toString().replace(",", "")

                                        val jumlahPrValue =
                                            if (jumlahPrText.isNotEmpty()) jumlahPrText.toLong() else 0L
                                        val jmlLkValue =
                                            if (jmlLkText.isNotEmpty()) jmlLkText.toLong() else 0L
                                        val total = jumlahPrValue + jmlLkValue
                                        val formatter = DecimalFormat("#,###,###,###")
                                        total_pr_lk.text = Editable.Factory.getInstance()
                                            .newEditable(formatter.format(total))
                                    }

                                    val calculateTotalSekolah: () -> Unit = {
                                        val jumlahPrText = jumlah_pr.text.toString().replace(",", "")
                                        val jmlLkText = jml_lk.text.toString().replace(",", "")
                                        val jumlahPrTextSekolah =
                                            jml_pr_sekolah.text.toString().replace(",", "")
                                        val jmlLkTextSekolah =
                                            jml_lk_sekolah.text.toString().replace(",", "")

                                        val jumlahPrTextValue =
                                            if (jumlahPrText.isNotEmpty()) jumlahPrText.toLong() else 0L
                                        val jmlLkTextValue =
                                            if (jmlLkText.isNotEmpty()) jmlLkText.toLong() else 0L
                                        val jumlahPrTextSekolahValue =
                                            if (jumlahPrTextSekolah.isNotEmpty()) jumlahPrTextSekolah.toLong() else 0L
                                        val jmlLkTextSekolahValue =
                                            if (jmlLkTextSekolah.isNotEmpty()) jmlLkTextSekolah.toLong() else 0L

                                        tilPerempuan.error = null
                                        tilLakiLaki.error = null
                                        if (jumlahPrTextSekolahValue > jumlahPrTextValue) {
                                            tilPerempuan.error =
                                                "Jumlah Perempuan Sekolah Tidak Boleh lebih besar dari Jumlah yg di Input"
                                            jml_pr_sekolah.requestFocus()

                                        } else if (jmlLkTextSekolahValue > jmlLkTextValue) {
                                            tilLakiLaki.error =
                                                "Jumlah Laki2 Sekolah Tidak Boleh lebih besar dari Jumlah yg di Input"
                                            jml_lk_sekolah.requestFocus()

                                        } else {
                                            val total = jumlahPrTextSekolahValue + jmlLkTextSekolahValue

                                            val formatter = DecimalFormat("#,###,###,###")
                                            total_pr_lk_sekolah.text = Editable.Factory.getInstance()
                                                .newEditable(formatter.format(total))
                                        }
                                    }

                                    val textWatcher: (TextInputEditText, () -> Unit) -> TextWatcher =
                                        { editText, calculation ->
                                            object : TextWatcher {
                                                override fun beforeTextChanged(
                                                    s: CharSequence?,
                                                    start: Int,
                                                    count: Int,
                                                    after: Int
                                                ) {
                                                }

                                                override fun onTextChanged(
                                                    s: CharSequence?,
                                                    start: Int,
                                                    before: Int,
                                                    count: Int
                                                ) {
                                                }

                                                override fun afterTextChanged(s: Editable?) {
                                                    editText.removeTextChangedListener(this)

                                                    try {
                                                        var originalString = s.toString()
                                                        val longval: Long
                                                        if (originalString.isNullOrEmpty()) {
                                                            originalString = "0"
                                                        }
                                                        if (originalString.contains(",")) {
                                                            originalString =
                                                                originalString.replace(",", "")
                                                        }

                                                        longval = originalString.toLong()

                                                        val formatter: DecimalFormat =
                                                            NumberFormat.getInstance(Locale.US) as DecimalFormat
                                                        formatter.applyPattern("#,###,###,###")
                                                        val formattedString: String =
                                                            formatter.format(longval)

                                                        editText.setText(formattedString)
                                                        calculation()
                                                        editText.setSelection(formattedString.length)
                                                    } catch (nfe: NumberFormatException) {
                                                        nfe.printStackTrace()
                                                    }

                                                    editText.addTextChangedListener(this)
                                                }
                                            }
                                        }

                                    jumlah_pr.addTextChangedListener(
                                        textWatcher(
                                            jumlah_pr,
                                            calculateTotal
                                        )
                                    )
                                    jml_pr_sekolah.addTextChangedListener(
                                        textWatcher(
                                            jml_pr_sekolah,
                                            calculateTotalSekolah
                                        )
                                    )
                                    jml_lk.addTextChangedListener(textWatcher(jml_lk, calculateTotal))
                                    jml_lk_sekolah.addTextChangedListener(
                                        textWatcher(
                                            jml_lk_sekolah,
                                            calculateTotalSekolah
                                        )
                                    )


                                    btClose.setOnClickListener {
                                        dialog.dismiss()
                                    }
                                    btn_simpankeluarga.setOnClickListener {


                                        tv_jml_pr.text = jumlah_pr.text.toString()
                                        tv_jml_pr_sekolah.text = jml_pr_sekolah.text.toString()
                                        tv_jml_lk.text = jml_lk.text.toString()
                                        tv_jml_lk_sekolah.text = jml_lk_sekolah.text.toString()
                                        tv_jml_total.text = total_pr_lk.text.toString()
                                        tv_jml_total_sekolah.text = total_pr_lk_sekolah.text.toString()
                                        tv_pg_answer.text = pg
                                        dialog.dismiss()
                                    }

                                    dialog.show()

                                }


                        }
                    }
                }
            }
        }




    }



    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title_section: TextView = view.findViewById(R.id.title_section)
    }
}
