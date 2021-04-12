package de.plk.kcoins.core

import de.plk.kcoins.ConcreteCoinsAPI
import de.plk.kcoins.ICoinsAPI
import de.plk.kcoins.database.Database
import de.plk.kcoins.database.crud.PlayerCoinsModelCrud
import de.plk.kcoins.listener.ConnectionListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import kotlin.concurrent.fixedRateTimer

/**
 * @author PLK
 * @since 11.04.2021 14:37
 * Copyright © 2021 | PLK | All rights reserved.
 */

class PluginCore : JavaPlugin() {

	private lateinit var crud: PlayerCoinsModelCrud
	lateinit var database: Database
	lateinit var coinsAPI: ICoinsAPI

	override fun onLoad() {
		instance = this
	}

	override fun onEnable() {
		initConfig()

		database = Database(config)
		crud = PlayerCoinsModelCrud(database)
		coinsAPI = ConcreteCoinsAPI(crud)

		Bukkit.getPluginManager().registerEvents(ConnectionListener(crud), this)
	}

	override fun onDisable() {
		crud.updateCacheToDatabase()
	}

	private fun initConfig() {
		config.options().copyDefaults(true)
		saveConfig()
	}

	companion object {
		lateinit var instance: PluginCore
	}

}