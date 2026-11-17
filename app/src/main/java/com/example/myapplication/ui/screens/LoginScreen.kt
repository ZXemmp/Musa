package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLogin: (String, String, (Boolean) -> Unit) -> Unit,
    onCreateUser: (String, String, String, (Boolean) -> Unit) -> Unit,
    onGuest: () -> Unit
) {
    var creating by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Musa", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.secondary)
            Text("Tu museo, tu recorrido", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(24.dp))
            if (creating) {
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Nombre") }, singleLine = true)
                Spacer(Modifier.height(8.dp))
            }
            OutlinedTextField(username, { username = it }, Modifier.fillMaxWidth(), label = { Text("Usuario") }, singleLine = true)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("Contraseña") }, singleLine = true)
            error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (creating) onCreateUser(name, username, password) { ok -> if (ok) error = null else error = "Completa los datos o elige otro usuario." }
                    else onLogin(username, password) { ok -> if (!ok) error = "Usuario o contraseña incorrectos." }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (creating) "Crear usuario" else "Iniciar sesión") }
            TextButton(onClick = { creating = !creating; error = null }) {
                Text(if (creating) "Ya tengo una cuenta" else "Crear nuevo usuario")
            }
            OutlinedButton(onClick = onGuest, modifier = Modifier.fillMaxWidth()) { Text("Ingresar como invitado") }
        }
    }
}
