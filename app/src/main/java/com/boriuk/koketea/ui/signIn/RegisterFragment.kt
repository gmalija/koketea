package com.boriuk.koketea.ui.signIn

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.boriuk.koketea.databinding.FragmentRegisterBinding
import com.boriuk.koketea.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class RegisterFragment : BaseFragment() {

    // Firebase
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // UI
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar(binding.progressBar)

        // Creamos una instancia para crear nuestra autenticación y guardar el usuario
        auth = Firebase.auth
        // Creamos una instancia para guardar los datos del usuario en nuestra base  de datos
        database = Firebase.firestore

        // Click listeners
        binding.signUpBtn.setOnClickListener{
            register()
        }
    }

    private fun createNewAccount() {

        showProgressBar()
        val fullName = binding.fullNameEt.text.toString()
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()

        // Verificamos que los campos estén llenos
        if (!TextUtils.isEmpty(fullName)
            && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            // Vamos a dar de alta el usuario con el correo y la contraseña
            activity?.let {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(it) {

                        // Si está en este método quiere decir que salio bien en la autenticación
                        // Una vez que se dio de alta la cuenta vamos a dar de alta la información en la base de datos
                        // Vamos a obtener el id del usuario con que accedio con currentUser
                        val user: FirebaseUser = auth.currentUser!!

                        // Enviamos email de verificación a la cuenta del usuario
                        verifyEmail(user)

                        // Create a new user with a first and last name
                        val userTemp = hashMapOf(
                            "nombre" to fullName,
                            "email" to email,
                            "password" to password
                        )

                        // Damos de alta la información del usuario enviamos el la referencia para guardarlo en la base de datos
                        // de preferencia enviamos el id para que no se repita
                        // Add a new document with a generated ID
                        database.collection("users")
                            .add(userTemp)
                            .addOnSuccessListener { documentReference ->
                                Timber.d("DocumentSnapshot added with ID: ${documentReference.id}")
                                //Por último nos vamos a la vista home
                                updateUserInfoAndGoHome()
                            }
                            .addOnFailureListener { e ->
                                Timber.e(e)
                                Toast.makeText(context, "Error en la autenticación.", Toast.LENGTH_SHORT).show()
                            }

                    }.addOnFailureListener{
                        // Si el registro falla se mostrara este mensaje
                        Toast.makeText(context, "Error en la autenticación.", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        }
        hideProgressBar()
    }
    //llamamos el método de crear cuenta en la accion registrar
    fun register(){
        createNewAccount()
    }

    private fun updateUserInfoAndGoHome() {
        // Navegamos a Home
        val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun verifyEmail(user: FirebaseUser) {
        activity?.let {
            user.sendEmailVerification()
                // Verificamos que la tarea se realizó correctamente
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context,
                            "Email " + user.email,
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context,
                            "Error al verificar el correo ",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}