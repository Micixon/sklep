package pl.dalcop.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.json.JSONObject
import org.jsoup.*
import org.jsoup.select.Elements
import pl.dalcop.playground.ui.theme.PlaygroundTheme
import kotlin.coroutines.coroutineContext


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                    .fillMaxHeight()
                    .fillMaxWidth()
                    ,color = MaterialTheme.colorScheme.background
                )
                {


                    var text: String? by remember { mutableStateOf(null) }
                    var r: String? by remember { mutableStateOf(null) }
                    var clicked by remember{mutableStateOf(false)}
                    var checked by remember { mutableStateOf(false) }
                    var show by remember { mutableStateOf(true)}
                    val checker:Boolean =false
                    var checking:Boolean= false
                    var listshop:String? by remember { mutableStateOf(null) }
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxSize()

                    ) {
                        var show by remember {
                            mutableStateOf(true)
                        }
                        var tabIndex by remember { mutableStateOf(0) }
                        val tabs = listOf("Sklep", "Koszyk")

                        TabRow(selectedTabIndex = tabIndex) {
                            tabs.forEachIndexed { index, title ->
                                Tab(text = { Text(title) },
                                    selected = tabIndex == index,
                                    onClick = { tabIndex = index }
                                )
                            }
                        }
                        when (tabIndex) {
                            0 -> Shop(text = text, r = r, clicked = clicked,show=show )
                            1 -> ShoppingCart(text=text,r = r,clicked=clicked,show=show)
                        }

                    }
                }
                }
            }
        }
    }


@Composable
fun ShoppingCart(text: String?,r: String?,clicked: Boolean,show: Boolean){
    val coroutineScope = rememberCoroutineScope()
    var text: String? by remember { mutableStateOf(null) }
    var r: String? by remember { mutableStateOf(null) }
    var clicked by remember{mutableStateOf(false)}
    var checked by remember { mutableStateOf(false) }
    var show by remember { mutableStateOf(true)}
    var checker by remember {
        mutableStateOf(false)
    }
    var checking:Boolean= false
    var listshop:String? by remember { mutableStateOf(null) }
    coroutineScope.launch {
        withContext(Dispatchers.IO) {
            text = get(url = "https://playground.dudu.ovh/purchase")
        }
    }
    if (text != null) {
        val doc = Jsoup.parse(text)
        val items: Elements = doc.select("li")
        val divs : Elements = items.select("div").remove()
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
            val description = item.text().replace(oldValue = "Usuń", newValue = "",ignoreCase = false)
            if (description.startsWith("")) {
                val desiredText = description
                var isvisible by remember {
                    mutableStateOf(true)
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    displayTextBasedOnCondition(desiredText = desiredText)
                    if(desiredText.contains(Regex("Usunięte"))) {
                       isvisible = false
                   }
                    else {
                        isvisible = true
                    }
                    AnimatedVisibility(visible = isvisible) {
                        Button(
                               onClick = {
                                    coroutineScope.launch {
                                        withContext(Dispatchers.IO) {
                                            r = deleter(
                                                url = "https://playground.dudu.ovh/delete?id=$id",
                                            ) ?: "kurwa"
                                        }
                                    }
                                    clicked = true
                                    checker = true
                                    isvisible= false
                                }
                            )
                            {
                                Text(text = "Usuń",)
                            }
                        }
                    FloatingActionButton(
                        modifier = Modifier
                        .padding(16.dp)
                        .size(56.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(16.dp)
                        .align(Alignment.Bottom)

                        ,onClick = { coroutineScope.launch{ withContext(Dispatchers.IO){
                            post(posturl = "https://playground.dudu.ovh/reset")
                        } } },
                    ) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = null, tint = MaterialTheme.colorScheme.surfaceTint)
                    }
                    if (clicked==true){
                    r?.let { MinimalDialog(onClose ={clicked=false}, textcontent = it) }
                }

                }
            }
        }
    }
}



@Composable
fun popup(){

}
@Composable
fun Shop(text:String?,r:String?,clicked:Boolean,show:Boolean){
    val coroutineScope = rememberCoroutineScope()
    var text: String? by remember { mutableStateOf(null) }
    var r: String? by remember { mutableStateOf(null) }
    var clicked by remember{mutableStateOf(false)}
    var checked by remember { mutableStateOf(false) }
    var show by remember { mutableStateOf(true)}
    val checker:Boolean =false
    var checking:Boolean= false
    var listshop:String? by remember { mutableStateOf(null) }

    coroutineScope.launch {
        withContext(Dispatchers.IO) {
            text = get(url = "https://playground.dudu.ovh")
        }
    }
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
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme.inversePrimary, shape = CircleShape
                            )){
                    Text(desiredText, fontSize = TextUnit(18F, TextUnitType.Sp))
                    }
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Zmniejsz ilość",
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier
                            .clickable {
                                if (counts <= 1) {
                                    counts = 1
                                } else {
                                    counts--
                                }
                            }
                            .background(MaterialTheme.colorScheme.background)

                    )
                    Text(counts.toString(), fontSize = TextUnit(18F, TextUnitType.Sp)
                    )

                    Icon(imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Zwiększ ilość",
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier
                            .clickable { counts++ }
                            .background(MaterialTheme.colorScheme.background)


                    )
                    Icon(imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
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
        r?.let { MinimalDialog(onClose = {clicked = false}, textcontent = it) }
    }
}
@Composable
fun displayTextBasedOnCondition(desiredText: String) {

    val pending = "Oczekujące"
    val accepted = "Przyjęte"
    val deleted = "Usunięte"

    val textColor = when {
        desiredText.contains(Regex(pending)) -> Color.LightGray
        desiredText.contains(Regex(accepted)) -> Color.Green
        desiredText.contains(Regex(deleted)) -> Color.Red
        else -> Color.Black
    }
    val textdecor = when {
        desiredText.contains(Regex(deleted)) -> TextDecoration.LineThrough
        else -> {
            null
        }
    }
    Text(text = desiredText, color = textColor, textDecoration = textdecor, fontStyle = FontStyle(4), fontSize = TextUnit(25F,type= TextUnitType.Sp))
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

private val client = OkHttpClient()
suspend fun get(url:String): String {
    val request: Request = Request.Builder()
        .url(url)
        .build()
    return client.newCall(request).execute().body?.string()?: "nie dziala"
}
suspend fun post(posturl:String): String {
    val request = Request.Builder()
        .url(posturl)
        .post(posturl.toRequestBody())
        .build()

    return client.newCall(request).execute().use {response:Response -> String()}
}
suspend fun deleter(url: String): String? {
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(TrafficInterceptor())
        .build()
    val request: Request = Request.Builder()
        .url(url)
        .post(url.toRequestBody())
        .build()
    val respnsbdy = request
    val response: Response = client.newCall(request).execute()
    return response.body?.string()?.let { JSONObject(it).getString("message") }

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

