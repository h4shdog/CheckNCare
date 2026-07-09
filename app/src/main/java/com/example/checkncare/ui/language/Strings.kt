package com.example.checkncare.ui.language

// ─────────────────────────────────────────────────────────────────────────────
// All user-facing strings for English and Tagalog.
// Access via AppStrings(isEnglish) anywhere in the UI.
// ─────────────────────────────────────────────────────────────────────────────

data class AppStrings(
    // ── Home ──────────────────────────────────────────────────────────────────
    val appTagline: String,
    val footerLabel: String,
    val featureAudioTitle: String,
    val featureAudioDesc: String,
    val featureFecalTitle: String,
    val featureFecalDesc: String,
    val featureHistoryTitle: String,
    val featureHistoryDesc: String,
    val featureAboutTitle: String,
    val featureAboutDesc: String,
    val languageToggleLabel: String,
    val fontSizeLabel: String,

    // ── Audio Detection ───────────────────────────────────────────────────────
    val audioScreenTitle: String,
    val audioInfoText: String,
    val audioRecordingLabel: String,
    val audioAnalyzingLabel: String,
    val audioTapToStop: String,
    val audioTapToRecord: String,
    val audioTooShort: String,

    // ── Fecal Detection ───────────────────────────────────────────────────────
    val fecalScreenTitle: String,
    val fecalNoImage: String,
    val fecalUseBelow: String,
    val fecalGallery: String,
    val fecalCamera: String,
    val fecalAnalyze: String,
    val fecalAnalyzing: String,

    // ── Permission messages ───────────────────────────────────────────────────
    val permMicRequired: String,
    val permCameraRequired: String,
    val permGalleryRequired: String,
    val permCameraFileError: String,

    // ── Result Card ───────────────────────────────────────────────────────────
    val resultConfidence: String,
    val resultRecommendation: String,
    val resultDescription: String,
    val resultClinicalSigns: String,
    val resultPrevention: String,
    val resultTreatment: String,

    // ── History ───────────────────────────────────────────────────────────────
    val historyScreenTitle: String,
    val historySearch: String,
    val historyEmpty: String,
    val historyRecordSuffix: String,
    val historyRecordsSuffix: String,
    val historyConfidence: String,
    val historyClearTitle: String,
    val historyClearBody: String,
    val historyClearConfirm: String,
    val historyClearCancel: String,
    // Single record delete dialog
    val historyDeleteTitle: String,
    val historyDeleteBody: String,
    val historyDeleteConfirm: String,
    val historyDeleteCancel: String,
    // Record type labels
    val historyTypeAudio: String,
    val historyTypeFecal: String,
    // Result labels
    val historyResultNormal: String,
    val historyResultSick: String,
    val historyResultCoccidiosis: String,
    val historyResultNewcastle: String,
    val historyResultSalmonella: String,
    val historyResultUnknown: String,

    // ── About ─────────────────────────────────────────────────────────────────
    val aboutScreenTitle: String,
    val aboutAiTagline: String,
    val aboutPurposeTitle: String,
    val aboutPurposeContent: String,
    val aboutTechTitle: String,
    val aboutDevTitle: String,
    val aboutDevContent: String,
    val aboutVersion: String,
)

fun AppStrings(isEnglish: Boolean): AppStrings = if (isEnglish) englishStrings else tagalogStrings

