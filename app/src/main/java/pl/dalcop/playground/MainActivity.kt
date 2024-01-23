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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import pl.dalcop.playground.ui.theme.PlaygroundTheme
import java.io.IOException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {    var text: String? by remember { mutableStateOf(null) }
                    Column(Modifier.verticalScroll(rememberScrollState())) {

                        var coroutineScope = rememberCoroutineScope()
                        if (text !=null) {
                            var connect = Jsoup.connect("https://playground.dudu.ovh").get()
                            var doc = Jsoup.parse(connect.toString())
                            var items: Elements = connect.select("li")
                            var count by remember {
                                mutableStateOf(1)
                            }
                            val identified:String ="1"
                            val url = "https://playground.dudu.ovh"
                            for (item in items) {
                                val sourceCode = coroutineScope.launch{withContext(Dispatchers.IO){parseSourceCode(url)} }
                                val req = coroutineScope.launch {  withContext(Dispatchers.IO) { request() } }
                                val description = sourceCode.toString()
                                    Row {
                                        Text(description)
                                        Icon(imageVector = Icons.Default.KeyboardArrowLeft,
                                            contentDescription = "Zmniejsz ilość",
                                            modifier = Modifier.clickable { if (count<=0){
                                            count = 0
                                            }
                                            else {
                                                    count--
                                            }
                                            }
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
                        }
            var text: String? by remember { mutableStateOf("") }
            val coroutineScope = rememberCoroutineScope()
//            Row {
//                Button(onClick = {
//                    coroutineScope.launch {
//                        withContext(Dispatchers.IO) {
//                            text = request()
////                            print("dupa")
//                        }
//                    }
//                }
//                ) {
//                    Text("Zacznij")
//                }
//            }
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
//            }
//        }
//    }
//}



suspend fun request(): String {
    val client = OkHttpClient()

    val request: Request = Request.Builder()
        .url("https://playground.dudu.ovh")
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
suspend fun parseSourceCode(url: String): String {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()

    val response = client.newCall(request).execute()
    val htmlContent = response.body?.string() ?: "Failed to fetch HTML"
    val doc = Jsoup.parse(htmlContent)
    doc.head()
    val sourceCode = doc.select("body").text()

    return sourceCode
}
