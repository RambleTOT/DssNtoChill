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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.raspberrypiserver.RetrofitHelper
import dssntochill.composeapp.generated.resources.IndieFlower_Regular
import dssntochill.composeapp.generated.resources.LabGrotesque_Medium
import dssntochill.composeapp.generated.resources.LabGrotesque_Regular
import dssntochill.composeapp.generated.resources.Res
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

private var s = mutableStateOf("На чиле")
private var centerText = mutableStateOf("Запустите программу для записи звуки и проведения анализа")
private var isAnimate = mutableStateOf(false)

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
        ){

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

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){

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


        if (!isAnimate.value){
            Button(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .width(415.dp)
                    .heightIn(min = 55.dp)
                    .background(color = Color(0xFFFF9A42), shape = RoundedCornerShape(size = 12.dp)),
                onClick = {
                    isAnimate.value = !isAnimate.value
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

fun request(){
    RetrofitHelper().getApi().loginUser().enqueue(object :
        Callback<GetData> {
        override fun onResponse(
            call: Call<GetData>,
            response: Response<GetData>
        ) {
            if (response.isSuccessful) {
//                    tokenManager.saveToken(response.body()!!.token.toString())
//                    firstEntryManager.saveFirstEntry(true)
//                    val transaction = activity!!.supportFragmentManager.beginTransaction()
//                    transaction.replace(R.id.linear_fragment, BottomNavBarFragment())
//                    transaction.disallowAddToBackStack()
//                    transaction.commit()
                centerText.value = response.toString()
                isAnimate.value = !isAnimate.value
            }else{
                //binding!!.textErrorLogin.visibility = View.VISIBLE
                centerText.value = "No"
                isAnimate.value = !isAnimate.value
            }
        }

        override fun onFailure(call: Call<GetData>, t: Throwable) {
            centerText.value = "Произошла ошибка ${t}"
            isAnimate.value = !isAnimate.value
        }

    })
}