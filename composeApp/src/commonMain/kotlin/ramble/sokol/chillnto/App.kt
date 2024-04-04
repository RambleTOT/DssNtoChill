package org.company.app

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raspberrypiserver.RetrofitHelper
import dssntochill.composeapp.generated.resources.IndieFlower_Regular
import dssntochill.composeapp.generated.resources.LabGrotesque_Medium
import dssntochill.composeapp.generated.resources.LabGrotesque_Regular
import dssntochill.composeapp.generated.resources.Res
import dssntochill.composeapp.generated.resources.graph_test
import dssntochill.composeapp.generated.resources.ic_cyclone
import dssntochill.composeapp.generated.resources.ic_rotate_right
import dssntochill.composeapp.generated.resources.icon_loader
import dssntochill.composeapp.generated.resources.icon_main
import dssntochill.composeapp.generated.resources.image_loader
import dssntochill.composeapp.generated.resources.run
import dssntochill.composeapp.generated.resources.title_one
import dssntochill.composeapp.generated.resources.title_two
import io.github.aakira.napier.Napier.d
import io.github.aakira.napier.Napier.i
import kotlinx.coroutines.CoroutineStart
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ramble.sokol.chillnto.theme.AppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.LogManager
import java.util.logging.Logger
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private var s = mutableStateOf("На чиле")
private var centerText = mutableStateOf("Запустите программу для записи звуки и проведения анализа")
private var isAnimate = mutableStateOf(false)
private var isSuccesfull = mutableStateOf(false)
private var min = mutableStateOf("")
private var max = mutableStateOf("")
private var mean = mutableStateOf("")
private var median = mutableStateOf("")
private var std = mutableStateOf("")
private var noise = mutableStateOf("")
private var photo = mutableStateOf("")

