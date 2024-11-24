package com.example.contactapp.Screens

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivities

@Composable
fun CallDialog(phoneNumber: String) {
    MakePhoneCallButton(phoneNumber = phoneNumber)
}

@Composable
fun MakePhoneCallButton(phoneNumber: String) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_CALL)
    intent.setData(Uri.parse("tel: $phoneNumber"))
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        startActivities(context, arrayOf( intent))
//        startActivity(context, intent, bundleOf())
    } else {
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), 777
        )
    }
}