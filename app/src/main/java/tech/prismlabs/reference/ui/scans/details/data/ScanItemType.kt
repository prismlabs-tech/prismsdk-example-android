package tech.prismlabs.reference.ui.scans.details.data

import android.content.Context
import tech.prismlabs.reference.R
import tech.prismlabs.reference.formatters.LengthFormatter
import tech.prismlabs.reference.formatters.LengthUnit
import tech.prismlabs.reference.formatters.WeightFormatter
import tech.prismlabs.reference.formatters.WeightUnit


enum class ScanItemType {
    // Key Stats
    FAT_MASS_PERCENTAGE,
    LEAN_MASS_PERCENTAGE,
    FAT_MASS,
    LEAN_MASS,
    WEIGHT,
    BODY_FAT,

    // Upper Torso
    NECK,
    SHOULDERS,
    UPPER_CHEST,
    CHEST,

    // Lower Torso
    WAIST_TO_HIP,
    WAIST,
    HIPS,

    // Arms
    LET_BICEP,
    RIGHT_BICEP,

    // Legs
    LEFT_UPPER_THIGH,
    RIGHT_UPPER_THIGH,

    LEFT_LOWER_THIGH,
    RIGHT_LOWER_THIGH,

    LEFT_THIGH,
    RIGHT_THIGH,

    LEFT_CALF,
    RIGHT_CALF,

    // Other
    SPACER;

    fun title(context: Context): String {
        return when (this) {
            // Key Stats
            FAT_MASS_PERCENTAGE -> context.getString(R.string.scan_details_item_body_fat_percentage)
            LEAN_MASS_PERCENTAGE -> context.getString(R.string.scan_details_item_lean_mass_percentage)
            FAT_MASS -> context.getString(R.string.scan_details_item_fat_mass)
            LEAN_MASS -> context.getString(R.string.scan_details_item_lean_mass)
            WEIGHT -> context.getString(R.string.scan_details_item_weight)
            BODY_FAT -> context.getString(R.string.scan_details_item_body_fat)

            // Upper Torso
            NECK -> context.getString(R.string.scan_details_item_neck)
            SHOULDERS -> context.getString(R.string.scan_details_item_shoulders)
            UPPER_CHEST -> context.getString(R.string.scan_details_item_upper_chest)
            CHEST -> context.getString(R.string.scan_details_item_chest)

            // Lower Torso
            WAIST_TO_HIP -> context.getString(R.string.scan_details_item_waist_to_hip_ratio)
            WAIST -> context.getString(R.string.scan_details_item_waist)
            HIPS -> context.getString(R.string.scan_details_item_hips)

            // Arms
            LET_BICEP -> context.getString(R.string.scan_details_item_left_bicep)
            RIGHT_BICEP -> context.getString(R.string.scan_details_item_right_bicep)

            // Legs
            LEFT_UPPER_THIGH -> context.getString(R.string.scan_details_item_left_upper_thigh)
            RIGHT_UPPER_THIGH -> context.getString(R.string.scan_details_item_right_upper_thigh)

            LEFT_LOWER_THIGH -> context.getString(R.string.scan_details_item_left_lower_thigh)
            RIGHT_LOWER_THIGH -> context.getString(R.string.scan_details_item_right_lower_thigh)

            LEFT_THIGH -> context.getString(R.string.scan_details_item_left_thigh)
            RIGHT_THIGH -> context.getString(R.string.scan_details_item_right_thigh)

            LEFT_CALF -> context.getString(R.string.scan_details_item_left_calf)
            RIGHT_CALF -> context.getString(R.string.scan_details_item_right_calf)

            // Other
            SPACER -> ""
        }
    }

    fun toIndex(): Int {
        return when (this) {
            // Key Stats
            FAT_MASS_PERCENTAGE -> 0
            LEAN_MASS_PERCENTAGE -> 1
            FAT_MASS -> 2
            LEAN_MASS -> 3
            WEIGHT -> 4
            BODY_FAT -> 5

            // Upper Torso
            NECK -> 6
            SHOULDERS -> 7
            UPPER_CHEST -> 8
            CHEST -> 9

            // Lower Torso
            WAIST_TO_HIP -> 10
            WAIST -> 11
            HIPS -> 12

            // Arms
            LET_BICEP -> 13
            RIGHT_BICEP -> 14

            // Legs
            LEFT_UPPER_THIGH -> 15
            RIGHT_UPPER_THIGH -> 16

            LEFT_LOWER_THIGH -> 17
            RIGHT_LOWER_THIGH -> 18

            LEFT_THIGH -> 19
            RIGHT_THIGH -> 20

            LEFT_CALF -> 21
            RIGHT_CALF -> 22

            // Other
            SPACER -> 23
        }
    }

    fun format(value: Double): String {
        return when (this) {
            FAT_MASS, LEAN_MASS, WEIGHT -> {
                val formatter = WeightFormatter()
                formatter.incomingUnit = WeightUnit.KILOGRAMS
                formatter.outgoingUnit = WeightUnit.POUNDS
                return formatter.format(value)
            }
            FAT_MASS_PERCENTAGE, LEAN_MASS_PERCENTAGE, BODY_FAT -> {
                // Double -> Percentage
                "%.1f".format(value)
            }
            WAIST_TO_HIP -> {
                // Decimal Value to 2 decimal places
                "%.2f".format(value)
            }
            else -> {
                val formatter = LengthFormatter()
                formatter.incomingUnit = LengthUnit.METERS
                formatter.outgoingUnit = LengthUnit.INCHES
                return formatter.format(value)
            }
        }
    }

    val formatType: String get() {
        return when (this) {
            FAT_MASS, LEAN_MASS, WEIGHT -> "lb"
            FAT_MASS_PERCENTAGE, LEAN_MASS_PERCENTAGE, BODY_FAT -> "%"
            WAIST_TO_HIP -> ""
            else -> "in"
        }
    }

    companion object {
        fun fromIndex(index: Int): ScanItemType {
            return when (index) {
                // Key Stats
                0 -> FAT_MASS_PERCENTAGE
                1 -> LEAN_MASS_PERCENTAGE
                2 -> FAT_MASS
                3 -> LEAN_MASS
                4 -> WEIGHT
                5 -> BODY_FAT

                // Upper Torso
                6 -> NECK
                7 -> SHOULDERS
                8 -> UPPER_CHEST
                9 -> CHEST

                // Lower Torso
                10 -> WAIST_TO_HIP
                11 -> WAIST
                12 -> HIPS

                // Arms
                13 -> LET_BICEP
                14 -> RIGHT_BICEP

                // Legs
                15 -> LEFT_UPPER_THIGH
                16 -> RIGHT_UPPER_THIGH
                17 -> LEFT_LOWER_THIGH
                18 -> RIGHT_LOWER_THIGH
                19 -> LEFT_THIGH
                20 -> RIGHT_THIGH
                21 -> LEFT_CALF
                22 -> RIGHT_CALF

                // Other
                23 -> SPACER
                else -> throw IllegalArgumentException("Invalid index: $index")
            }
        }

        fun titles(context: Context): List<String> {
            return Category.entries.map { it.title(context) }
        }
    }
}