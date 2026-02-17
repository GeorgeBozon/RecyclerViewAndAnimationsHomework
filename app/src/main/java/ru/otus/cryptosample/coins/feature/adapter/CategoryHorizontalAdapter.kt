package ru.otus.cryptosample.coins.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.otus.cryptosample.coins.feature.adapter.CoinsAdapterItem.CoinItem
import ru.otus.cryptosample.databinding.ItemCoinHorizontalBinding

private const val HIGHLIGHT = "HIGHLIGHT"

class CategoryHorizontalAdapter :
    ListAdapter<CoinItem, HorizontalCoinViewHolder>(CoinDiffUtilCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalCoinViewHolder {
        return HorizontalCoinViewHolder(
            ItemCoinHorizontalBinding.inflate(
                LayoutInflater.from(
                    parent.context,
                ),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(
        holder: HorizontalCoinViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position).coin)
    }

    override fun onBindViewHolder(
        holder: HorizontalCoinViewHolder,
        position: Int,
        payloads: List<Any?>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(getItem(position).coin)
            return
        }

        if (payloads.contains(HIGHLIGHT)) {
            holder.updateHighlight(getItem(position).coin.highlight)
        }
    }

    class CoinDiffUtilCallBack : DiffUtil.ItemCallback<CoinItem>() {
        override fun areItemsTheSame(
            oldItem: CoinItem,
            newItem: CoinItem
        ): Boolean = oldItem.coin.id == newItem.coin.id

        override fun areContentsTheSame(
            oldItem: CoinItem,
            newItem: CoinItem
        ): Boolean = oldItem.coin == newItem.coin

        override fun getChangePayload(oldItem: CoinItem, newItem: CoinItem): Any? =
            if (oldItem.coin.highlight != newItem.coin.highlight) HIGHLIGHT else null
    }
}