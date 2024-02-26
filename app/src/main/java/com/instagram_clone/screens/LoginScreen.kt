package com.instagram_clone.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.instagram_clone.DataManager
import com.instagram_clone.R
import com.instagram_clone.repos.Resource
import com.instagram_clone.viewModels.LoginViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun LoginScreen(onSignUp: () -> Unit, onLogin: () -> Unit) {

    var loginEmail by remember {
        mutableStateOf("")
    }

    var loginPass by remember {
        mutableStateOf("")
    }

    var isPassVisible by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var isLoggedIn by remember {
        mutableStateOf(true)
    }

    var isCallable by remember {
        mutableStateOf(false)
    }

    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginFlow by loginViewModel.loginFlow.collectAsState()

    val userFLow by loginViewModel.userFlow.collectAsState()

    userFLow?.let {
        when (it) {
            is Resource.Success -> {
                DataManager.userData = it.result
            }

            Resource.Loading -> {/*LOADING*/
            }

            is Resource.Failure -> {
                it.exception.printStackTrace()
            }
        }
    }


    loginFlow?.let {
        when (it) {
            is Resource.Failure -> {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    if (it.exception.message == "noUser") {
                        isLoggedIn = false
                    } else {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    isLoading = false
                }
            }

            Resource.Loading -> isLoading = true

            is Resource.Success -> {
                LaunchedEffect(Unit) {

                    loginViewModel.getUserData(it.result.uid)

                    if (!isCallable)
                        delay((1.5).seconds)

                    onLogin()
                    isLoading = false

                }
            }

        }
    }


    Box(modifier = Modifier.animateContentSize()) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize()
        ) {

            if (!isLoggedIn && isCallable) Spacer(
                modifier = Modifier
                    .fillMaxHeight(0.3f)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_instagram),
                contentDescription = "instagram",
                colorFilter = ColorFilter.tint(color = Color.White),
            )

            LaunchedEffect(Unit) {
                delay((1.5).seconds)
                isCallable = true
            }

            if (!isLoggedIn && isCallable) LoginContent(
                loginEmail = loginEmail,
                onEmailValueChange = { loginEmail = it },
                loginPass = loginPass,
                onPassValueChange = { loginPass = it },
                isPassVisible = isPassVisible,
                onPassVisible = { isPassVisible = !isPassVisible },
                loginViewModel = loginViewModel,
                isLoading = isLoading
            ) {
                onSignUp()
            }
        }
    }
}

@Composable
fun LoginContent(
    loginEmail: String,
    onEmailValueChange: (it: String) -> Unit,
    loginPass: String,
    onPassValueChange: (it: String) -> Unit,
    isPassVisible: Boolean,
    onPassVisible: () -> Unit,
    loginViewModel: LoginViewModel,
    isLoading: Boolean,
    onSignUp: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {

        Box(modifier = Modifier.padding(horizontal = 50.dp)) {

            Column {

                Spacer(modifier = Modifier.height(50.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(1f),
                    value = loginEmail,
                    onValueChange = { onEmailValueChange(it) },
                    shape = RoundedCornerShape(size = 5.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        cursorColor = Color.White,
                    ),
                    placeholder = { Text(text = "Email", color = Color.Gray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)

                )

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(1f),
                    value = loginPass,
                    onValueChange = { onPassValueChange(it) },
                    shape = RoundedCornerShape(size = 5.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        cursorColor = Color.White
                    ),
                    placeholder = { Text(text = "Password", color = Color.Gray) },
                    visualTransformation = if (isPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (isPassVisible) R.drawable.visibility
                        else R.drawable.visibility_off

                        val description = if (isPassVisible) "Hide Password" else "Show Password"

                        IconButton(onClick = { onPassVisible() }) {
                            Icon(painterResource(id = image), contentDescription = description)
                        }
                    }

                )

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth(1f)) {
                    TextButton(onClick = {}) {
                        Text(text = "Forgot Password")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedButton(
                    onClick = {
                        if (DataManager.isValidEmail(loginEmail) && loginPass.isNotEmpty())
                            loginViewModel.loginUser(loginEmail, loginPass)
                    },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .animateContentSize(),
                    colors = ButtonDefaults.buttonColors(),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    if (isLoading) CircularProgressIndicator(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(30.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    ) else Text(
                        text = "Login",
                        modifier = Modifier.padding(5.dp),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Box(contentAlignment = Alignment.BottomCenter) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Divider(
                    color = Color.DarkGray, modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth(1f)
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(vertical = 15.dp)
                ) {
                    Text(text = "Don't have an account? ", color = Color.White)
                    ClickableText(
                        text = AnnotatedString("Sign Up"),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        onClick = { onSignUp() })
                }
            }
        }
    }

}