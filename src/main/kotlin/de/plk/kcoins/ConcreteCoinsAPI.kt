package de.plk.kcoins

import de.plk.kcoins.database.crud.PlayerCoinsModelCrud
import de.plk.kcoins.event.CoinsChangeEvent
import org.bukkit.Bukkit
import java.util.*

/**
 * @author PLK
 * @since 11.04.2021 14:41
 * Copyright © 2021 | PLK | All rights reserved.
 */

class ConcreteCoinsAPI(private val crud: PlayerCoinsModelCrud) : ICoinsAPI {

	override fun set(type: CoinsChangeType, uuid: UUID, coins: Long) {
		val model = crud.read(uuid)

		when (type) {
			CoinsChangeType.ADD -> if (model.coins + coins >= Long.MAX_VALUE) {
				model.coins = Long.MAX_VALUE
			} else {
				model.coins += coins
			}

			CoinsChangeType.REMOVE -> if (model.coins - coins <= 0) {
				model.coins = 0
			} else {
				model.coins -= coins
			}

			CoinsChangeType.SET -> model.coins = coins
		}

		Bukkit.getPluginManager().callEvent(CoinsChangeEvent(type, model))
	}

	override fun get(uuid: UUID): Long {
		val model = crud.read(uuid)
		return model.coins
	}
}