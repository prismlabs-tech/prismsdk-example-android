package tech.prismlabs.reference.formatters

enum class LengthUnit {
    METERS,
    INCHES
}

private const val METERS_TO_INCHES = 39.3701

class LengthFormatter {
    var incomingUnit: LengthUnit = LengthUnit.METERS
    var outgoingUnit: LengthUnit = LengthUnit.INCHES

    fun format(value: Double): String {
        val incomingValue = if (incomingUnit == LengthUnit.METERS) {
            value
        } else {
            value / METERS_TO_INCHES
        }
        val convertedValue = if (outgoingUnit == LengthUnit.METERS) {
            incomingValue
        } else {
            incomingValue * METERS_TO_INCHES
        }
        return String.format("%.1f", convertedValue)
    }
}