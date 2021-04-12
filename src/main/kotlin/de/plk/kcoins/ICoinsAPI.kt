package de.plk.kcoins

import java.util.*

/**
 * @author PLK
 * @since 11.04.2021 14:39
 * Copyright © 2021 | PLK | All rights reserved.
 */

interface ICoinsAPI {

	fun set(type: CoinsChangeType, uuid: UUID, coins: Long)

	fun add(uuid: UUID, coins: Long) {
		set(CoinsChangeType.ADD, uuid, get(uuid) + coins)
	}

	fun remove(uuid: UUID, coins: Long) {
		set(CoinsChangeType.ADD, uuid, get(uuid) - coins)
	}

	fun get(uuid: UUID): Long

}