package com.innerCat.multiQR.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.appcompattheme.AppCompatTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.innerCat.multiQR.R

data class Page(val title: String, val description: AnnotatedString, @DrawableRes val image: Int);

class OnboardingActivity : AppCompatActivity() {

    @ExperimentalPagerApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppCompatTheme {
                OnboardingUI({ finish() })
            }
        }
    }
}

val onboardingPages = listOf(
    Page(
        "Organise your QR Scanning",
        buildAnnotatedString { append("Use a regex string to split a scan into columns.") },
        R.drawable.ic_onboard_page1
    ),
    Page(
        "Edit scanned data on-device",
        buildAnnotatedString { append("Click on a row to view and edit its columns.") },
        R.drawable.ic_onboard_page2
    ),
    Page(
        "Share with export to .CSV",
        buildAnnotatedString {
            append("Use the ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("\$date ")
            }
            append("keyword to insert the current date and time into the name of the file.")
        },
        R.drawable.ic_onboard_page3
    )
)


@ExperimentalPagerApi
@ExperimentalAnimationApi
@Preview
@Composable
fun Preview() {
    OnboardingUI({})
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun OnboardingUI(
    onClick: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = 3)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            PageUI(page = onboardingPages[page])
        }

        AnimatedVisibility(visible = pagerState.currentPage == 2) {
            OutlinedButton(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp), onClick = onClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = colorResource(R.color.primaryColor),
                    contentColor = Color.White
                )
            ) {
                Text(text = "GET STARTED")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


//        Text(
//            text = "SKIP",
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .clickable { onClick() }
//        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        ) {
            OutlinedButton(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .padding(start = 12.dp, bottom = 12.dp, top = 0.dp, end = 0.dp),
                onClick = onClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = colorResource(R.color.transparent),
                    contentColor = colorResource(android.R.color.tab_indicator_text),
                ),
                border = null
            ) {
                Text(text = "SKIP")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(bottom = 16.dp)
            ) {
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    activeColor = colorResource(R.color.black),
                )
            }
        }


    }
}

@Composable
fun PageUI(page: Page) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = page.title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.lato_bold_ttf)),
                textAlign = TextAlign.Start,
                modifier = Modifier.width(220.dp)
            )
        }
        Spacer(modifier = Modifier.height(60.dp))

        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = page.description,
            textAlign = TextAlign.Center, fontSize = 14.sp,
            modifier = Modifier.width(300.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

    }
}
