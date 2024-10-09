package com.example.class_7.fragment

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.clase7.SecondActivity
import com.example.class_7.MainActivity
import com.example.class_7.R
import com.example.class_7.databinding.FragmentHomeBinding

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val idChannel = "idChannel"
    private val nameChannel = "nameChannel"
    private val notificationId = 0

    // requestPermissionLauncher: Solicita permiso para abrir la cámara.
    // Si se concede, se abre la cámara; si no, se muestra un mensaje indicando que se necesitan los permisos.
    private  val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
            if (isGranted) {
                //Si el permiso es aceptado, la cámara se abre
                openCamera()
            } else {
                // Muestra un mensaje cuando el permiso es denegado
                Toast.makeText(context, "Necesitas aceptar los Permisos", Toast.LENGTH_SHORT).show()
            }
        }

    // onCreateView: Infla el layout de FragmentHomeBinding y establece el propietario del ciclo de vida
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    // onViewCreated: Se llama después de que la vista ha sido creada.
    // Aquí se inicializan los métodos para navegación, permisos y notificaciones.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToRecycler()
        cameraPermissions()
        launchNotification()
    }

    // navigateToRecycler: Navega al fragmento RecyclerFragment cuando se presiona un botón.
    private fun navigateToRecycler() {
        binding.buttonFragmentRecycler.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_recycler_Fragment)
        }
    }

    // cameraPermissions: Configura el botón para solicitar permisos de cámara.
    private fun cameraPermissions() {
        binding.buttonCamera.setOnClickListener{
            cameraPermitRequest()
        }
    }

    // openCamera: Abre la cámara para capturar una imagen.
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 0)
    }

    // cameraPermitRequest: Solicita el permiso de la cámara. Si el permiso ya está concedido, se abre la cámara.
    // Si no, se muestra un diálogo explicando la necesidad del permiso o se solicita por primera vez.
    private fun cameraPermitRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Verifica si la versiòn del dispositivo es mayor o igual a Marshmallow (API 23)
            when {
                // Cuando ya se ha aceptado el permiso
                ContextCompat.checkSelfPermission(
                    requireContext(), android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    openCamera()
                }
                // Cuando se pide el permiso y se rechaza
                shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                    AlertDialog.Builder(requireContext()).setTitle("Permisos de Cámara")
                        .setMessage("Acepta los permisos").setPositiveButton("Sí") {_, _ ->
                            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }.setNegativeButton("No") {_,_ ->}.show()
                }
                else -> {
                    // Cuando se entra a la camara por primera vez
                    requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }
        } else {
            // Si la version es menor a la 23, se abre la camara sin necesidad de permisos
            openCamera()
        }
    }

    // launchNotification: Configura el botón de notificaciones para que, al presionarlo, cree y muestre una notificación.
    private fun launchNotification() {
        binding.buttonNotifications.setOnClickListener {
            createNotification()
        }
    }

    // createNotificationChannel: Crea un canal de notificación para Android Oreo (API 26) o superior.
    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Establece la importancia de la notificacion
            val importance = NotificationManager.IMPORTANCE_HIGH
            // CRea un objeto NotificationChannel, utilizando id, nombre e importancia
            val channel = NotificationChannel(idChannel, nameChannel, importance)
            val notificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // createNotification: Genera y muestra una notificación.
    // Solicita el permiso POST_NOTIFICATIONS en Android 13 (API 33) o superior si no ha sido concedido.
    @SuppressLint("MissingPermission")
    private fun createNotification() {
        // Solicitar permiso para mostrar notificaciones en Android 13 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verifica si el permiso ya ha sido concedido anteriormente
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
                // Pide permiso si no se tiene aùn
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
                return // Sale sin mostrar la notificación, pues no se dió permiso
            }
        }

        // Crear un canal de notificación, solo necesario para Android Oreo y superiores
        createNotificationChannel()

        (requireActivity() as MainActivity).apply {
            // Si quiero que al dar clic sobre la notificación me lleve a otra actividad:
            val intent = Intent(this, SecondActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Construir la notificación
            val notification = NotificationCompat.Builder(requireContext(), idChannel)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Título Notificación")
                .setContentText("Esto es una notificación")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true) // Esto hace que la notificación se cierre cuando el usuario haga clic en ella

            // Mostrar la notificación
            NotificationManagerCompat.from(requireContext())
                .notify(notificationId, notification.build())

            // Registro en el log para verificar si se muestra la notificación
            Log.d("Notification", "Notificación enviada con ID: $notificationId")
        }
    }
}