package pl.dalcop.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import pl.dalcop.playground.ui.theme.PlaygroundTheme
class ShoppingCart : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {

                    var r: String by remember { mutableStateOf("") }
                    var clicked by remember{mutableStateOf(false)}
                    var text: String? by remember { mutableStateOf(null) }
                    val coroutineScope = rememberCoroutineScope()
                    var checked by remember { mutableStateOf(false) }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxSize()

                    ) {
                        if (text != null) {
                            val doc = Jsoup.parse(text)
                            val items: Elements = doc.select("li")

                            var count by remember {
                                mutableStateOf(1)
                            }

                            val ids = mutableListOf<Int>()
                            for (item in items) {
                                val currentItemCount = items.indexOf(item) + 1
                                val id = currentItemCount

                                ids.add(id)
                                var counted: Int = 1
                                while (counted < ids.size) {
                                    if (true) {
                                        ids[counted] = count
                                        break
                                    }
                                    counted++
                                }
                                var counts by remember {
                                    mutableStateOf(1)
                                }
                                val description = item.text()
                                if (description.startsWith("Przedmiot ")) {
                                    val desiredText = description.substring(0, 37)
                                    Row {
                                        Icon(imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.surfaceTint,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.background)
                                                .clickable {
                                                    coroutineScope.launch {
                                                        withContext(Dispatchers.IO) {
                                                            r = interceptor(
                                                                url = "https://playground.dudu.ovh/purchase",
                                                                IdValue = id.toString(),
                                                                AmountValue = counts.toString()
                                                            ) ?: "kurwa"
                                                        }
                                                    }
                                                    clicked = true
                                                }
                                        )

                                    }
                                }
                            }
                        }
                        if(clicked==true){
                            MinimalDialog(onClose = {clicked = false}, textcontent = r)

                        }
                        Row(modifier = Modifier.wrapContentSize(Alignment.Center)) {
                            Button(onClick = {
                                coroutineScope.launch {
                                    withContext(Dispatchers.IO) {
                                        text = get(url = "https://playground.dudu.ovh/purchase")
                                    }
                                }
                            }
                            )
                            {
                                Text("Zacznij")
                            }
                        }

                    }
                }
            }
        }
    }
}

