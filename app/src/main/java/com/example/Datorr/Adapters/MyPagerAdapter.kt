package com.example.Datorr.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.Datorr.Fragments.AdvanceFragment
import com.example.Datorr.Fragments.BasicFragment
import com.example.Datorr.Fragments.ProFragment

class MyPagerAdapter(fm: androidx.fragment.app.FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                BasicFragment()
            }

            1 -> {

                AdvanceFragment()

            }

            else -> {

                ProFragment()
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
