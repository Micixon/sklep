package pl.dalcop.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import pl.dalcop.playground.ui.theme.PlaygroundTheme
import java.io.IOException
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    var text: String? by remember { mutableStateOf(null) }
                    val coroutineScope = rememberCoroutineScope()
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        //text?.let { Text(it) }
                        if (text != null) {
//                            var count = remember {
//                                mutableStateOf(0)
//                            }
                            val doc = Jsoup.parse(text)
                            val title: Elements = doc.select("h1")
                            val items: Elements = doc.select("li")
                            var count by remember {
                                mutableStateOf(1)
                            }
                            val identified:String ="1"

                            for (item in items) {
                                val description = item.text()
                                if (description.startsWith("Przedmiot 1 - Cena: 29.99zł zł Ilość:")) {
                                    val desiredText = description.substring(0, 37)
                                    Row {
                                        //Text(title)
                                    }
                                    Row {
                                        Text(desiredText)
                                        Icon(imageVector = Icons.Default.KeyboardArrowLeft,
                                            contentDescription = "Zmniejsz ilość",
                                            modifier = Modifier.clickable { count-- }
                                        )
                                        Text(count.toString())

                                        Icon(imageVector = Icons.Default.KeyboardArrowRight,
                                            contentDescription = "Zwiększ ilość",
                                            modifier = Modifier.clickable { count++ }
                                        )
                                        Icon(imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White)
                                                .clickable {
                                                    coroutineScope.launch {
                                                        withContext(Dispatchers.IO) {
                                                            run(
                                                                counting = count,
                                                                identify = identified
                                                            )
                                                        }
                                                    }
                                                }
                                        )
                                    }
                                }
                            }

                        }
                        Row {
                            if (text != null) {
//                            var count = remember {
//                                mutableStateOf(0)
//                            }
                                val doc = Jsoup.parse(text)
                                val title: Elements = doc.select("h1")
                                val items: Elements = doc.select("li")
                                var count1 by remember {
                                    mutableStateOf(1)
                                }
                                val identified:String ="2"

                                for (item in items) {
                                    val description = item.text()
                                    if (description.startsWith("Przedmiot 2 - Cena: 19.99zł zł Ilość:")) {
                                        val desiredText = description.substring(0, 37)
                                        Row {
                                            //Text(title)
                                        }
                                        Row {
                                            Text(desiredText)
                                            Icon(imageVector = Icons.Default.KeyboardArrowLeft,
                                                contentDescription = "Zmniejsz ilość",
                                                modifier = Modifier.clickable { count1-- }
                                            )
                                            Text(count1.toString())

                                            Icon(imageVector = Icons.Default.KeyboardArrowRight,
                                                contentDescription = "Zwiększ ilość",
                                                modifier = Modifier.clickable { count1++ }
                                            )
                                            Icon(imageVector = Icons.Default.ShoppingCart,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color.White)
                                                    .clickable {
                                                        coroutineScope.launch {
                                                            withContext(Dispatchers.IO) {
                                                                run(
                                                                    counting = count1,
                                                                    identify = identified
                                                                )
                                                            }
                                                        }
                                                    }
                                            )
                                        }
                                    }
                                }

                            }
                        }
                        Row {
                            if (text != null) {
//                            var count = remember {
//                                mutableStateOf(0)
//                            }
                                val doc = Jsoup.parse(text)
                                val title: Elements = doc.select("h1")
                                val items: Elements = doc.select("li")
                                var count2 by remember {
                                    mutableStateOf(1)
                                }
                                val identified:String ="3"

                                for (item in items) {
                                    val description = item.text()
                                    if (description.startsWith("Przedmiot 3 - Cena: 9.99zł zł Ilość:")) {
                                        val desiredText = description.substring(0, 37)
                                        Row {
                                            //Text(title)
                                        }
                                        Row {

                                            Text(desiredText)
                                            Icon(imageVector = Icons.Default.KeyboardArrowLeft,
                                                contentDescription = "Zmniejsz ilość",
                                                modifier = Modifier.clickable { count2-- }
                                            )
                                            Text(count2.toString())

                                            Icon(imageVector = Icons.Default.KeyboardArrowRight,
                                                contentDescription = "Zwiększ ilość",
                                                modifier = Modifier
                                                    .clickable { count2++ }
                                            )

                                            Icon(imageVector = Icons.Default.ShoppingCart,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color.White)
                                                    .clickable {
                                                        coroutineScope.launch {
                                                            withContext(Dispatchers.IO) {
                                                                run(
                                                                    counting = count2,
                                                                    identify = identified
                                                                )
                                                            }
                                                        }

                                                    }
                                            )
                                        }
                                    }
                                }

                            }
                        }
                        Row {
                            Button(onClick = {
                                coroutineScope.launch {
                                    withContext(Dispatchers.IO) {
                                        text = request()
                                    }
                                }
                            }
                            ) {

                                Text("Zacznij")

                            }
                        /* TODO wyswietlanie listy zakupów */
//                            var text1 by rememberSaveable { mutableStateOf("") }
//                            val docum = Jsoup.parse(text1)
//                            var shoplist : Elements = docum.select("body")
//                            Button(onClick = {
//                                coroutineScope.launch {
//                                    withContext(Dispatchers.IO) {
//                                        text1 = requestpurchase()
//                                    }
//                                }
//                            }
//                            ) {
//                                Text("Lista Zamówień")
//                            }


                       /* TODO przyciski wysyłają requesta tylko gdy kliknięte */
//                            var counted = 420
//                            var identified2 = 1
//                            val scoping: Suspend () -> Unit = { coroutineScope.launch {
//                            withContext(Dispatchers.IO) {
//                                run(counting = counted, identify = identified2.toString())
//                            }
//                            }
//                              Button(onClick = { LaunchedEffect(true){
//                                  scoping()
//                              } })
//                                {
//                                Text("test post id=1 amount=420")
//                            }
                        }
                    }

                    }
                }
            }
        }
    }
//}

suspend fun request(): String {
    val client = OkHttpClient()

    val request: Request = Request.Builder()
        .url("https://playground.dudu.ovh")
        .build()

    return client.newCall(request).execute().body?.string()?: "nie dziala"


}
suspend fun requestpurchase(): String {
    val client = OkHttpClient()

    val request: Request = Request.Builder()
        .url("https://playground.dudu.ovh/purchase")
        .build()

    return client.newCall(request).execute().body?.string()?: "nie dziala"


}
private val client = OkHttpClient()

suspend  fun run(counting:Int,identify:String): String {
    val formData = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("id","$identify")
        .addFormDataPart("amount","${counting.toString()}")
        .build()
    val request = Request.Builder()
        .url("https://playground.dudu.ovh/purchase")
        .post(formData)
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
    }
    return client.newCall(request).execute().body?.string()?: "nie dziala"
}
//fun postRequestWithFormData() {
//    val client = OkHttpClient()
//
//
//
//    val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), formData)
//
//    val request = Request.Builder()
//        .url("https://example.com/post")
//        .post(requestBody)
//        .build()
//
//    val call = client.newCall(request)
//    val response = call.execute()
//}

