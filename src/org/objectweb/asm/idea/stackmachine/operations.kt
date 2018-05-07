package org.objectweb.asm.idea.stackmachine

interface Operations<T: Number> {
    fun add(a: T, b: T): T
    fun subtract(a: T, b: T): T
    fun multiply(a: T, b: T): T
    fun divide(a: T, b: T): T
    fun reminder(a: T, b: T): T
}

object IntOperations: Operations<Int> {
    override fun add(a: Int, b: Int) = a + b
    override fun subtract(a: Int, b: Int) = a - b
    override fun multiply(a: Int, b: Int) = a * b
    override fun divide(a: Int, b: Int) = a / b
    override fun reminder(a: Int, b: Int) = a % b
}

object LongOperations: Operations<Long> {
    override fun add(a: Long, b: Long) = a + b
    override fun subtract(a: Long, b: Long) = a - b
    override fun multiply(a: Long, b: Long) = a * b
    override fun divide(a: Long, b: Long) = a / b
    override fun reminder(a: Long, b: Long) = a % b
}

object FloatOperations: Operations<Float> {
    override fun add(a: Float, b: Float) = a + b
    override fun subtract(a: Float, b: Float) = a - b
    override fun multiply(a: Float, b: Float) = a * b
    override fun divide(a: Float, b: Float) = a / b
    override fun reminder(a: Float, b: Float) = a % b
}

object DoubleOperations: Operations<Double> {
    override fun add(a: Double, b: Double) = a + b
    override fun subtract(a: Double, b: Double) = a - b
    override fun multiply(a: Double, b: Double) = a * b
    override fun divide(a: Double, b: Double) = a / b
    override fun reminder(a: Double, b: Double) = a % b
}