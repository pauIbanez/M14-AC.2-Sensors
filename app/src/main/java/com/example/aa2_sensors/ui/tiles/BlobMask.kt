package com.example.aa2_sensors.ui.tiles

/**
 *
 * COMPLETLEY AI GENERATED
 */


/** Neighbour bits of a blob autotile, clockwise from north. */
object Blob {
    const val N = 1
    const val NE = 2
    const val E = 4
    const val SE = 8
    const val S = 16
    const val SW = 32
    const val W = 64
    const val NW = 128
}

fun Int.hasBit(bit: Int): Boolean = (this and bit) != 0

/** A corner only matters when both edges next to it are solid. This folds 256 masks down to 47. */
fun normalizeBlobMask(mask: Int): Int {
    var result = mask
    if (!(mask.hasBit(Blob.N) && mask.hasBit(Blob.E))) result = result and Blob.NE.inv()
    if (!(mask.hasBit(Blob.S) && mask.hasBit(Blob.E))) result = result and Blob.SE.inv()
    if (!(mask.hasBit(Blob.S) && mask.hasBit(Blob.W))) result = result and Blob.SW.inv()
    if (!(mask.hasBit(Blob.N) && mask.hasBit(Blob.W))) result = result and Blob.NW.inv()
    return result
}

/** The 47 distinct tiles of the set. */
val BlobTileMasks: List<Int> = (0..255).map(::normalizeBlobMask).distinct().sorted()

/** Works out which tile belongs at (x, y), given a lookup for solid cells. */
fun blobMaskAt(x: Int, y: Int, isSolid: (Int, Int) -> Boolean): Int {
    var mask = 0
    if (isSolid(x, y - 1)) mask = mask or Blob.N
    if (isSolid(x + 1, y - 1)) mask = mask or Blob.NE
    if (isSolid(x + 1, y)) mask = mask or Blob.E
    if (isSolid(x + 1, y + 1)) mask = mask or Blob.SE
    if (isSolid(x, y + 1)) mask = mask or Blob.S
    if (isSolid(x - 1, y + 1)) mask = mask or Blob.SW
    if (isSolid(x - 1, y)) mask = mask or Blob.W
    if (isSolid(x - 1, y - 1)) mask = mask or Blob.NW
    return normalizeBlobMask(mask)
}
