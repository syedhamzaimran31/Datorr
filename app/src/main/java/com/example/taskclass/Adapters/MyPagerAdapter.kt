package com.example.taskclass.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.taskclass.Fragments.Basic
import com.example.taskclass.Fragments.Advance
import com.example.taskclass.Fragments.Pro

class MyPagerAdapter(fm: androidx.fragment.app.FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int):Fragment{
        return when (position) {
            0 -> {
                Basic()
            }
            1 -> {Advance()}
            else -> {
                return Pro()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Basic"
            1 -> "Advance"
            else -> {
                return "Pro"
            }
        }
    }
}
