package com.litcove.litcove.ui.main.mybook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        val textView: TextView = binding.textMyBook
        myBookViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}