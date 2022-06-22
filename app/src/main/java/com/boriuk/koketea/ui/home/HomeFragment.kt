package com.boriuk.koketea.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.boriuk.koketea.databinding.FragmentHomeBinding
import com.boriuk.koketea.domain.PrendaItem
import com.boriuk.koketea.domain.Prendas
import com.boriuk.koketea.ui.base.BaseFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import timber.log.Timber

class HomeFragment : BaseFragment() {

    // UI
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Firebase
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

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

        // Creamos una instancia para guardar los datos del usuario en nuestra base  de datos
        database = Firebase.firestore
        storage = Firebase.storage

        // Cerrar sesion
//        Firebase.auth.signOut()
    }

    override fun onStart() {
        super.onStart()
        readDataFromFirestore()
    }

    private fun readDataFromFirestore(){
        showProgressBar()
        database.collection("prendas")
            .get()
            .addOnSuccessListener { result ->
                val prendas = Prendas()
                for (document in result) {
                    val firebaseImage = document.getString("imagen")
                    val prendaItem = PrendaItem(
                        id = document.id,
                        descripcion = document.getString("descripcion"),
                        precio = document.getLong("precio"),
                        imagen = storage.getReference(firebaseImage!!)
                    )
                    prendas.prendasList.add(prendaItem)
                }

                // This will pass the ArrayList to our Adapter
                val adapter = context?.let {
                    HomeAdapter(prendas.prendasList, it) { id ->
                        // Navegamos a Details pasando el ID por argumentos
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
                        view?.findNavController()?.navigate(action)
                    }
                }

                // Setting the Adapter with the recyclerview
                // this creates a vertical layout Manager
                binding.rvHome.layoutManager = LinearLayoutManager(context)
                binding.rvHome.adapter = adapter

            }
            .addOnFailureListener { exception ->
                Timber.w("Error getting documents." + exception)
            }
        hideProgressBar()

    }

}