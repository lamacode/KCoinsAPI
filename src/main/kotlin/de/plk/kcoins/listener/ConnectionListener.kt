package de.plk.kcoins.listener

import de.plk.kcoins.CoinsChangeType
import de.plk.kcoins.core.PluginCore
import de.plk.kcoins.database.crud.PlayerCoinsModelCrud
import de.plk.kcoins.event.CoinsChangeEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * @author PLK
 * @since 11.04.2021 15:49
 * Copyright © 2021 | PLK | All rights reserved.
 */

class ConnectionListener(private val crud: PlayerCoinsModelCrud) : Listener {

	@EventHandler
	fun onConnect(event: PlayerJoinEvent) {
		val player = event.player
		crud.read(player.uniqueId)

		val api = PluginCore.instance.coinsAPI
		api.set(CoinsChangeType.SET, player.uniqueId, 200L)
	}

	@EventHandler
	fun onChange(event: CoinsChangeEvent) {
		event.getPlayer().sendMessage("Coins : ${event.model.coins}")
	}

	@EventHandler
	fun onDisconnect(event: PlayerQuitEvent) {
		val player = event.player
		val model = crud.read(player.uniqueId)

		crud.update(model, false)
	}

}