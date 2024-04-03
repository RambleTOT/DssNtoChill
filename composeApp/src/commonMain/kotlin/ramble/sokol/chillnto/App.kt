package org.company.app

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.raspberrypiserver.RetrofitHelper
import dssntochill.composeapp.generated.resources.IndieFlower_Regular
import dssntochill.composeapp.generated.resources.Res
import dssntochill.composeapp.generated.resources.ic_cyclone
import dssntochill.composeapp.generated.resources.ic_rotate_right
import dssntochill.composeapp.generated.resources.run
import dssntochill.composeapp.generated.resources.stop
import io.github.aakira.napier.Napier.d
import io.github.aakira.napier.Napier.i
import org.jetbrains.compose.resources.Font
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = s.value,
            fontFamily = FontFamily(Font(Res.font.IndieFlower_Regular)),
            style = MaterialTheme.typography.displayLarge
        )

        val transition = rememberInfiniteTransition()
        val rotate by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing)
            )
        )

        if (!isAnimate.value){
            Text(
                text = centerText.value,
                fontFamily = FontFamily(Font(Res.font.IndieFlower_Regular)),
                style = MaterialTheme.typography.displayLarge
            )
        }else{
            Image(
                modifier = Modifier
                    .size(250.dp)
                    .padding(16.dp)
                    .run { if (isAnimate.value) rotate(rotate) else this },
                imageVector = vectorResource(Res.drawable.ic_cyclone),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                contentDescription = null
            )
        }


        if (!isAnimate.value){
            ElevatedButton(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .widthIn(min = 350.dp)
                    .heightIn(min = 70.dp),
                onClick = {
                    isAnimate.value = !isAnimate.value
                    request()
                },
                content = {
                    Icon(vectorResource(Res.drawable.ic_rotate_right), contentDescription = null)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        stringResource(if (isAnimate.value) Res.string.stop else Res.string.run)
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