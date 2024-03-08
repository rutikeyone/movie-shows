package com.ru.movieshows.app.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ru.movieshows.app.databinding.SearchHintItemBinding

class SearchHintAdapter(
    private val searchItems: List<String?>,
    private val listener: SimpleAdapterListener<*>,
) : RecyclerView.Adapter<SearchHintAdapter.Holder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchHintItemBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return Holder(binding)
    }

    override fun getItemCount(): Int = searchItems.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = searchItems[position]
        holder.bind(item, position)
    }

    override fun onClick(view: View) {
        val position = view.tag as Int
        listener.onClickPosition(position)
    }

    inner class Holder(
        private val binding: SearchHintItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String?, position: Int) {
            binding.root.tag = position
            configureNameUI(name)
        }

        private fun configureNameUI(name: String?) = with(binding.hintNameTextView) {
            if(!name.isNullOrEmpty()) {
                visibility = View.VISIBLE
                text = name
            } else {
                visibility = View.INVISIBLE
            }
        }

    }

}