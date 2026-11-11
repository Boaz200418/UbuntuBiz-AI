package africa.ubuntubiz.ai.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Forest = Color(0xFF103E32)
val Gold = Color(0xFFEBC477)
val Cream = Color(0xFFFAF8F1)
val Ink = Color(0xFF172D25)
val Muted = Color(0xFF5D7066)
val Pale = Color(0xFFEAF0E7)
@Composable fun UbuntuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme=lightColorScheme(primary=Forest,onPrimary=Color.White,secondary=Color(0xFF76551C),secondaryContainer=Gold,onSecondaryContainer=Forest,background=Cream,onBackground=Ink,surface=Color.White,onSurface=Ink,surfaceVariant=Pale,onSurfaceVariant=Muted,outline=Color(0xFF7B897E)),
        typography=Typography(
            headlineLarge=TextStyle(fontFamily=FontFamily.Serif,fontWeight=FontWeight.Bold,fontSize=34.sp,lineHeight=40.sp),
            headlineMedium=TextStyle(fontFamily=FontFamily.Serif,fontWeight=FontWeight.Bold,fontSize=28.sp,lineHeight=34.sp),
            titleLarge=TextStyle(fontWeight=FontWeight.SemiBold,fontSize=21.sp,lineHeight=27.sp),
            titleMedium=TextStyle(fontWeight=FontWeight.SemiBold,fontSize=17.sp,lineHeight=24.sp),
            bodyLarge=TextStyle(fontSize=16.sp,lineHeight=25.sp),
            bodyMedium=TextStyle(fontSize=14.sp,lineHeight=22.sp),
            labelLarge=TextStyle(fontWeight=FontWeight.SemiBold,fontSize=14.sp),
            labelSmall=TextStyle(fontWeight=FontWeight.Bold,fontSize=10.sp,letterSpacing=1.sp)
        ),content=content
    )
}
