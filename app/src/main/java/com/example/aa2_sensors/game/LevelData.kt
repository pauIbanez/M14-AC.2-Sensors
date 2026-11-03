package com.example.aa2_sensors.game

import android.content.Context


class LevelData(val tiles: List<String>) {
    companion object {
        fun load(context: Context, name: String) = LevelData(
            context.assets.open("levels/$name.lvl").bufferedReader()
                .readLines()
                .filter { it.isNotBlank() }
        )

        fun list(context: Context) =
            context.assets.list("levels")!!.sorted()
    }

    val width = tiles[0].length.toFloat()
    val height = tiles.size.toFloat()

    lateinit var startPos: Vector2


    init {
        setStartPos()
    }

    private fun setStartPos() {
        for ((y, row) in tiles.withIndex()) {
            for ((x, cell) in row.withIndex()) {
                if (cell == 's') startPos = Vector2(x + 0.5f, y + 0.5f)
            }
        }
    }

    fun isEnd(pos: Vector2): Boolean = isIn('e', pos)
    fun isHole(pos: Vector2): Boolean = isIn('h', pos)
    fun isWall(pos: Vector2): Boolean = isIn('w', pos)


    private fun isIn(tile: Char, pos: Vector2): Boolean {
        if (pos.x < 0 || pos.y < 0) return false
        val x = pos.x.toInt()
        val y = pos.y.toInt()
        if (x >= width || y >= height) return false

        return tiles[y][x] == tile
    }

    operator fun get(x: Int, y: Int) = tiles[y][x]


}
