package com.boriuk.koketea.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boriuk.koketea.R
import com.boriuk.koketea.base.BaseFragment
import com.boriuk.koketea.databinding.FragmentHomeBinding
import com.boriuk.koketea.databinding.FragmentLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : BaseFragment() {

    // UI
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar(binding.progressBar)

        // Aqui van las referncias a los componentes del fragment
    }

}