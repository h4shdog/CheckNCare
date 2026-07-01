package com.example.checkncare.ui.language

// ─────────────────────────────────────────────────────────────────────────────
// Structured disease information for Fecal detection results.
// ─────────────────────────────────────────────────────────────────────────────

data class DiseaseDetail(
    val name: String,
    val description: String,
    val clinicalSigns: List<String>,
    val recommendations: List<String>,
    val prevention: List<String> = emptyList(),
    val treatment: List<String> = emptyList()
)

data class DiseaseInfo(
    val en: DiseaseDetail,
    val tl: DiseaseDetail
)

val diseaseInfoMap: Map<String, DiseaseInfo> = mapOf(

    "Coccidiosis" to DiseaseInfo(
        en = DiseaseDetail(
            name = "Coccidiosis",
            description = "Coccidiosis is a parasitic disease caused by Eimeria protozoa that damages the intestinal lining of chickens, causing bloody droppings, weight loss, and reduced egg production.",
            clinicalSigns = listOf(
                "Bloody or watery droppings",
                "Lethargy and weakness",
                "Reduced feed and water intake",
                "Pale combs and wattles",
                "Ruffled feathers and huddling",
                "Poor growth in young chickens"
            ),
            recommendations = listOf(
                "Isolate sick birds immediately to prevent spread.",
                "Clean and disinfect the coop thoroughly, paying special attention to feeders and waterers.",
                "Provide clean, fresh water with electrolytes to prevent dehydration.",
                "Administer anticoccidial medications as prescribed by a veterinarian.",
                "Prevent overcrowding to reduce stress and disease transmission.",
                "Practice good biosecurity by cleaning boots and equipment between pens."
            ),
            prevention = listOf(
                "Use anticoccidial (coccidiostat) feed additives during the growing period.",
                "Maintain dry and clean litter — oocysts thrive in moist environments.",
                "Avoid overcrowding to reduce fecal contamination of feed and water.",
                "Vaccinate chicks with live oocyst vaccines before exposure.",
                "Rotate and rest pasture areas to break the parasite life cycle.",
                "Regularly clean and disinfect drinkers and feeders."
            ),
            treatment = listOf(
                "Administer anticoccidials such as Amprolium, Toltrazuril, or Diclazuril as directed by a veterinarian.",
                "Provide supportive care: electrolytes and vitamins A and K in drinking water.",
                "Remove wet litter and replace with dry, clean material.",
                "Continue treatment for the full prescribed course (usually 3–7 days).",
                "Monitor flock closely; repeat fecal oocyst counts after treatment to confirm resolution.",
                "Consult a veterinarian before administering any medication."
            )
        ),
        tl = DiseaseDetail(
            name = "Coccidiosis",
            description = "Ang Coccidiosis ay isang sakit na parasitiko na dulot ng Eimeria protozoa na sumisira sa dingding ng bituka ng mga manok, nagdudulot ng madugong dumi, pagpayat, at pagbaba ng produksyon ng itlog.",
            clinicalSigns = listOf(
                "Madugo o matubig na dumi",
                "Panghihina at pagkahilo",
                "Pagbaba ng kain at inumin",
                "Namumutla ang suklay at wattle",
                "Magulo ang balahibo at nagsisiksikan",
                "Mabagal na paglaki ng mga batang manok"
            ),
            recommendations = listOf(
                "Ihiwalay agad ang mga may sakit na manok upang hindi kumalat.",
                "Linisin at disimpektahan ang kulungan lalo na ang mga taguan ng pagkain at tubig.",
                "Magbigay ng malinis at sariwang tubig na may electrolytes upang maiwasan ang dehydration.",
                "Magbigay ng anticoccidial na gamot ayon sa reseta ng beterinaryo.",
                "Iwasan ang siksikan upang mabawasan ang stress at pagkalat ng sakit.",
                "Magsagawa ng biosecurity tulad ng paglilinis ng sapatos at kagamitan sa pagitan ng mga pen."
            ),
            prevention = listOf(
                "Gumamit ng anticoccidial (coccidiostat) na additives sa pagkain ng mga manok habang lumalaki.",
                "Panatilihing tuyo at malinis ang higaan — ang mga oocyst ay lumalaki sa mamasa-masa.",
                "Iwasan ang siksikan upang mabawasan ang polusyon ng dumi sa pagkain at tubig.",
                "Bakunahan ang mga sisiw gamit ang live oocyst vaccines bago ma-expose.",
                "Mag-rotate at magpahinga ng lugar ng pastura upang mapigilan ang ikot ng buhay ng parasito.",
                "Regular na linisin at disimpektahan ang mga inuman at taguan ng pagkain."
            ),
            treatment = listOf(
                "Magbigay ng anticoccidials tulad ng Amprolium, Toltrazuril, o Diclazuril ayon sa direksyon ng beterinaryo.",
                "Magbigay ng suportang pag-aalaga: electrolytes at bitamina A at K sa inuming tubig.",
                "Alisin ang basang higaan at palitan ng tuyo at malinis.",
                "Ituloy ang gamutan sa buong itinakdang tagal (karaniwan 3–7 araw).",
                "Bantayan nang mabuti ang kawan; ulitin ang fecal oocyst count pagkatapos ng gamutan upang kumpirmahin ang pagbabago.",
                "Kumonsulta sa beterinaryo bago magbigay ng anumang gamot."
            )
        )
    ),

    "Salmonella Infection" to DiseaseInfo(
        en = DiseaseDetail(
            name = "Salmonellosis (Pullorum Disease)",
            description = "Salmonellosis is a bacterial infection caused by Salmonella spp. that affects the digestive system of chickens, causing white diarrhea, weakness, and high mortality in young chicks.",
            clinicalSigns = listOf(
                "White, pasty diarrhea sticking to the vent area",
                "Weakness and lethargy",
                "Reduced appetite and weight loss",
                "Ruffled feathers and huddling near heat sources",
                "High mortality in chicks under 2 weeks old",
                "Decreased egg production in adult hens"
            ),
            recommendations = listOf(
                "Isolate sick birds immediately to prevent contamination.",
                "Clean the vent area of affected birds to prevent blockage.",
                "Provide clean water with electrolytes and vitamins to boost immunity.",
                "Administer antibiotics as prescribed by a veterinarian.",
                "Practice strict biosecurity and disinfect the coop thoroughly.",
                "Test and cull carrier birds to eliminate the disease from the flock."
            ),
            prevention = listOf(
                "Source chicks and hatching eggs only from Salmonella-free, accredited flocks.",
                "Disinfect hatcheries, incubators, and all equipment between batches.",
                "Maintain strict biosecurity: limit visitor access and disinfect entry points.",
                "Provide clean, uncontaminated feed and water at all times.",
                "Control rodents and wild birds that can introduce the bacteria.",
                "Conduct routine flock testing and cull positive carrier birds."
            ),
            treatment = listOf(
                "Administer antibiotics (e.g., Enrofloxacin, Amoxicillin, or Trimethoprim-Sulfa) as prescribed by a veterinarian.",
                "Provide supportive care: electrolytes and vitamins in drinking water to prevent dehydration.",
                "Gently clean the vent area of affected chicks to prevent pasting and blockage.",
                "Ensure adequate warmth for sick chicks to reduce stress.",
                "Note: treatment suppresses symptoms but may not eliminate carrier birds.",
                "Consult a veterinarian immediately — do not self-medicate."
            )
        ),
        tl = DiseaseDetail(
            name = "Salmonellosis o Pullorum Disease",
            description = "Ang Salmonellosis ay isang bacterial infection na dulot ng Salmonella spp. na nakakaapekto sa digestive system ng mga manok, nagdudulot ng puting pagtatae, panghihina, at mataas na dami ng namamatay sa mga sisiw.",
            clinicalSigns = listOf(
                "Puting at malagkit na dumi na dumidikit sa puwitan",
                "Panghihina at pagkahilo",
                "Pagbaba ng kain at pagpayat",
                "Magulo ang balahibo at nagsisiksikan sa mainit na lugar",
                "Mataas na dami ng namamatay sa mga sisiw na wala pang 2 linggo",
                "Pagbaba ng itlog sa mga adult na inahin"
            ),
            recommendations = listOf(
                "Ihiwalay agad ang mga may sakit na manok upang hindi makahawa.",
                "Linisin ang puwitan ng mga apektadong manok upang hindi bumara.",
                "Magbigay ng malinis na tubig na may electrolytes at bitamina upang palakasin ang resistensya.",
                "Magbigay ng antibiotics ayon sa reseta ng beterinaryo.",
                "Magsagawa ng mahigpit na biosecurity at linisin nang husto ang kulungan.",
                "I-test at alisin ang mga carrier na manok upang tuluyang mawala ang sakit sa kawan."
            ),
            prevention = listOf(
                "Kumuha ng mga sisiw at itlog para sa pagpapisa mula lamang sa mga kawan na walang Salmonella at may akreditasyon.",
                "Disimpektahan ang mga hatchery, incubator, at lahat ng kagamitan sa pagitan ng mga batch.",
                "Magsagawa ng mahigpit na biosecurity: limitahan ang pagpasok ng bisita at disimpektahan ang mga pintuan.",
                "Magbigay ng malinis at hindi kontaminadong pagkain at tubig sa lahat ng oras.",
                "Kontrolin ang mga daga at ligaw na ibon na maaaring magdala ng bacteria.",
                "Magsagawa ng regular na pagsusuri sa kawan at alisin ang mga positibong carrier na manok."
            ),
            treatment = listOf(
                "Magbigay ng antibiotics (hal. Enrofloxacin, Amoxicillin, o Trimethoprim-Sulfa) ayon sa reseta ng beterinaryo.",
                "Magbigay ng suportang pag-aalaga: electrolytes at bitamina sa inuming tubig upang maiwasan ang dehydration.",
                "Maingat na linisin ang puwitan ng mga apektadong sisiw upang maiwasan ang pagbara.",
                "Tiyakin na mainit ang lugar ng mga may sakit na sisiw upang mabawasan ang stress.",
                "Tandaan: ang gamutan ay pumipigil ng sintomas ngunit hindi nito papalayasin ang mga carrier na manok.",
                "Kumonsulta agad sa beterinaryo — huwag mag-gamot nang walang reseta."
            )
        )
    ),

    "Newcastle Disease" to DiseaseInfo(
        en = DiseaseDetail(
            name = "Newcastle Disease",
            description = "Newcastle Disease is a highly contagious viral infection caused by Avian Paramyxovirus that affects the respiratory, nervous, and digestive systems of chickens, often leading to high mortality rates.",
            clinicalSigns = listOf(
                "Coughing, sneezing, and labored breathing",
                "Greenish, watery droppings",
                "Twisted neck (torticollis) or paralysis of legs and wings",
                "Decreased egg production; soft-shelled or misshapen eggs",
                "Swollen eyes and facial edema",
                "Sudden death without prior symptoms"
            ),
            recommendations = listOf(
                "Isolate sick birds immediately to prevent spread.",
                "Disinfect the coop and equipment with approved disinfectants.",
                "Provide clean water with electrolytes and vitamins to boost immunity.",
                "Consult a veterinarian for vaccination and treatment options.",
                "Report to the Bureau of Animal Industry (BAI) if multiple birds are affected.",
                "Practice strict biosecurity to prevent introduction of the virus."
            ),
            prevention = listOf(
                "Vaccinate the entire flock using approved ND vaccines (e.g., La Sota, B1) on schedule.",
                "Implement strict biosecurity: control entry of people, vehicles, and equipment.",
                "Quarantine all newly purchased birds for at least 2–4 weeks before mixing with the flock.",
                "Prevent contact with wild birds, which are natural carriers of the virus.",
                "Disinfect the poultry house between flocks using approved viricidal disinfectants.",
                "Avoid sharing equipment between farms; disinfect if unavoidable."
            ),
            treatment = listOf(
                "There is no specific antiviral treatment for Newcastle Disease.",
                "Provide supportive care: electrolytes, vitamins (especially A, C, E) in drinking water.",
                "Administer broad-spectrum antibiotics to prevent secondary bacterial infections.",
                "Keep birds warm, dry, and free from stress to support recovery.",
                "Immediately report confirmed or suspected cases to the Bureau of Animal Industry (BAI).",
                "Consult a veterinarian for proper flock management and emergency vaccination protocols."
            )
        ),
        tl = DiseaseDetail(
            name = "Newcastle Disease",
            description = "Ang Newcastle Disease ay isang lubhang nakakahawang sakit na viral na dulot ng Avian Paramyxovirus na nakakaapekto sa respiratory, nervous, at digestive system ng mga manok, kadalasang nagdudulot ng mataas na bilang ng namamatay.",
            clinicalSigns = listOf(
                "Pag-ubo, pagbahing, at hirap sa paghinga",
                "Maberde at matubig na dumi",
                "Baluktot na leeg (torticollis) o paralisis ng mga paa at pakpak",
                "Pagbaba ng itlog; malambot o may depektong balat ng itlog",
                "Namamagang mata at mukha",
                "Biglaang pagkamatay na walang sintomas"
            ),
            recommendations = listOf(
                "Ihiwalay agad ang mga may sakit na manok upang maiwasan ang pagkalat.",
                "Linisin at disimpektahan ang kulungan at mga kagamitan gamit ang aprubadong disinfectant.",
                "Magbigay ng malinis na tubig na may electrolytes at bitamina upang palakasin ang resistensya.",
                "Kumonsulta sa beterinaryo para sa bakuna at paggamot.",
                "I-report sa Bureau of Animal Industry (BAI) kung maraming manok ang apektado.",
                "Magsagawa ng mahigpit na biosecurity upang maiwasan ang pagpasok ng virus."
            ),
            prevention = listOf(
                "Bakunahan ang buong kawan gamit ang mga aprubadong ND vaccines (hal. La Sota, B1) ayon sa iskedyul.",
                "Magsagawa ng mahigpit na biosecurity: kontrolin ang pagpasok ng tao, sasakyan, at kagamitan.",
                "I-quarantine ang lahat ng bagong biniling manok nang hindi bababa sa 2–4 na linggo bago ihalo sa kawan.",
                "Pigilan ang pakikipag-ugnayan sa mga ligaw na ibon, na natural na nagdadala ng virus.",
                "Disimpektahan ang bahay-manukan sa pagitan ng mga batch gamit ang aprubadong viricidal na disinfectant.",
                "Iwasan ang pagbabahagi ng kagamitan sa pagitan ng mga bukid; disimpektahan kung kinakailangan."
            ),
            treatment = listOf(
                "Walang espesipikong antiviral na gamutan para sa Newcastle Disease.",
                "Magbigay ng suportang pag-aalaga: electrolytes at mga bitamina (lalo na A, C, E) sa inuming tubig.",
                "Magbigay ng malawak-spectrum na antibiotics upang maiwasan ang pangalawang bacterial na impeksyon.",
                "Panatilihing mainit, tuyo, at walang stress ang mga manok upang suportahan ang pagbawi.",
                "Agad na i-report ang kumpirmado o pinaghihinalaang kaso sa Bureau of Animal Industry (BAI).",
                "Kumonsulta sa beterinaryo para sa tamang pamamahala ng kawan at mga emergency na protokol ng bakuna."
            )
        )
    )
)

/**
 * Returns the DiseaseDetail for the given label and language.
 * Returns null for Normal / unknown labels.
 */
fun getDiseaseDetail(label: String, isEnglish: Boolean): DiseaseDetail? {
    return diseaseInfoMap[label]?.let { if (isEnglish) it.en else it.tl }
}
