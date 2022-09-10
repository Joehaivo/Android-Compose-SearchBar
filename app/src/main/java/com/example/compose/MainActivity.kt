package com.example.compose

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.ui.theme.ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Column {
                        SearchBar(showSearchButton = true) {
                            Toast.makeText(
                                this@MainActivity,
                                "searchButtonClicked, text=$it",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    placeText: String = "输入文字开始搜索",
    showSearchButton: Boolean = false,
    onSearchButtonClick: (text: String) -> Unit
) {
    BasicSearchBar(
        placeHolderText = {
            Text(
                text = placeText,
                style = TextStyle(color = Color(0, 0, 0, 128))
            )
        },
        showSearchButton = showSearchButton,
        onSearchButtonClick = onSearchButtonClick
    )
}

@Composable
fun BasicSearchBar(
    modifier: Modifier = Modifier,
    defaultText: String = "",
    placeHolderText: @Composable () -> Unit,
    showSearchButton: Boolean = true,
    onSearchButtonClick: (text: String) -> Unit
) {
    var textValue by remember { mutableStateOf(defaultText) }
    val rotateClearIconDegree by animateFloatAsState(
        targetValue = if (textValue.isNotEmpty()) 90f else 0f,
        animationSpec = tween(durationMillis = 400)
    )
    Box(
        modifier = modifier
            .padding(horizontal = 5.dp)
            .height(50.dp)
            .fillMaxWidth()
            .background(color = Color(0xffd3d3d3))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = textValue,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearchButtonClick(textValue)
                }),
                onValueChange = { textValue = it },
                decorationBox = { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "search",
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 0.dp)
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (textValue.isEmpty()) {
                                placeHolderText()
                            }
                            innerTextField()
                        }

                        AnimatedVisibility(visible = textValue.isNotEmpty()) {
                            IconButton(
                                onClick = { textValue = "" },
                                modifier = Modifier.rotate(rotateClearIconDegree)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "clear"
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color.White, CircleShape)
                    .fillMaxHeight()
                    .weight(1f)
            )
            if (showSearchButton) {
                AnimatedVisibility(visible = textValue.isNotEmpty()) {
                    TextButton(
                        onClick = { onSearchButtonClick(textValue) },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Text(text = "搜索", style = TextStyle(fontSize = 11.sp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar() {
        Log.d("TAG", "SearchBarPreview: searchButtonClicked, text=$it")
    }
}