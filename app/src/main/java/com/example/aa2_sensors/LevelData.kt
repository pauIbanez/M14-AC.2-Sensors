package com.example.aa2_sensors

import android.content.Context


class LevelData(val tiles: List<String>) {
    val width = tiles[0].length.toFloat()
    val height = tiles.size.toFloat()

    operator fun get(x: Int, y: Int) = tiles[y][x]

    companion object {
        fun load(context: Context, name: String) = LevelData(
            context.assets.open("levels/$name").bufferedReader()
                .readLines()
                .filter { it.isNotBlank() }
        )

        fun list(context: Context) =
            context.assets.list("levels")!!.sorted()
    }
}
