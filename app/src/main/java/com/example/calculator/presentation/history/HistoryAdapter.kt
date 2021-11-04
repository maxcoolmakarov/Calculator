package com.example.calculator.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.calculator.databinding.HistoryItemBinding

class HistoryAdapter(
    private val onItemClicked: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryViewHolder>() {

    private var data: List<HistoryItem> = emptyList()

    fun setData(data: List<HistoryItem>) {
        this.data = data.reversed()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent)
        return HistoryViewHolder(
            HistoryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = data[position]
        with (holder.bindings) {
            expression.text = item.expression
            result.text = item.result
            root.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class HistoryViewHolder(val bindings: HistoryItemBinding) : RecyclerView.ViewHolder(bindings.root)