// ─────────────────────────────────────────────────────────────────────────────
// ENGLISH
// ─────────────────────────────────────────────────────────────────────────────
val englishStrings = AppStrings(
    appTagline            = "Poultry Health Monitoring & Disease Detection",
    footerLabel           = "AI-Powered Poultry Health • v1.0.0",
    featureAudioTitle     = "Audio Detection",
    featureAudioDesc      = "Analyze vocalizations",
    featureFecalTitle     = "Fecal Detection",
    featureFecalDesc      = "Analyze fecal images",
    featureHistoryTitle   = "History",
    featureHistoryDesc    = "View past predictions",
    featureAboutTitle     = "Details",
    featureAboutDesc      = "App information",
    languageToggleLabel   = "English / Tagalog",
    fontSizeLabel         = "Font Size",

    audioScreenTitle      = "Audio Detection",
    audioInfoText         = "Record chicken vocalizations to detect respiratory diseases.",
    audioRecordingLabel   = "● Recording… tap Stop when done",
    audioAnalyzingLabel   = "Analyzing audio…",
    audioTapToStop        = "Tap to stop",
    audioTapToRecord      = "Tap to record",
    audioTooShort         = "Recording too short. Please record for at least 5 seconds and try again.",

    fecalScreenTitle      = "Fecal Detection",
    fecalNoImage          = "No image selected",
    fecalUseBelow         = "Use Camera or Gallery below",
    fecalGallery          = "Gallery",
    fecalCamera           = "Camera",
    fecalAnalyze          = "Analyze Image",
    fecalAnalyzing        = "Analyzing…",

    permMicRequired       = "Microphone permission is required",
    permCameraRequired    = "Camera permission is required",
    permGalleryRequired   = "Storage permission is required to access gallery",
    permCameraFileError   = "Could not create image file",

    resultConfidence      = "Confidence",
    resultRecommendation  = "Actions to Take",
    resultDescription     = "About This Disease",
    resultClinicalSigns   = "Clinical Signs",
    resultPrevention      = "Prevention",
    resultTreatment       = "Treatment",

    historyScreenTitle    = "Prediction History",
    historySearch         = "Search records…",
    historyEmpty          = "No history records found.",
    historyRecordSuffix   = "record",
    historyRecordsSuffix  = "records",
    historyConfidence     = "Confidence",
    historyClearTitle     = "Clear All History",
    historyClearBody      = "This will permanently delete all prediction records. Are you sure?",
    historyClearConfirm   = "Clear",
    historyClearCancel    = "Cancel",
    historyDeleteTitle    = "Delete Record",
    historyDeleteBody     = "Are you sure you want to delete this record?",
    historyDeleteConfirm  = "Delete",
    historyDeleteCancel   = "Cancel",
    historyTypeAudio      = "Audio",
    historyTypeFecal      = "Fecal",
    historyResultNormal   = "Normal",
    historyResultSick     = "Sick",
    historyResultCoccidiosis = "Coccidiosis",
    historyResultNewcastle   = "Newcastle Disease",
    historyResultSalmonella  = "Salmonella Infection",
    historyResultUnknown     = "Unknown",

    aboutScreenTitle      = "Details",
    aboutAiTagline        = "AI-Powered Poultry Health Monitoring",
    aboutPurposeTitle     = "Purpose",
    aboutPurposeContent   = "CheckNCare helps poultry farmers detect common diseases early through AI-powered vocalization and fecal image analysis — improving flock health and productivity.",
    aboutTechTitle        = "Technologies",
    aboutDevTitle         = "Developers",
    aboutDevContent       = "Developed as a College Capstone Project.",
    aboutVersion          = "Version 1.0.0",
)

