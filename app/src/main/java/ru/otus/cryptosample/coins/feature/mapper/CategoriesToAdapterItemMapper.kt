package ru.otus.cryptosample.coins.feature.mapper

import ru.otus.cryptosample.coins.feature.CoinCategoryState
import ru.otus.cryptosample.coins.feature.adapter.CoinsAdapterItem
import javax.inject.Inject

class CategoriesToAdapterItemMapper @Inject constructor() {

    fun map(categories: List<CoinCategoryState>): List<CoinsAdapterItem> {

        val adapterItems = mutableListOf<CoinsAdapterItem>()

        categories.forEach { category ->
            adapterItems.add(CoinsAdapterItem.CategoryHeader(category.name))
            if (category.coins.size > 10) {
                adapterItems.add(CoinsAdapterItem.HorizontalCategory(id = category.id, coinItems = category.coins.map {
                    CoinsAdapterItem.CoinItem(
                        it
                    )
                }))
            } else {
                category.coins.forEach { coin ->
                    adapterItems.add(CoinsAdapterItem.CoinItem(coin))
                }
            }
        }

        return adapterItems
    }
}