package com.android.patmore.features.foryou.presentation.model

import com.android.patmore.R
import com.android.patmore.databinding.SingleCategoryItemBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.databinding.BindableItem

class CategoryTweetItem(val category: String, val items: List<SingleCategoryTweetItem>) :
    BindableItem<SingleCategoryItemBinding>() {
    override fun bind(binding: SingleCategoryItemBinding, p1: Int) {
        binding.header.text = category

        binding.categoryTweetRecyclerView.adapter = GroupieAdapter().apply {
            addAll(items)
        }
    }

    override fun getLayout(): Int = R.layout.single_category_item
}