// ─────────────────────────────────────────────────────────────────────────────
// TAGALOG
// ─────────────────────────────────────────────────────────────────────────────
val tagalogStrings = AppStrings(
    appTagline            = "Pagmamasid sa Kalusugan ng Manok at Pagtuklas ng Sakit",
    footerLabel           = "AI-Powered na Kalusugan ng Manok • v1.0.0",
    featureAudioTitle     = "Pagsusuri ng Tilaok",
    featureAudioDesc      = "Pakinggan ang tunog",
    featureFecalTitle     = "Pagsusuri ng Dumi",
    featureFecalDesc      = "Kunan ng litrato",
    featureHistoryTitle   = "Kasaysayan",
    featureHistoryDesc    = "Tingnan ang mga dating resulta",
    featureAboutTitle     = "Mga Detalye",
    featureAboutDesc      = "Impormasyon tungkol sa app",
    languageToggleLabel   = "Wika / Language",
    fontSizeLabel         = "Laki ng Teksto",

    audioScreenTitle      = "Pagsusuri ng Tilaok",
    audioInfoText         = "I-record ang tunog ng manok upang matuklas ang mga sakit sa paghinga.",
    audioRecordingLabel   = "● Nire-record… pindutin ang Tigil kapag tapos na",
    audioAnalyzingLabel   = "Sinusuri ang audio…",
    audioTapToStop        = "Pindutin upang tigilan",
    audioTapToRecord      = "Pindutin upang i-record",
    audioTooShort         = "Masyadong maikli ang recording. Mangyaring mag-record ng hindi bababa sa 5 segundo at subukan muli.",

    fecalScreenTitle      = "Pagsusuri ng Dumi",
    fecalNoImage          = "Walang larawan na napili",
    fecalUseBelow         = "Gamitin ang Camera o Gallery sa ibaba",
    fecalGallery          = "Gallery",
    fecalCamera           = "Camera",
    fecalAnalyze          = "Suriin ang Larawan",
    fecalAnalyzing        = "Sinusuri…",

    permMicRequired       = "Kailangan ang pahintulot sa mikropono",
    permCameraRequired    = "Kailangan ang pahintulot sa camera",
    permGalleryRequired   = "Kailangan ang pahintulot sa storage para ma-access ang gallery",
    permCameraFileError   = "Hindi malikha ang image file",

    resultConfidence      = "Antas ng Katiyakan",
    resultRecommendation  = "Mga dapat Gawin",
    resultDescription     = "Tungkol sa Sakit na Ito",
    resultClinicalSigns   = "Mga Klinikal na Palatandaan",
    resultPrevention      = "Pag-iwas",
    resultTreatment       = "Paggamot",

    historyScreenTitle    = "Kasaysayan ng Pagsusuri",
    historySearch         = "Maghanap ng rekord…",
    historyEmpty          = "Walang nahanap na rekord.",
    historyRecordSuffix   = "rekord",
    historyRecordsSuffix  = "mga rekord",
    historyConfidence     = "Katiyakan",
    historyClearTitle     = "Burahin ang Lahat ng Kasaysayan",
    historyClearBody      = "Permanenteng mabubura ang lahat ng rekord. Sigurado ka ba?",
    historyClearConfirm   = "Burahin",
    historyClearCancel    = "Kanselahin",
    historyDeleteTitle    = "Burahin ang Rekord",
    historyDeleteBody     = "Sigurado ka bang gusto mong burahin ang rekord na ito?",
    historyDeleteConfirm  = "Burahin",
    historyDeleteCancel   = "Kanselahin",
    historyTypeAudio      = "Tilaok",
    historyTypeFecal      = "Dumi",
    historyResultNormal   = "Normal",
    historyResultSick     = "May Sakit",
    historyResultCoccidiosis = "Coccidiosis",
    historyResultNewcastle   = "Newcastle Disease",
    historyResultSalmonella  = "Salmonella Infection",
    historyResultUnknown     = "Hindi Natukoy",

    aboutScreenTitle      = "Mga Detalye",
    aboutAiTagline        = "AI-Powered na Pagmamasid sa Kalusugan ng Manok",
    aboutPurposeTitle     = "Layunin",
    aboutPurposeContent   = "Tinutulungan ng CheckNCare ang mga magsasakang manok na maaga na matuklas ang mga karaniwang sakit sa pamamagitan ng AI-powered na pagsusuri ng tunog at larawan ng dumi — upang mapabuti ang kalusugan at produktibidad ng kawan.",
    aboutTechTitle        = "Mga Teknolohiya",
    aboutDevTitle         = "Mga Developer",
    aboutDevContent       = "Ginawa bilang College Capstone Project.",
    aboutVersion          = "Bersyon 1.0.0",
)
