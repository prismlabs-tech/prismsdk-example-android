package tech.prismlabs.reference.scans

import tech.prismlabs.prismsdk.api_client.Scan
import tech.prismlabs.reference.extensions.ScanGroupDate
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

data class GroupedScan(
    val month: Month,
    val year: Int,
    val scans: List<Scan>
)

val GroupedScan.date: ZonedDateTime
    get() {
        return ZonedDateTime.of(
            this.year,
            this.month.value,
            1,
            0,
            0,
            0,
            0,
            ZoneId.systemDefault()
        )
    }

val GroupedScan.scanDate: String
    get() {
        return date.format(ScanGroupDate)
    }

fun List<Scan>.grouped(): List<GroupedScan> {
    return this.groupBy { "${it.createdAt.month}:${it.createdAt.year}" }.map { (_, scans) ->
        GroupedScan(
            month = scans.first().createdAt.month,
            year = scans.first().createdAt.year,
            scans = scans
        )
    }
}

fun List<GroupedScan>.list(): List<Scan> {
    return this.map { it.scans }
        .flatten()
        .sortedBy { it.createdAt }
}
