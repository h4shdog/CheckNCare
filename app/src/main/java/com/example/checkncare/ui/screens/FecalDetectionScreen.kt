package com.example.checkncare.ui.screens

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.checkncare.ui.language.AppStrings
import com.example.checkncare.ui.language.LocalLanguage
import com.example.checkncare.ui.language.diseaseInfoMap
import com.example.checkncare.ui.theme.*
import com.example.checkncare.ui.viewmodels.FecalViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FecalDetectionScreen(
    navController: NavController,
    viewModel    : FecalViewModel = viewModel()
) {
    val state   by viewModel.state.collectAsState()
    val context = LocalContext.current
    val lang    = LocalLanguage.current
    val strings = AppStrings(lang.isEnglish)

    LaunchedEffect(Unit) { viewModel.resetState() }

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    fun createCameraUri(): Uri? = try {
        val file = File(File(context.cacheDir, "images").also { it.mkdirs() },
            "camera_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    } catch (e: Exception) { e.printStackTrace(); null }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) cameraImageUri?.let { uri ->
            uriToBitmap(context, uri)?.let { viewModel.onImageSelected(it.copy(Bitmap.Config.ARGB_8888, true)) }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            createCameraUri()?.let { uri -> cameraImageUri = uri; cameraLauncher.launch(uri) }
                ?: Toast.makeText(context, strings.permCameraFileError, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, strings.permCameraRequired, Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { uriToBitmap(context, it)?.let { b -> viewModel.onImageSelected(b.copy(Bitmap.Config.ARGB_8888, true)) } }
    }

    // Permission required to read gallery images:
    //   Android 13+ (API 33+) → READ_MEDIA_IMAGES
    //   Android 12 and below  → READ_EXTERNAL_STORAGE
    val galleryPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val galleryPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, strings.permGalleryRequired, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.fecalScreenTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor             = CrimsonRed,
                    titleContentColor          = PureWhite,
                    navigationIconContentColor = PureWhite
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Image preview ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = if (state.selectedImage != null) 0.dp else 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (state.selectedImage != null) {
                    Image(
                        bitmap             = state.selectedImage!!.asImageBitmap(),
                        contentDescription = "Selected fecal image",
                        modifier           = Modifier.fillMaxSize(),
                        contentScale       = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null,
                                modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.outline)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(strings.fecalNoImage, style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(strings.fecalUseBelow, style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Camera / Gallery buttons ──────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick  = {
                        val granted = ContextCompat.checkSelfPermission(
                            context, galleryPermission
                        ) == PermissionChecker.PERMISSION_GRANTED
                        if (granted) galleryLauncher.launch("image/*")
                        else galleryPermissionLauncher.launch(galleryPermission)
                    },
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                    border   = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(strings.fecalGallery, fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        val granted = ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED
                        if (granted) {
                            createCameraUri()?.let { uri -> cameraImageUri = uri; cameraLauncher.launch(uri) }
                                ?: Toast.makeText(context, strings.permCameraFileError, Toast.LENGTH_SHORT).show()
                        } else permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = CrimsonRed)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(strings.fecalCamera, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Analyze button ────────────────────────────────────────
            val canAnalyze = state.selectedImage != null && !state.isAnalyzing
            Button(
                onClick  = { if (canAnalyze) viewModel.analyzeImage() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled  = canAnalyze,
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = DeepRed,
                    contentColor           = PureWhite,
                    disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
                    disabledContentColor   = PureWhite.copy(alpha = 0.60f)
                )
            ) {
                if (state.isAnalyzing) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = PureWhite, strokeWidth = 2.5.dp)
                    Spacer(Modifier.width(12.dp))
                    Text(strings.fecalAnalyzing, fontWeight = FontWeight.SemiBold, color = PureWhite)
                } else {
                    Text(strings.fecalAnalyze, fontWeight = FontWeight.SemiBold,
                        color = if (canAnalyze) PureWhite else PureWhite.copy(alpha = 0.60f))
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Result ────────────────────────────────────────────────
            AnimatedVisibility(visible = state.result != null, enter = fadeIn() + expandVertically()) {
                state.result?.let { result ->
                    val diseaseInfo = diseaseInfoMap[result.label]
                    PredictionResultCard(
                        label            = result.label,
                        recommendationEn = result.recommendationEn,
                        recommendationTl = result.recommendationTl,
                        isEnglish        = lang.isEnglish,
                        strings          = strings,
                        descriptionEn    = diseaseInfo?.en?.description    ?: "",
                        descriptionTl    = diseaseInfo?.tl?.description    ?: "",
                        signsEn          = diseaseInfo?.en?.clinicalSigns  ?: emptyList(),
                        signsTl          = diseaseInfo?.tl?.clinicalSigns  ?: emptyList(),
                        detailedRecsEn   = diseaseInfo?.en?.recommendations ?: emptyList(),
                        detailedRecsTl   = diseaseInfo?.tl?.recommendations ?: emptyList(),
                        preventionEn     = diseaseInfo?.en?.prevention     ?: emptyList(),
                        preventionTl     = diseaseInfo?.tl?.prevention     ?: emptyList(),
                        treatmentEn      = diseaseInfo?.en?.treatment      ?: emptyList(),
                        treatmentTl      = diseaseInfo?.tl?.treatment      ?: emptyList()
                    )
                }
            }
        }
    }
}

private fun uriToBitmap(context: android.content.Context, uri: Uri): Bitmap? = try {
    if (Build.VERSION.SDK_INT < 28) BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
    else ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
} catch (e: Exception) { e.printStackTrace(); null }
