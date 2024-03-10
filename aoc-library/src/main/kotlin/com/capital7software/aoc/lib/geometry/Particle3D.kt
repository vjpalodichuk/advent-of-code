package com.capital7software.aoc.lib.geometry

/**
 * A three-dimensional particle with a position, velocity, and acceleration.
 *
 * @param initialPosition The initial position of this particle.
 * @param initialVelocity The initial velocity of this particle.
 * @param initialAcceleration The initial acceleration of this particle.
 */
open class Particle3D<T>(
    initialPosition: Point3D<T>,
    initialVelocity: Point3D<T>,
    initialAcceleration: Point3D<T>
) where T: Number, T: Comparable<T> {
  private var currentPosition: Point3D<T> = initialPosition
  private var currentVelocity: Point3D<T> = initialVelocity
  private var currentAcceleration: Point3D<T> = initialAcceleration

  /**
   * The current position of this particle.
   */
  val position: Point3D<T>
    get() = currentPosition

  /**
   * The current velocity of this particle.
   */
  val velocity: Point3D<T>
    get() = currentVelocity

  /**
   * The current acceleration of this particle.
   */
  val acceleration: Point3D<T>
    get() = currentAcceleration

  /**
   * Updates the velocity of this particle by applying the acceleration to it. Then updates the
   * position of this particle by applying the velocity.
   */
  fun update() {
    currentVelocity += currentAcceleration
    currentPosition += currentVelocity
  }

  override fun toString(): String {
    return "Particle3D(position=$position, velocity=$velocity, acceleration=$acceleration)"
  }

  /**
   * Returns the Manhattan Distance between two particles. The Manhattan Distance is the sum of
   * the absolute values of the coordinate differences of the particle's current positions.
   * In other words it is:
   *
   * ```
   * abs(a.x() - b.x()) + abs(a.y() - b.y()) + abs(a.z() - b.z())
   * ```
   *
   * where a is the current position of this particle and b is the current position of the
   * other particle.
   *
   * @param other The particle to get the distance from this particle.
   * @return The Manhattan Distance between this particle and the other particle.
   */
  fun manhattanDistance(other: Particle3D<T>): T {
    return currentPosition.manhattanDistance(other.currentPosition)
  }

  /**
   * Returns a copy of this particle that can be modified independently of this particle.
   *
   * @return A copy of this particle that can be modified independently of this particle.
   */
  open fun copy(): Particle3D<T> = Particle3D(position, velocity, acceleration)
}
