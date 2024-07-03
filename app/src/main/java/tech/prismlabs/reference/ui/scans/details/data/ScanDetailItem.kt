package tech.prismlabs.reference.ui.scans.details.data

import tech.prismlabs.prismsdk.api_client.Scan

data class ScanDetailItem(val type: ScanItemType, val value: Double) {
    val formattedValue: String
        get() = type.format(value)
    val formattedValueSuffix: String
        get() = type.formatType

    companion object {
        val spacer = ScanDetailItem(ScanItemType.SPACER, 0.0)

        fun fromScan(scan: Scan): List<ScanDetailItem> {
            return listOf(
                // Key Stats
                ScanDetailItem(ScanItemType.FAT_MASS_PERCENTAGE, scan.bodyFat?.bodyFatPercentage ?: 0.0),
                ScanDetailItem(ScanItemType.LEAN_MASS_PERCENTAGE, scan.bodyFat?.leanMassPercentage ?: 0.0),
                ScanDetailItem(ScanItemType.FAT_MASS, scan.bodyFat?.fatMass ?: 0.0),
                ScanDetailItem(ScanItemType.LEAN_MASS, scan.bodyFat?.leanMass ?: 0.0),
                ScanDetailItem(ScanItemType.WEIGHT, scan.weight.value),
                ScanDetailItem(ScanItemType.WAIST_TO_HIP, scan.measurements?.waistToHipRatio ?: 0.0),

                // Upper Torso
                ScanDetailItem(ScanItemType.NECK, scan.measurements?.neckFit ?: 0.0),
                ScanDetailItem(ScanItemType.SHOULDERS, scan.measurements?.shoulderFit ?: 0.0),
                ScanDetailItem(ScanItemType.UPPER_CHEST, scan.measurements?.upperChestFit ?: 0.0),
                ScanDetailItem(ScanItemType.CHEST, scan.measurements?.chestFit ?: 0.0),

                // Lower Torso
                ScanDetailItem(ScanItemType.WAIST, scan.measurements?.waistFit ?: 0.0),
                ScanDetailItem(ScanItemType.HIPS, scan.measurements?.hipsFit ?: 0.0),

                // Arms
                ScanDetailItem(ScanItemType.LET_BICEP, scan.measurements?.midArmLeftFit ?: 0.0),
                ScanDetailItem(ScanItemType.RIGHT_BICEP, scan.measurements?.midArmRightFit ?: 0.0),

                // Legs
                ScanDetailItem(ScanItemType.LEFT_UPPER_THIGH, scan.measurements?.thighLeftFit ?: 0.0),
                ScanDetailItem(ScanItemType.RIGHT_UPPER_THIGH, scan.measurements?.thighRightFit ?: 0.0),
                ScanDetailItem(ScanItemType.LEFT_LOWER_THIGH, scan.measurements?.calfLeftFit ?: 0.0),
                ScanDetailItem(ScanItemType.RIGHT_LOWER_THIGH, scan.measurements?.calfRightFit ?: 0.0),
                ScanDetailItem(ScanItemType.LEFT_CALF, scan.measurements?.thighLeftFit ?: 0.0),
                ScanDetailItem(ScanItemType.RIGHT_CALF, scan.measurements?.thighRightFit ?: 0.0)
            )
        }
    }
}
