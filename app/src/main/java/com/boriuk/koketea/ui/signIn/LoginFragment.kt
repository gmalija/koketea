package com.boriuk.koketea.ui.signIn

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.boriuk.koketea.databinding.FragmentLoginBinding
import com.boriuk.koketea.ui.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class LoginFragment : BaseFragment() {

    // Creamos nuestra variable de autenticación firebase
    private lateinit var auth: FirebaseAuth

    // UI
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar(binding.progressBar)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Click listeners
        binding.signInBtn.setOnClickListener{
            login()
        }
        binding.signUpTv.setOnClickListener{
            register()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Timber.e("User" + currentUser)
        if(currentUser != null) {
            goHome()
        }
    }

    // Ahora vamos a Iniciar sesión con firebase
    private fun loginUser() {

        showProgressBar()
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()

        //Verificamos que los campos no este vacios
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            // Iniciamos sesión con el método signIn y enviamos usuario y contraseña
            activity?.let {
                auth.signInWithEmailAndPassword(email, password)
                    //Verificamos que la tarea se ejecutó correctamente
                    .addOnCompleteListener(it) { task ->
                        if (task.isSuccessful) {
                            // Si se inició correctamente la sesión vamos a la vista Home de la aplicación
                            goHome()
                        } else {
                            // sino le avisamos el usuairo que orcurrio un problema
                            Toast.makeText(context, "Authentication failed. No existe usuario", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } else {
            Toast.makeText(context, "Enter all details", Toast.LENGTH_SHORT).show()
        }
        hideProgressBar()

    }

    private fun goHome() {
        // Navegamos a Home
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        view?.findNavController()?.navigate(action)
    }

    // Primero creamos nuestro evento login dentro de este llamamos nuestro método loginUser al dar click en el botón se iniciara sesión
    private fun login() {
        loginUser()
    }

    // Si se olvido de la contraseña lo enviaremos en la siguiente actividad nos marcara error porque necesitamos crear la actividad*/
    fun forgotPassword() {
//        startActivity(Intent(this,
//            ForgotPasswordActivity::class.java))
    }

    // Si quiere registrarse lo enviaremos en la siguiente actividad nos marcara error porque necesitamos crear la actividad
    private fun register() {
        // Navegamos a register
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        view?.findNavController()?.navigate(action)
    }

}