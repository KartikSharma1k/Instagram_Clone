package com.instagram_clone.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.instagram_clone.DataManager
import com.instagram_clone.R
import com.instagram_clone.models.UserData
import com.instagram_clone.repos.Resource
import com.instagram_clone.viewModels.SignUpViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun SignUpScreen(onLogin: () -> Unit, onSignUp: () -> Unit) {

    var signUpEmail by remember {
        mutableStateOf("")
    }

    var signUpPass by remember {
        mutableStateOf("")
    }

    var signUpCnfPass by remember {
        mutableStateOf("")
    }

    var signUpFullname by remember {
        mutableStateOf("")
    }

    var signUpUsername by remember {
        mutableStateOf("")
    }

    var isPassVisible by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var isError by remember {
        mutableStateOf(false)
    }

    val signUpViewMode: SignUpViewModel = hiltViewModel()
    val signUpFlow by signUpViewMode.signUpFlow.collectAsState()
    val usernameFlow by signUpViewMode.usernameFlow.collectAsState()
    val addUserFlow by signUpViewMode.addUserFlow.collectAsState()

    usernameFlow?.let {
        when (it) {
            is Resource.Failure -> {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT)
                        .show()
                    isLoading = false
                }
            }

            is Resource.Loading -> isLoading = true

            is Resource.Success -> {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    if (it.result) {
//                        Toast.makeText(context, "valid UserName", Toast.LENGTH_SHORT).show()
                        signUpViewMode.createUser(signUpEmail, signUpPass)
                    } else {
                        Toast.makeText(context, "Username already taken", Toast.LENGTH_SHORT)
                            .show()
                    }
                    isLoading = false
                }
            }
        }
    }

    signUpFlow?.let {
        when (it) {
            is Resource.Failure -> {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT)
                        .show()
                    isLoading = false
                }
            }

            is Resource.Loading -> isLoading = true

            is Resource.Success -> {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
//                    Toast.makeText(context, it.result.email, Toast.LENGTH_SHORT).show()
                    signUpViewMode.addUser(
                        UserData(
                            signUpFullname,
                            signUpUsername,
                            it.result.uid,
                            signUpEmail
                        )
                    )
                    isLoading = false
                }
            }
        }
    }

    addUserFlow?.let {
        when (it) {
            is Resource.Failure -> {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            }

            is Resource.Loading -> isLoading = true

            is Resource.Success -> {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
//                    Toast.makeText(context, "User Added", Toast.LENGTH_SHORT).show()
                    onSignUp()
                    isLoading = false
                }

            }
        }
    }

    Box(contentAlignment = Alignment.Center) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_instagram),
                contentDescription = "instagram",
                colorFilter = ColorFilter.tint(color = Color.White)
            )

            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(1f),
                value = signUpEmail,
                onValueChange = { signUpEmail = it },
                shape = RoundedCornerShape(size = 5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    cursorColor = Color.White,
                ),
                placeholder = { Text(text = "Email", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(1f),
                value = signUpFullname,
                onValueChange = { signUpFullname = it },
                shape = RoundedCornerShape(size = 5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    cursorColor = Color.White,
                ),
                placeholder = { Text(text = "FullName", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(1f),
                value = signUpUsername,
                isError = isError,
                onValueChange = {
                    if (DataManager.isSpecialCharacter(it)) isError = true
                    else isError = false
                    signUpUsername = it.replace(" ", "_")
                },
                shape = RoundedCornerShape(size = 5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isError) Color.Red else Color.White,
                    cursorColor = if (isError) Color.Red else Color.White,
                ),
                placeholder = { Text(text = "Username", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                supportingText = { Text(text = "Don't use any special character") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(1f),
                value = signUpPass,
                onValueChange = { signUpPass = it },
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

                    IconButton(onClick = { isPassVisible = !isPassVisible }) {
                        Icon(painterResource(id = image), contentDescription = description)
                    }
                },
                supportingText = { Text(text = "Atleast 6 characters long!") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(1f),
                value = signUpCnfPass,
                onValueChange = { signUpCnfPass = it },
                shape = RoundedCornerShape(size = 5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    cursorColor = Color.White
                ),
                placeholder = { Text(text = "Confirm Password", color = Color.Gray) },
                visualTransformation = if (isPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (isPassVisible) R.drawable.visibility
                    else R.drawable.visibility_off

                    val description = if (isPassVisible) "Hide Password" else "Show Password"

                    IconButton(onClick = { isPassVisible = !isPassVisible }) {
                        Icon(painterResource(id = image), contentDescription = description)
                    }
                },
                singleLine = true

            )

            Spacer(modifier = Modifier.height(20.dp))

            ElevatedButton(
                onClick = {
                    if (DataManager.isValidEmail(signUpEmail) &&
                        signUpPass.isNotEmpty() &&
                        signUpCnfPass.isNotEmpty() &&
                        signUpPass == signUpCnfPass &&
                        signUpFullname.isNotEmpty() &&
                        signUpUsername.isNotEmpty() &&
                        signUpPass.length >= 6
                    ) {
                        signUpViewMode.isUsernameValid(signUpUsername)
                    }
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
                    text = "Sign Up",
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
                Text(text = "Already have an Account? ", color = Color.White)
                ClickableText(
                    text = AnnotatedString("Login"),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    onClick = { onLogin() })
            }
        }
    }

}