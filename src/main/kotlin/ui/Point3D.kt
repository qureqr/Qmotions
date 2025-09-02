// Файл: org/qure/ui/Point3D.kt
package org.qure.ui

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

// Общий класс для 3D точек
data class Point3D(val x: Float, val y: Float, val z: Float, val seed: Float)

// Общие функции-расширения для работы с 3D точками
fun Point3D.distanceTo(other: Point3D): Float {
    return sqrt((x - other.x).pow(2) + (y - other.y).pow(2) + (z - other.z).pow(2))
}

fun Point3D.rotateY(angle: Float): Point3D {
    val newX = x * cos(angle) + z * sin(angle)
    val newZ = -x * sin(angle) + z * cos(angle)
    return this.copy(x = newX, z = newZ)
}

fun Point3D.rotateX(angle: Float): Point3D {
    val newY = y * cos(angle) - z * sin(angle)
    val newZ = y * sin(angle) + z * cos(angle)
    return this.copy(y = newY, z = newZ)
}