package tech.prismlabs.reference.extensions

import tech.prismlabs.prismsdk.api_client.Scan
import java.time.LocalDateTime
import java.util.TimeZone

val Scan.localCreatedAt: String
    get() {
        val timezone = TimeZone.getDefault()
        val localDateTime = LocalDateTime.ofInstant(this.createdAt.toInstant(), timezone.toZoneId())
        return localDateTime.format(ScanCreatedAt)
    }