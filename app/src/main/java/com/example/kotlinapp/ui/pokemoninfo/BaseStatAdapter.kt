package com.example.kotlinapp.ui.pokemoninfo

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapp.databinding.BaseStatElementBinding

class BaseStatAdapter(
    private val baseStatList: List<BaseStat>,
    private val color: Int
) : RecyclerView.Adapter<BaseStatAdapter.BaseStatsViewHolder>() {

    inner class BaseStatsViewHolder(private val binding: BaseStatElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private fun fillProgressBar(
            progressBar: ProgressBar, currentValue: Int, color: Int
        ) = with(progressBar) {
            progressTintList = ColorStateList.valueOf(color)
            progressBackgroundTintList = ColorStateList.valueOf(color)
            max = 233
            progress = currentValue
        }

        fun bind(baseStatElement: BaseStat) = with(binding) {
            baseStatsLabelTextView.text = baseStatElement.baseStatName
            baseStatsValueTextView.text = baseStatElement.baseStatStringValue
            baseStatsLabelTextView.setTextColor(color)

            fillProgressBar(
                hpProgressBar,
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