package com.patmore.android.features.foryou.presentation.model

import com.patmore.android.R
import com.patmore.android.core.utility.capitalizeFirstLetter
import com.patmore.android.databinding.SingleCategoryItemBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.databinding.BindableItem

class CategoryTweetItem(
    private val category: String,
    private val items: List<SingleCategoryTweetItem>,
    val onClick: (String) -> Unit,
) :
    BindableItem<SingleCategoryItemBinding>() {
    override fun bind(binding: SingleCategoryItemBinding, p1: Int) {
        binding.header.text = category.lowercase().capitalizeFirstLetter()

        binding.categoryTweetRecyclerView.adapter = GroupieAdapter().apply {
            addAll(items)
        }

        binding.headerRelativeLayout.setOnClickListener {
            onClick(category)
        }
    }

    override fun getLayout(): Int = R.layout.single_category_item
}
