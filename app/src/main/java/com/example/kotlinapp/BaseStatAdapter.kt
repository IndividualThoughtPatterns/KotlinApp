package com.example.kotlinapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.BaseStatElementBinding

class BaseStatAdapter(
    private val baseStatList: List<BaseStat>,
    private val fillProgressBar: (
        progressBar: ProgressBar,
        currentValue: Int,
        color: Int
    ) -> Unit,
    private val color: Int
) : RecyclerView.Adapter<BaseStatAdapter.BaseStatsViewHolder>() {

    inner class BaseStatsViewHolder(private val binding: BaseStatElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(baseStatElement: BaseStat) {
            binding.baseStatsLabelTextView.text = baseStatElement.baseStatName
            binding.baseStatsValueTextView.text = baseStatElement.baseStatStringValue
            binding.baseStatsLabelTextView.setTextColor(color)
            fillProgressBar(
                binding.hpProgressBar,
                baseStatElement.baseStatValue,
                color
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseStatsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BaseStatElementBinding.inflate(inflater, parent, false)
        return BaseStatsViewHolder(binding)
    }

    override fun getItemCount() = baseStatList.size

    override fun onBindViewHolder(holder: BaseStatsViewHolder, position: Int) {
        holder.bind(baseStatList[position])
    }
}