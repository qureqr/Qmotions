package org.qure.ui.shared

import kotlin.math.*
import kotlin.random.Random

/**
 * Класс данных для луча, который "выстреливает" из центра.
 */

/**
 * Общий класс для 3D точек
 */
data class Point3D(val x: Float, val y: Float, val z: Float, val seed: Float)

// --- Общие функции-утилиты ---

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

/**
 * <-- ВОТ ЭТА ФУНКЦИЯ, КОТОРОЙ НЕ ХВАТАЛО
 * Она создает одну случайную точку на поверхности сферы единичного радиуса.
 * Нужна для генерации направлений "выстрелов" лучей.
 */
fun randomNormalizedPoint3D(): Point3D {
    val random = Random
    val theta = random.nextFloat() * 2 * PI.toFloat()
    val phi = acos(2 * random.nextFloat() - 1)
    return Point3D(
        x = sin(phi) * cos(theta),
        y = sin(phi) * sin(theta),
        z = cos(phi),
        seed = 0f // Семя здесь не важно, так как это просто вектор направления
    )
}