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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.CheckCircle
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.json.JSONObject
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

                    val rember = rememberCompositionContext()
                    var r: String by remember { mutableStateOf("") }
                    var clicked by remember{mutableStateOf(false)}
                    var text: String? by remember { mutableStateOf(null) }
                    val coroutineScope = rememberCoroutineScope()
                    var checked by remember { mutableStateOf(false) }
                    var show by remember { mutableStateOf(true)}
                    val checker = remember { mutableStateOf(false) }
                    var checking:Boolean= false
                    var listshop:String? by remember { mutableStateOf(null) }
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
                        if(show==true){
                            Row(modifier = Modifier.wrapContentSize(Alignment.Center)) {
                                Button(onClick = {
                                    coroutineScope.launch {
                                        withContext(Dispatchers.IO) {
                                            text = get(url = "https://playground.dudu.ovh")
                                        }
                                    }
                                    show=false
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
    }

@Composable
fun MinimalDialog(onClose: () -> Unit,textcontent:String) {
    Dialog(onDismissRequest = { onClose() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                maxLines = 3,
                text = "$textcontent\n Przesuń w lewo lub w prawo by zamknąć",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Composable
fun Switcher(checker: Boolean){
    var checked by remember { mutableStateOf(true) }
    Switch(
        enabled = checker==true,
        checked = checked,
        onCheckedChange = {
            checked = it

        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        thumbContent = { Icon(imageVector = Icons.Sharp.CheckCircle, contentDescription = null) }
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
suspend fun interceptor(url: String,IdValue:String,AmountValue:String): String? {
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TrafficInterceptor())
        .build()

    val formData = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("id",IdValue)
        .addFormDataPart("amount",AmountValue)
        .build()
    val request: Request = Request.Builder()
        .url(url)
        .post(formData)
        .build()
    val response: Response = client.newCall(request).execute()
    return response.body?.string()?.let { JSONObject(it).getString("message") }

}
class TrafficInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val responseBody: ResponseBody? = response.body
        val responseBodyString: String = responseBody?.string() ?: ""
        val responsed = responseBodyString
        println("Response Body: $responseBodyString")
        return response.newBuilder()
            .body(responseBodyString.toResponseBody(responseBody?.contentType()))
            .build()
    }

}

