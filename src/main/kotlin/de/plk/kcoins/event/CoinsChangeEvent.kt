package de.plk.kcoins.event

import de.plk.kcoins.CoinsChangeType
import de.plk.kcoins.database.model.PlayerCoinsModel
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * @author PLK
 * @since 11.04.2021 15:51
 * Copyright © 2021 | PLK | All rights reserved.
 */

class CoinsChangeEvent(
	val changeType: CoinsChangeType,
	val model: PlayerCoinsModel
) : Event() {

	companion object {
		private val HANDLER_LIST = HandlerList()

		@JvmStatic
		fun getHandlerList(): HandlerList {
			return HANDLER_LIST
		}
	}

	fun getPlayer(): Player {
		return Bukkit.getPlayer(model.uuid)
	}

	override fun getHandlers(): HandlerList {
		return HANDLER_LIST
	}

}