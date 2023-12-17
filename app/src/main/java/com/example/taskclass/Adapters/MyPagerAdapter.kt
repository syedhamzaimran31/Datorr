package com.example.taskclass.Adapters

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.taskclass.Fragments.Advance
import com.example.taskclass.Fragments.Basic
import com.example.taskclass.Fragments.Pro

class MyPagerAdapter(fm: androidx.fragment.app.FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Basic()
            }

            1 -> ({
                if (Basic.checkFormSubmit == true) {
                    Advance()
                } else {

                }
            }) as Fragment

            else -> ({
                if (Basic.checkFormSubmit == true) {
                     Pro()
                } else {
                Toast.makeText(con)
                }
            }) as Fragment
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
