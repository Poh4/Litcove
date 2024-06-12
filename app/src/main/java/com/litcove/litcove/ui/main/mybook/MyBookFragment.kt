package com.litcove.litcove.ui.main.mybook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.litcove.litcove.databinding.FragmentMyBookBinding

class MyBookFragment : Fragment() {

    private var _binding: FragmentMyBookBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myBookViewModel =
            ViewModelProvider(this)[MyBookViewModel::class.java]

        _binding = FragmentMyBookBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tabLayout: TabLayout = binding.tabLayout
        val sections = arrayOf("History", "Collection")

        for (section in sections) {
            tabLayout.addTab(tabLayout.newTab().setText(section))
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // Ganti genre berdasarkan tab yang dipilih
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Opsional: lakukan sesuatu saat tab tidak lagi dipilih
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Opsional: lakukan sesuatu saat tab dipilih kembali
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}