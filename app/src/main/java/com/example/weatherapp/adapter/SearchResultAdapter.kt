package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.SearchResultListItemBinding
import com.example.weatherapp.response.geolocation.LocationKeyResponse

class SearchResultAdapter(
    private var listener: OnResultClickListener
) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    private var resultList = ArrayList<LocationKeyResponse>()

    fun setResultList(result : ArrayList<LocationKeyResponse>){
        this.resultList = result
        notifyDataSetChanged()
    }

    inner class SearchResultViewHolder(val binding: SearchResultListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    interface OnResultClickListener {
        fun onResultClick( location: LocationKeyResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            SearchResultListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        with(holder){
            with(resultList[position]){
                binding.cityName.text = this.EnglishName

                holder.itemView.setOnClickListener {
                    listener.onResultClick(resultList[position])
                }

            }
        }
    }
}