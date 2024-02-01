package com.android.searchproject

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.android.searchproject.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        fun newInstance(mItems: ArrayList<Document>, favoriteItems: ArrayList<Document>) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("mItems", mItems)
                    putParcelableArrayList("favoriteItem", favoriteItems)
                }
            }
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var mItems: ArrayList<Document> = arrayListOf()
    private var favoriteItems: ArrayList<Document> = arrayListOf()


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mItems = it.getParcelableArrayList("mItems", Document::class.java) as ArrayList<Document>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchAdapter(requireContext(), mItems, favoriteItems)

        adapter.searchClick = object : SearchAdapter.SearchClick {
            override fun onClick(view: View, position: Int) {
                view.isVisible = !view.isVisible
                Toast.makeText(requireContext(), "좋아요 보관함으로 이동", Toast.LENGTH_SHORT).show()

            }
        }

        binding.rvSearchList.adapter = adapter
        binding.rvSearchList.layoutManager = GridLayoutManager(requireContext(), 2)


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}