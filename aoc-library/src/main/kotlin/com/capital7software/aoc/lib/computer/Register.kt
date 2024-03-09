package com.capital7software.aoc.lib.computer

/**
 * Represents a register in a processor that can hold a single [Long] value.
 *
 * @param id The identifier of this register.
 * @param value The mutable value of this register.
 */
data class Register(val id: String, var value: Long = 0)
