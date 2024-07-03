package tech.prismlabs.reference.extensions

import tech.prismlabs.prismsdk.api_client.BodyFat
import java.text.DecimalFormat

val BodyFat.formattedBodyFatPercentage: String?
    get() {
        if (bodyFatPercentage == null) return null
        val df = DecimalFormat("##.#%")
        return df.format(bodyFatPercentage!! / 100.0)
    }

val BodyFat.formattedLeanMassPercentage: String?
    get() {
        if (leanMassPercentage == null) return null
        val df = DecimalFormat("##.#%")
        return df.format(leanMassPercentage!! / 100.0)
    }