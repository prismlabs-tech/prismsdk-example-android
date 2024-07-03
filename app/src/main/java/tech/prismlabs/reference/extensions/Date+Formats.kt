package tech.prismlabs.reference.extensions

import java.time.format.DateTimeFormatter

val ScanGroupDate = DateTimeFormatter.ofPattern("MMMM yyyy")

val ScanCreatedAt = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a")