package com.boriuk.koketea.domain

import com.google.firebase.storage.StorageReference

data class Prendas(
    var prendasList: ArrayList<PrendaItem> = arrayListOf()
)

data class PrendaItem(
    var id: String = "",
    var descripcion: String? = "",
    var precio: Long? = 0,
    var imagen: StorageReference
)
