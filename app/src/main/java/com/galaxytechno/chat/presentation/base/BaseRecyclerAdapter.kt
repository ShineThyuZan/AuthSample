package com.galaxytechno.chat.presentation.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

abstract class BaseRecyclerAdapter<T, W : Any>(context: Context) :
    RecyclerView.Adapter<BaseViewHolder<W>>() {

    protected  var mData: MutableList<W>? = null
    protected var mLayoutInflator: LayoutInflater

    val items: List<W>
        get() {
            val data = mData
            return data ?: ArrayList()
        }

    init {
        mData = ArrayList()
        mLayoutInflator = LayoutInflater.from(context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<W>, position: Int) {
        holder.setData(mData!![position])
    }

    override fun getItemCount(): Int {
        return mData!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun setNewData(newData: MutableList<W>) {
        mData = ArrayList()
        mData = newData
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun setNewDataList(newData: MutableList<W>) {
        mData = ArrayList()
        mData = newData
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun appendNewData(newData: List<W>) {
        mData!!.addAll(newData)
        Handler().postDelayed({
            notifyDataSetChanged()
        }, 300)
    }

    fun getItemAt(position: Int): W? {
        return if (position < mData!!.size - 1) mData!![position] else null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeData(data: W) {
        mData!!.remove(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun addNewData(data: W) {
        mData!!.add(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        mData = ArrayList()
        notifyDataSetChanged()
    }
}