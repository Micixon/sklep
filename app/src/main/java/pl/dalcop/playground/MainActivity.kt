package pl.dalcop.playground

import android.graphics.Color
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.util.Log
import android.widget.Switch
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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.window.Dialog
import com.fasterxml.jackson.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.jsoup.*
import org.jsoup.select.Elements
import pl.dalcop.playground.ui.theme.PlaygroundTheme


@OptIn(ExperimentalMaterial3Api::class)
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
                    var clicked by remember{mutableStateOf(false)}
                    var text: String? by remember { mutableStateOf(null) }
                    val coroutineScope = rememberCoroutineScope()
                    var checked by remember { mutableStateOf(false) }
                    var show: Switch
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
                                var currentItemCount = items.indexOf(item) + 1
                                var id = currentItemCount

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
                                        Text(desiredText)
                                        Icon(imageVector = Icons.Default.KeyboardArrowLeft,
                                            contentDescription = "Zmniejsz ilość",
                                            modifier = Modifier.clickable {
                                                if (counts <= 1) {
                                                    counts = 1
                                                } else {
                                                    counts--
                                                }
                                            }
                                        )
                                        Text(counts.toString())

                                        Icon(imageVector = Icons.Default.KeyboardArrowRight,
                                            contentDescription = "Zwiększ ilość",
                                            modifier = Modifier.clickable { counts++ }
                                        )
                                        Icon(imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.surfaceTint,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.background)
                                                .clickable {
                                                    coroutineScope.launch {
                                                        withContext(Dispatchers.IO) {
                                                            post(
                                                                counting = counts,
                                                                identify = id.toString(),
                                                                posturl = "https://playground.dudu.ovh/purchase"
                                                            )
                                                        }
                                                    }
                                                    clicked = true
                                                    Log.d("huj", "$clicked")
                                                }
                                        )
                                        if (clicked) {
                                            AlertDialog(
                                                onClose = {
                                                    clicked = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                        }
                        Row(modifier = Modifier.wrapContentSize(Alignment.Center)) {
                            Button(onClick = {
                                    coroutineScope.launch {
                                        withContext(Dispatchers.IO) {
                                            text = get(url = "https://playground.dudu.ovh")
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

@Composable
fun AlertDialog(onClose: () -> Unit) {

    Dialog(
        onDismissRequest = { /*onClose()*/ }
    ) {
        AlertDialog(
            onDismissRequest = { /*onClose()*/ },
            title = { Text("Przeprowadzono transakcje pomyślnie") },
            text = { Text("Możesz sprawdzić koszyk") },
            confirmButton = {
                TextButton(
                    onClick = { onClose() }
                ) {
                    Text("Dobrze")
                }
            }
        )
    }
}
@Composable
fun Switch(checked:Boolean) {

    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it

        }
    )
}
private val client = OkHttpClient()
suspend fun get(url:String): String {
    val request: Request = Request.Builder()
        .url(url)
        .build()
    return client.newCall(request).execute().body?.string()?: "nie dziala"
}
suspend fun post(counting:Int, identify:String, posturl:String): String {
    val formData = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("id","$identify")
        .addFormDataPart("amount","${counting.toString()}")
        .build()
    val request = Request.Builder()
        .url(posturl)
        .post(formData)
        .build()

    return client.newCall(request).execute().use {response:Response -> String()}
}
suspend fun interceptor(url: String,Id:String,IdValue:String,IdAmount:String,AmountValue:String) {
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TrafficInterceptor())
        .build()

    val formData = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(Id,IdValue)
        .addFormDataPart(IdAmount,AmountValue)
        .build()
    val request: Request = Request.Builder()
        .url(url)
        .post(formData)
        .build()
    try {
        val response: Response = client.newCall(request).execute()

        println("Response Code: ${ when (response.code) {
            200 -> { "Handle Success" }
            400 -> { "Show Bad Request Error Message" }
            401 -> { "Show Unauthorized Error Message" }
            403 -> { "Show Forbidden Message" }
            404 -> { "Show Not Found Message" }
            500 -> { "Show Internal Server Error Message" }
            502 -> { "Show Bad Gateway Error Message" }
            503 -> { "Show Service Unavailable Message" }
            else -> { "Handle Other Response Code: ${response.code}" }
        }}")
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
class TrafficInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val responseBody: ResponseBody? = response.body
        val responseBodyString: String = responseBody?.string() ?: ""
        println("Response Body: $responseBodyString")
        return response.newBuilder()
            .body(responseBodyString.toResponseBody(responseBody?.contentType()))
            .build()
    }
}
