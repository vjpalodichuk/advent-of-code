package com.capital7software.aoc.lib.geometry

import com.capital7software.aoc.lib.collection.PriorityQueue
import com.capital7software.aoc.lib.string.clean
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings


/**
 * Suddenly, the GPU contacts you, asking for help. Someone has asked it to simulate
 * **too many particles**, and it won't be able to finish them all in time to render
 * the next frame at this rate.
 *
 * It transmits to you a buffer (your puzzle input) listing each particle in order
 * (starting with particle 0, then particle 1, particle 2, and so on). For each particle,
 * it provides the X, Y, and Z coordinates for the particle's position (p), velocity (v),
 * and acceleration (a), each in the format <X,Y,Z>.
 *
 * Each tick, all particles are updated simultaneously. A particle's properties are
 * updated in the following order:
 *
 * - Increase the X velocity by the X acceleration.
 * - Increase the Y velocity by the Y acceleration.
 * - Increase the Z velocity by the Z acceleration.
 * - Increase the X position by the X velocity.
 * - Increase the Y position by the Y velocity.
 * - Increase the Z position by the Z velocity.
 *
 * Because of seemingly tenuous rationale involving z-buffering, the GPU would like to know
 * which particle will stay closest to position <0,0,0> in the long term. Measure this using
 * the Manhattan distance, which in this situation is simply the sum of the absolute values
 * of a particle's X, Y, and Z position.
 *
 * For example, suppose you are only given two particles, both of which stay entirely on
 * the X-axis (for simplicity). Drawing the current states of particles 0 and 1 (in that order)
 * with an adjacent a number line and diagram of current X positions (marked in parentheses),
 * the following would take place:
 *
 * ```
 * p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>                         (0)(1)
 *
 * p=< 4,0,0>, v=< 1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=< 2,0,0>, v=<-2,0,0>, a=<-2,0,0>                      (1)   (0)
 *
 * p=< 4,0,0>, v=< 0,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=<-2,0,0>, v=<-4,0,0>, a=<-2,0,0>          (1)               (0)
 *
 * p=< 3,0,0>, v=<-1,0,0>, a=<-1,0,0>    -4 -3 -2 -1  0  1  2  3  4
 * p=<-8,0,0>, v=<-6,0,0>, a=<-2,0,0>                         (0)
 * ```
 *
 * At this point, particle 1 will never be closer to <0,0,0> than particle 0, and so,
 * in the long run, particle 0 will stay closest.
 *
 *
 * To simplify the problem further, the GPU would like to remove any particles that **collide**.
 * Particles collide if their positions ever **exactly match**. Because particles are updated
 * simultaneously, **more than two particles** can collide at the same time and place.
 * Once particles collide, they are removed and cannot collide with anything else after that tick.
 *
 * For example:
 *
 * ```
 * p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>
 * p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
 * p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>    (0)   (1)   (2)            (3)
 * p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * p=<-3,0,0>, v=< 3,0,0>, a=< 0,0,0>
 * p=<-2,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
 * p=<-1,0,0>, v=< 1,0,0>, a=< 0,0,0>             (0)(1)(2)      (3)
 * p=< 2,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * p=< 0,0,0>, v=< 3,0,0>, a=< 0,0,0>
 * p=< 0,0,0>, v=< 2,0,0>, a=< 0,0,0>    -6 -5 -4 -3 -2 -1  0  1  2  3
 * p=< 0,0,0>, v=< 1,0,0>, a=< 0,0,0>                       X (3)
 * p=< 1,0,0>, v=<-1,0,0>, a=< 0,0,0>
 *
 * ------destroyed by collision------
 * ------destroyed by collision------    -6 -5 -4 -3 -2 -1  0  1  2  3
 * ------destroyed by collision------                      (3)
 * p=< 0,0,0>, v=<-1,0,0>, a=< 0,0,0>
 * ```
 *
 * In this example, particles 0, 1, and 2 are simultaneously destroyed at the time and
 * place marked X. On the next tick, particle 3 passes through unharmed.
 */
class ParticleSwarm private constructor(private val target: Particle3D<Long>) {
  private companion object {
    private val PARTICLE_REGEX: Regex =
        "(?<type>\\w)=<(?<x>[ -]?\\d+),(?<y>[ -]?\\d+),(?<z>[ -]?\\d+)>".toRegex()

    private const val MAX_ITERATIONS: Int = 400
  }

  private val particles = mutableListOf<SwarmParticle>()

