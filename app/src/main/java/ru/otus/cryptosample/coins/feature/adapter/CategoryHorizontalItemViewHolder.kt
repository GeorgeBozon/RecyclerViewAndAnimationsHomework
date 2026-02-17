package ru.otus.cryptosample.coins.feature.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cryptosample.coins.feature.adapter.CoinsAdapterItem.CoinItem
import ru.otus.cryptosample.databinding.ItemHorizontalCategoryBinding

class CategoryHorizontalItemViewHolder(
    binding: ItemHorizontalCategoryBinding,
    sharedPool: RecyclerView.RecycledViewPool
) :
    RecyclerView.ViewHolder(binding.root) {
    private val _adapter = CategoryHorizontalAdapter()

    init {
        binding.horizontalRecycler.apply {
            setRecycledViewPool(sharedPool)
            layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = _adapter
            setHasFixedSize(true)
            itemAnimator = null
        }
    }

    fun bind(items: List<CoinItem>) {
        _adapter.submitList(items)
    }
}