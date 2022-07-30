package com.android.patmore.features.foryou.presentation.model

import com.android.patmore.R
import com.android.patmore.core.utility.capitalizeFirstLetter
import com.android.patmore.databinding.SingleCategoryItemBinding
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
