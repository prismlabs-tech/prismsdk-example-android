package tech.prismlabs.reference.extensions

import tech.prismlabs.prismsdk.api_client.Weight
import tech.prismlabs.reference.formatters.WeightFormatter
import tech.prismlabs.reference.formatters.WeightUnit

val Weight.formatted: String
    get() {
        // Since we only get Kilograms back from the server, we need to convert to lbs if the user's preference is lbs
        // Currently Kotlin or Android doesn't have any frameworks that do this automatically like iOS,
        // we have to do the calculation manually.
        // We don't have Locale support either, so we are just going to force pounds for now.
        // Below if the logic needed to use wither Kilos or Pounds
//        val value = if (this.unit == WeightUnit.KILOGRAMS) {
//            round(this.value).toInt()
//        } else {
//            round(this.value * 2.20462).toInt()
//        }
//        val measurement = if (this.unit == WeightUnit.KILOGRAMS) "kg" else "lbs"

        val formatter = WeightFormatter()
        formatter.incomingUnit = WeightUnit.KILOGRAMS
        formatter.outgoingUnit = WeightUnit.POUNDS
        val value = formatter.format(this.value)
        val measurement = "lbs"
        return "$value $measurement"
    }