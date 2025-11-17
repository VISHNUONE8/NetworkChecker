package com.andrayudu.networkobserver

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private lateinit var controller: OfflineController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(com.andrayudu.networkobserver.R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.andrayudu.networkobserver.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val rootView = findViewById<ViewGroup>(android.R.id.content)

        controller = NetworkObserverUI.attach(
            parent = rootView,
            scope = lifecycleScope,
            mode = OfflineViewMode.FULLSCREEN,
            onRetry = {
                // <-- YOUR API reload logic here
//                myViewModel.loadHomeScreen()
                Toast.makeText(this, "Internet is back", Toast.LENGTH_SHORT).show()
            }
        )


    }

    override fun onDestroy() {
        controller.stop()
        super.onDestroy()
    }
}