  /**
   * Instantiates a new [ParticleSwarm] from the specified list of particles and the specified
   * target particle.
   *
   * @param input The [List] of [String] particles to parse and load.
   * @param target The particle to swarm around.
   */
  constructor(input: List<String>, target: Particle3D<Long>) : this(target.copy()) {
    input.withIndex().forEach { (index, line) ->
      var match = PARTICLE_REGEX.find(line)

      check(match != null) { "unable to parse particle: $line" }

      var x = match.groups["x"]!!.value.clean().toLong()
      var y = match.groups["y"]!!.value.clean().toLong()
      var z = match.groups["z"]!!.value.clean().toLong()

      val position = Point3D(x, y, z)

      match = match.next()

      check(match != null) { "unable to parse particle: $line" }

      x = match.groups["x"]!!.value.clean().toLong()
      y = match.groups["y"]!!.value.clean().toLong()
      z = match.groups["z"]!!.value.clean().toLong()

      val velocity = Point3D(x, y, z)

      match = match.next()

      check(match != null) { "unable to parse particle: $line" }

      x = match.groups["x"]!!.value.clean().toLong()
      y = match.groups["y"]!!.value.clean().toLong()
      z = match.groups["z"]!!.value.clean().toLong()

      val acceleration = Point3D(x, y, z)

      particles.add(SwarmParticle(index, target, position, velocity, acceleration))
    }
  }

  /**
   * A subclass of [Particle3D] that provides an integer [id] and a [target] to swarm around.
   *
   * @param id The ID of this particle.
   * @param target The [Particle3D] to swarm around.
   * @param initialPosition The initial position of this particle.
   * @param initialVelocity The initial velocity of this particle.
   * @param initialAcceleration The initial acceleration of this particle.
   */
  class SwarmParticle(
      val id: Int,
      val target: Particle3D<Long>,
      initialPosition: Point3D<Long>,
      initialVelocity: Point3D<Long>,
      initialAcceleration: Point3D<Long>,
  ) : Particle3D<Long>(
      initialPosition,
      initialVelocity,
      initialAcceleration
  ), Comparable<SwarmParticle> {

    /**
     * The Manhattan distance between this particle and the target particle. When comparing
     * two particles this distance is used for ordering them.
     */
    val distance: Long
      get() = manhattanDistance(target)


    override fun toString(): String {
      return "SwarmParticle(id=$id, target=$target, distance=$distance, ${super.toString()})"
    }

    override fun compareTo(other: SwarmParticle): Int {
      return distance.compareTo(other.distance)
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is SwarmParticle) return false

      if (id != other.id) return false
      if (target != other.target) return false

      return true
    }

    override fun hashCode(): Int {
      var result = id
      result = 31 * result + target.hashCode()
      return result
    }

    override fun copy(): SwarmParticle {
      val partCopy = super.copy()

      return SwarmParticle(id, target, partCopy.position, partCopy.velocity, partCopy.acceleration)
    }
  }

  /**
   * Returns the [SwarmParticle] that will stay closest to the target [Particle3D] in the long run.
   *
   * @return The [SwarmParticle] that will stay closest to the target [Particle3D] in the long run.
   */
  @SuppressFBWarnings
  fun closestInTheLongRun(): SwarmParticle {
    val initial = particles.map { it.copy() }

    var closest: SwarmParticle? = null
    var changeCount = 0

    var queue = PriorityQueue<SwarmParticle>()
    queue.addAll(initial)
    var i = 0
    while (i < MAX_ITERATIONS) {
      val nextQueue = PriorityQueue<SwarmParticle>()
      while (queue.isNotEmpty()) {
        val current = queue.poll() ?: break
        current.update()
        nextQueue.offer(current)
        val t = closest
        if (t == null || current.distance < t.distance) {
          closest = current
          changeCount++
        }
      }
      queue = nextQueue
      i++
    }

    closest = queue.poll()
    return closest ?: SwarmParticle(
        -1, target.copy(), target.position, target.velocity, target.acceleration
    )
  }

  /**
   * Returns the [SwarmParticle] that will stay closest to the target [Particle3D] in the long run.
   *
   * @return The [SwarmParticle] that will stay closest to the target [Particle3D] in the long run.
   */
  @SuppressFBWarnings
  fun closestInTheLongRunFast(): SwarmParticle {
    return particles.minBy { it.acceleration.abssum() }
  }

  /**
   * Returns the list of remaining particles after all collisions have been resolved.
   *
   * @return The list of remaining particles after all collisions have been resolved.
   */
  @SuppressFBWarnings
  fun particlesAfterCollisions(): List<SwarmParticle> {
    val initial = particles.map { it.copy() }

    var lastCount: Int = initial.size
    var changeCount = 0

    val queue = PriorityQueue<SwarmParticle>()
    queue.addAll(initial)
    var i = 0

    while (i < MAX_ITERATIONS) {
      val collisions = mutableMapOf<Point3D<Long>, MutableList<SwarmParticle>>()

      while (queue.isNotEmpty()) {
        val current = queue.poll() ?: break
        current.update()
        collisions.computeIfAbsent(current.position) { mutableListOf() }.add(current)
      }
      queue.addAll(collisions.values.filter { it.size == 1 }.flatten())
      if (queue.size != lastCount) {
        changeCount++
        lastCount = queue.size
      }
      i++
    }

    return queue.sort()
  }
}
