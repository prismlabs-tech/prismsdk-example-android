package tech.prismlabs.reference.ui.scans.details.data

import android.content.Context
import tech.prismlabs.reference.R

enum class Category {
    KEY_STATS,
    UPPER_TORSO,
    LOWER_TORSO,
    ARMS,
    LEGS;

    fun title(context: Context): String {
        return when (this) {
            KEY_STATS -> context.getString(R.string.scan_details_key_stats)
            UPPER_TORSO -> context.getString(R.string.scan_details_upper_torso)
            LOWER_TORSO -> context.getString(R.string.scan_details_lower_torso)
            ARMS -> context.getString(R.string.scan_details_arms)
            LEGS -> context.getString(R.string.scan_details_legs)
        }
    }

    fun toIndex(): Int {
        return when (this) {
            KEY_STATS -> 0
            UPPER_TORSO -> 1
            LOWER_TORSO -> 2
            ARMS -> 3
            LEGS -> 4
        }
    }

    companion object {
        fun fromIndex(index: Int): Category {
            return when (index) {
                0 -> KEY_STATS
                1 -> UPPER_TORSO
                2 -> LOWER_TORSO
                3 -> ARMS
                4 -> LEGS
                else -> throw IllegalArgumentException("Invalid index: $index")
            }
        }

        fun titles(context: Context): List<String> {
            return entries.map { it.title(context) }
        }
    }
}