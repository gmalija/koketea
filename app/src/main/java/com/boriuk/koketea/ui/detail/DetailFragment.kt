package com.boriuk.koketea.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.boriuk.koketea.databinding.FragmentDetailBinding
import com.boriuk.koketea.domain.PrendaItem
import com.boriuk.koketea.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import timber.log.Timber

class DetailFragment : BaseFragment() {

    // UI
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // Firebase
    private lateinit var database: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    // Arguments
    val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
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
    }

    override fun onStart() {
        super.onStart()
        readDataFromFirestore()
    }

    private fun readDataFromFirestore(){
        showProgressBar()
        database.collection("prendas").document(args.prendaId)
            .get()
            .addOnSuccessListener { result ->
                val firebaseImage = result.getString("imagen")
                val prendaItem = PrendaItem(
                    id = result.id,
                    descripcion = result.getString("descripcion"),
                    precio = result.getLong("precio"),
                    imagen = storage.getReference(firebaseImage!!)
                )
                setUi(prendaItem)
            }
            .addOnFailureListener { exception ->
                Timber.w("Error getting documents." + exception)
            }
        hideProgressBar()
    }

    private fun setUi(prendaItem: PrendaItem) {
        // Set data
        binding.tvName.text = prendaItem.descripcion
        binding.tvPrice.text = prendaItem.precio.toString() + " eur"
        context?.let {
            Glide.with(it)
                .load(prendaItem.imagen)
                .into(binding.imPrenda)
        }
    }

}