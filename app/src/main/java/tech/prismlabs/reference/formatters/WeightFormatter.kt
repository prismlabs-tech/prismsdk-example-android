package tech.prismlabs.reference.formatters

enum class WeightUnit {
    KILOGRAMS,
    POUNDS
}

private const val KILOGRAMS_TO_POUNDS = 2.20462

class WeightFormatter {
    var incomingUnit: WeightUnit = WeightUnit.KILOGRAMS
    var outgoingUnit: WeightUnit = WeightUnit.POUNDS

    fun format(value: Double): String {
        val incomingValue = if (incomingUnit == WeightUnit.KILOGRAMS) {
            value
        } else {
            value / KILOGRAMS_TO_POUNDS
        }
        val convertedValue = if (outgoingUnit == WeightUnit.KILOGRAMS) {
            incomingValue
        } else {
            incomingValue * KILOGRAMS_TO_POUNDS
        }
        return String.format("%.1f", convertedValue)
    }
}
