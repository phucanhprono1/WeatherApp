package com.example.weatherapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.LocationListItemBinding
import com.example.weatherapp.response.geolocation.LocationKeyResponse
import java.util.Collections


class LocationRecyclerAdapter(
    private var listener: OnLocationLongPressListener
    )
    : RecyclerView.Adapter<LocationRecyclerAdapter.LocationViewHolder>(){

    private var locationList = ArrayList<LocationKeyResponse>()
    private var checkedItems: ArrayList<Boolean> = ArrayList()
    private var isSelectionMode = false

    fun getLocationList() : ArrayList<LocationKeyResponse>{
        return locationList
    }

    fun setListener(listener: OnLocationLongPressListener){
        this.listener = listener
    }


    fun setLocationKeyResponseList(LocationKeyResponseList: List<LocationKeyResponse>){
        this.locationList = ArrayList(LocationKeyResponseList)
        this.checkedItems = ArrayList<Boolean>(Collections.nCopies(LocationKeyResponseList.size,
            false))
        notifyDataSetChanged()
    }

    interface OnLocationLongPressListener {
        fun onLocationLongPress()
    }

     inner class LocationViewHolder(val binding: LocationListItemBinding) : RecyclerView.ViewHolder(binding.root) {
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(
            LocationListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        with(holder){
            with(locationList[position]){
                binding.locationItemName.text = this.EnglishName
                binding.locationItemTemp.text = "30"
                binding.locationItemWeather.text = "Trời nắng"

                if (isSelectionMode) {
                    binding.checkbox.visibility = View.VISIBLE
                    binding.locationItemTemp.visibility = View.GONE
                    binding.tempUnit.visibility = View.GONE
                    binding.locationItemWeather.visibility = View.GONE
                    binding.checkbox.isChecked = checkedItems[position]
                } else {
                    binding.checkbox.visibility = View.GONE
                    binding.locationItemTemp.visibility = View.VISIBLE
                    binding.tempUnit.visibility = View.VISIBLE
                    binding.locationItemWeather.visibility = View.VISIBLE
                }

                holder.itemView.setOnClickListener {
                    if (isSelectionMode) {
                        toggleSelection(position)
                        notifyItemChanged(position)
                    }
                }
                holder.itemView.setOnLongClickListener {
                    isSelectionMode = true
                    toggleSelection(position)
                    notifyDataSetChanged()
                    listener.onLocationLongPress()
                    true
                }

            }
        }
    }

    private fun toggleSelection(position: Int) {
        checkedItems[position] = !checkedItems[position]
    }
    fun exitSelectionMode() {
        isSelectionMode = false
        clearSelections()
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<Int>? {
        val selectedPositions: ArrayList<Int> = ArrayList()
        for (i in checkedItems.indices) {
            if (checkedItems[i]) {
                selectedPositions.add(i)
            }
        }
        return selectedPositions
    }

    fun removeSelectedItems() {
        val selectedPositions = getSelectedItems()
        selectedPositions?.let { positions ->
            positions.sortedDescending().forEach { position ->
                locationList.removeAt(position)
                checkedItems.removeAt(position)
            }
            exitSelectionMode()
            notifyDataSetChanged()
        }
    }

    fun clearSelections() {
        for (i in checkedItems.indices) {
            checkedItems[i] = false
        }
    }

}