@Composable
internal fun App() = AppTheme {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(top = 40.dp, bottom = 40.dp, start = 40.dp, end = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                stringResource(Res.string.title_one),
                style = TextStyle(
                    fontSize = 40.sp,
                    lineHeight = 16.sp,
                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF222222),
                )
            )

            Text(
                stringResource(Res.string.title_two),
                style = TextStyle(
                    fontSize = 40.sp,
                    lineHeight = 16.sp,
                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFF9A42),
                )
            )

        }

        val transition = rememberInfiniteTransition()
        val rotate by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing)
            )
        )

        if (isSuccesfull.value) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,

                ){

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(start = 20.dp, end = 40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Text(
                        text = "График амплитудного спектра",
                        style = TextStyle(
                            fontSize = 22.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF191919),
                        ),
                        textAlign = TextAlign.Center
                    )
                    if (photo.value == "") {
                        Image(
                            painter = painterResource(Res.drawable.graph_test),
                            modifier = Modifier.fillMaxWidth(),
                            contentDescription = null
                        )
                    }else{

                        val decodedBytes = java.util.Base64.getDecoder().decode(photo.value)
                        val image = org.jetbrains.skia.Image.makeFromEncoded(decodedBytes)

                        Image(
                            bitmap = image.asImageBitmap(),
                            modifier = Modifier.fillMaxWidth(),
                            contentDescription = null
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .background(color = Color(0xFFF8F8FA), shape = RoundedCornerShape(size = 15.dp))
                                .padding(vertical = 20.dp, horizontal = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Максимальное значение",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFB3B3B3),
                                ),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = max.value,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFF8A65D),
                                ),
                                textAlign = TextAlign.Center
                            )

                        }

                        Spacer(modifier = Modifier.padding(horizontal = 25.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color(0xFFF8F8FA), shape = RoundedCornerShape(size = 15.dp))
                                .padding(vertical = 20.dp, horizontal = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Минимальное значение",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFB3B3B3),
                                ),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = min.value,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFF8A65D),
                                ),
                                textAlign = TextAlign.Center
                            )

                        }

                    }

                    Spacer(modifier = Modifier.padding(top = 50.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .background(color = Color(0xFFF8F8FA), shape = RoundedCornerShape(size = 15.dp))
                                .padding(vertical = 20.dp, horizontal = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Медианное значение",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFB3B3B3),
                                ),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = median.value,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFF8A65D),
                                ),
                                textAlign = TextAlign.Center
                            )

                        }

                        Spacer(modifier = Modifier.padding(horizontal = 25.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color(0xFFF8F8FA), shape = RoundedCornerShape(size = 15.dp))
                                .padding(vertical = 20.dp, horizontal = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Среднее значение",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFB3B3B3),
                                ),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = mean.value,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFF8A65D),
                                ),
                                textAlign = TextAlign.Center
                            )

                        }

                    }

                    Spacer(modifier = Modifier.padding(top = 50.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .background(color = Color(0xFFF8F8FA), shape = RoundedCornerShape(size = 15.dp))
                                .padding(vertical = 20.dp, horizontal = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Шум в дБ",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFB3B3B3),
                                ),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = noise.value,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFF8A65D),
                                ),
                                textAlign = TextAlign.Center
                            )

                        }

                        Spacer(modifier = Modifier.padding(horizontal = 25.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color(0xFFF8F8FA), shape = RoundedCornerShape(size = 15.dp))
                                .padding(vertical = 20.dp, horizontal = 15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Стандартное квадратичное отклонение",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFB3B3B3),
                                ),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = std.value,
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xFFF8A65D),
                                ),
                                textAlign = TextAlign.Center
                            )

                        }

                    }

                }

            }

        } else {

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    if (isAnimate.value) {

                        Image(
                            painter = painterResource(Res.drawable.image_loader),
                            modifier = Modifier
                                .size(350.dp)
                                .padding(16.dp)
                                .run { if (isAnimate.value) rotate(rotate) else this },
                            contentDescription = null
                        )

                    }

                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp),
                        imageVector = vectorResource(Res.drawable.icon_main),
                        colorFilter = ColorFilter.tint(Color(0xFFF8A65D)),
                        contentDescription = null
                    )
                }

                Text(
                    text = centerText.value,
                    style = TextStyle(
                        fontSize = 40.sp,
                        lineHeight = 16.sp,
                        fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                        fontWeight = FontWeight(600),
                        color = if (!isAnimate.value) Color(0xFF222222) else Color(0xFFFFFFFF),
                    )
                )
            }
    }


        if (!isAnimate.value){
            Button(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .width(415.dp)
                    .heightIn(min = 55.dp)
                    .background(color = Color(0xFFFF9A42), shape = RoundedCornerShape(size = 12.dp)),
                onClick = {
                    isAnimate.value = !isAnimate.value
                    isSuccesfull.value = false
                    request()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9A42)),
                content = {
                    Text(
                        stringResource(Res.string.run),
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 16.sp,
                            fontFamily = FontFamily(Font(Res.font.LabGrotesque_Regular)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF),
                            )

                    )
                }
            )
        }else{
            Box(modifier = Modifier.height(80.dp))
        }

//        var isDark by LocalThemeIsDark.current
//        val icon = remember(isDark) {
//            if (isDark) Res.drawable.ic_light_mode
//            else Res.drawable.ic_dark_mode
//        }
//
//        ElevatedButton(
//            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
//            onClick = { isDark = !isDark },
//            content = {
//                Icon(vectorResource(icon), contentDescription = null)
//                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
//                Text(stringResource(Res.string.theme))
//            }
//        )

    }

    Spacer(modifier = Modifier.padding(bottom = 80.dp))

}

@OptIn(ExperimentalEncodingApi::class)
fun request(){
    RetrofitHelper().getApi().loginUser().enqueue(object :
        Callback<GetData> {
        override fun onResponse(
            call: Call<GetData>,
            response: Response<GetData>
        ) {
            if (response.isSuccessful) {
                val body = response.body()
                centerText.value = response.toString()
                isAnimate.value = !isAnimate.value
                isSuccesfull.value = !isSuccesfull.value
                min.value = body!!.min.toString()
                max.value = body.max.toString()
                mean.value = body.mean.toString()
                median.value = body.median.toString()
                std.value = body.std.toString()
                noise.value = body.noise.toString()
                photo.value = body.graph.toString()
            }else{
                //binding!!.textErrorLogin.visibility = View.VISIBLE
                centerText.value = response.toString()
                isAnimate.value = !isAnimate.value
            }
        }

        override fun onFailure(call: Call<GetData>, t: Throwable) {
            centerText.value = "Произошла ошибка ${t}"
            isAnimate.value = !isAnimate.value
        }

    })
}