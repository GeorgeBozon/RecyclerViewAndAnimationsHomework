package ru.otus.cryptosample.coins.feature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cryptosample.databinding.ItemCategoryHeaderBinding
import ru.otus.cryptosample.databinding.ItemCoinBinding
import ru.otus.cryptosample.databinding.ItemHorizontalCategoryBinding

private const val VIEW_TYPE_CATEGORY = 0
private const val VIEW_TYPE_COIN = 1

private const val VIEW_TYPE_HORIZONTAL_CATEGORY = 2

class CoinsListAdapter(private val sharedPool: RecyclerView.RecycledViewPool) :
    ListAdapter<CoinsAdapterItem, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CoinsAdapterItem.CategoryHeader -> VIEW_TYPE_CATEGORY
            is CoinsAdapterItem.CoinItem -> VIEW_TYPE_COIN
            is CoinsAdapterItem.HorizontalCategory -> VIEW_TYPE_HORIZONTAL_CATEGORY
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> CategoryHeaderViewHolder(
                ItemCategoryHeaderBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            VIEW_TYPE_COIN -> CoinViewHolder(
                ItemCoinBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            VIEW_TYPE_HORIZONTAL_CATEGORY -> {
                CategoryHorizontalItemViewHolder(
                    ItemHorizontalCategoryBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    ),
                    sharedPool
                )
            }

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any?>
    ) {
        val item = getItem(position)

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        val payload = payloads.getOrNull(0)

        payload?.let {
            when (it) {
                is Payloads.SingleHighLight -> (holder as CoinViewHolder).updateHighlight((item as CoinsAdapterItem.CoinItem).coin.highlight)

                is Payloads.MultiHighlights -> (holder as CategoryHorizontalItemViewHolder).bind(
                    (item as CoinsAdapterItem.HorizontalCategory).coinItems
                )

                else -> Unit
            }
        }

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        when (item) {
            is CoinsAdapterItem.CategoryHeader -> (holder as CategoryHeaderViewHolder).bind(item.categoryName)

            is CoinsAdapterItem.CoinItem -> (holder as CoinViewHolder).bind(item.coin)

            is CoinsAdapterItem.HorizontalCategory -> (holder as CategoryHorizontalItemViewHolder).bind(
                item.coinItems
            )
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<CoinsAdapterItem>() {

        override fun areItemsTheSame(
            oldItem: CoinsAdapterItem,
            newItem: CoinsAdapterItem
        ): Boolean {
            return when {
                oldItem is CoinsAdapterItem.CoinItem && newItem is CoinsAdapterItem.CoinItem -> {
                    oldItem.coin.id == newItem.coin.id
                }

                oldItem is CoinsAdapterItem.CategoryHeader && newItem is CoinsAdapterItem.CategoryHeader -> {
                    oldItem.categoryName == newItem.categoryName
                }

                oldItem is CoinsAdapterItem.HorizontalCategory && newItem is CoinsAdapterItem.HorizontalCategory -> {
                    oldItem.id == newItem.id
                }

                else -> false

            }
        }

        override fun areContentsTheSame(
            oldItem: CoinsAdapterItem,
            newItem: CoinsAdapterItem
        ): Boolean = oldItem == newItem

        override fun getChangePayload(oldItem: CoinsAdapterItem, newItem: CoinsAdapterItem): Any? {
            return when {
                oldItem is CoinsAdapterItem.CoinItem && newItem is CoinsAdapterItem.CoinItem -> {
                    if (oldItem.coin.highlight != newItem.coin.highlight) Payloads.SingleHighLight else null
                }

                oldItem is CoinsAdapterItem.HorizontalCategory && newItem is CoinsAdapterItem.HorizontalCategory -> {

                    if (hasHighLightsChanges(oldItem, newItem)) Payloads.MultiHighlights else null
                }

                else -> null
            }
        }

        private fun hasHighLightsChanges(
            oldItem: CoinsAdapterItem.HorizontalCategory,
            newItem: CoinsAdapterItem.HorizontalCategory
        ): Boolean {

            oldItem.coinItems.forEach { item ->
                val new = newItem.coinItems.firstOrNull { it.coin.id == item.coin.id }
                if (new?.coin?.highlight != item.coin.highlight) return true
            }

            return false
        }

    }

    private sealed interface Payloads {
        data object SingleHighLight : Payloads

        data object MultiHighlights : Payloads
    }
}