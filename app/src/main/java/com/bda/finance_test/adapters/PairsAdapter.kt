package com.bda.finance_test.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bda.finance_test.databinding.ItemPairsAdapterBinding
import com.bda.finance_test.model.database.entity.CurrencyPair

class PairsAdapter(
    private var pairList: MutableList<CurrencyPair>,
    private val listener: OnItemClickListener): RecyclerView.Adapter<PairsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(pair: CurrencyPair)
        fun onStarClick(pair: CurrencyPair)
    }


    fun setPairList(pairList: List<CurrencyPair>) {
        this.pairList = pairList as MutableList<CurrencyPair>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(_binding = ItemPairsAdapterBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imageStar.setImageResource(
            if(pairList[position].isFavorite()) android.R.drawable.star_big_on
            else android.R.drawable.star_big_off )
        holder.binding.titlePair.setOnClickListener {
            listener.onItemClick(pairList[position])
        }
        holder.binding.imageStar.setOnClickListener {
            listener.onStarClick(pairList[position])
        }
        holder.bind(pairList[position])
    }

    override fun getItemCount(): Int {
        return pairList.size
    }

    class ViewHolder(_binding: ItemPairsAdapterBinding) : RecyclerView.ViewHolder(_binding.root) {
        val binding = _binding
        fun bind (pair: CurrencyPair) {
            binding.pair = pair
            binding.executePendingBindings()
        }
    }
}