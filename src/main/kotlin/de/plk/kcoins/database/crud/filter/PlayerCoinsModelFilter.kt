package de.plk.kcoins.database.crud.filter

import de.plk.kcoins.database.model.PlayerCoinsModel
import java.util.*
import java.util.function.Predicate

/**
 * @author PLK
 * @since 11.04.2021 15:28
 * Copyright © 2021 | PLK | All rights reserved.
 */

class PlayerCoinsModelFilter(private val uuid: UUID) : Predicate<PlayerCoinsModel> {

	override fun test(t: PlayerCoinsModel): Boolean {
		return t.uuid == uuid
	}

}