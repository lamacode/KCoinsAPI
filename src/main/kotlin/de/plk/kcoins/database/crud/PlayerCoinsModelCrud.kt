package de.plk.kcoins.database.crud

import de.plk.kcoins.database.Database
import de.plk.kcoins.database.crud.filter.PlayerCoinsModelFilter
import de.plk.kcoins.database.model.PlayerCoinsModel
import java.io.FileInputStream
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.swing.text.html.HTML.Tag.OL

/**
 * @author PLK
 * @since 11.04.2021 14:42
 * Copyright © 2021 | PLK | All rights reserved.
 */

class PlayerCoinsModelCrud(val database: Database) {

	private val CACHE: MutableList<PlayerCoinsModel> = mutableListOf()

	init {
		if (!database.isConnected()) database.connect()

		val sql = "CREATE TABLE IF NOT EXISTS PlayerCoins (uuid VARCHAR(100), coins BIGINT)"

		val connection = database.connection
		val statement = connection!!.prepareStatement(sql)

		statement.executeUpdate()
		database.disconnect()
	}

	fun create(uuid: UUID): PlayerCoinsModel {
		val model = PlayerCoinsModel(uuid, 0L)
		CACHE.add(model)

		if (!database.isConnected()) database.connect()
		val connection = database.connection

		with(connection!!) {
			val sql = "INSERT INTO PlayerCoins VALUES (?, ?)"

			val statement = prepareStatement(sql)
			statement.setString(1, uuid.toString())
			statement.setLong(2, model.coins)
			statement.executeUpdate().also { statement.close() }
		}

		database.disconnect()
		return model
	}

	fun read(uuid: UUID): PlayerCoinsModel {
		var model = CACHE.firstOrNull { it.uuid == uuid }

		if (model != null)
			return model

		if (!database.isConnected()) database.connect()
		val connection = database.connection

		with(connection!!) {
			val sql = "SELECT * FROM PlayerCoins WHERE uuid LIKE ?"

			val statement = prepareStatement(sql)
			statement.setString(1, uuid.toString())

			val result = statement.executeQuery()
			if (result != null && result.next()) {
				with(result) {
					model = PlayerCoinsModel(
						UUID.fromString(getString("uuid")),
						getLong("coins")
					)
				}

				result.close()
			}

			statement.close()
		}

		database.disconnect()

		model?.let { CACHE.add(it) }
		return model ?: create(uuid)
	}

	fun update(model: PlayerCoinsModel, multiple: Boolean) {
		if (!multiple) CACHE.remove(model)

		if (!database.isConnected()) database.connect()
		val connection = database.connection

		with(connection!!) {
			val sql = "UPDATE PlayerCoins SET coins = ? WHERE uuid LIKE ?"

			val statement = prepareStatement(sql)
			statement.setLong(1, model.coins)
			statement.setString(2, model.uuid.toString())
			statement.executeUpdate().also { statement.close() }
		}

		if (!multiple)
			database.disconnect()
	}

	fun delete(uuid: UUID) {
		val model = CACHE.firstOrNull { it.uuid == uuid }
		CACHE.remove(model)

		if (!database.isConnected()) database.connect()
		val connection = database.connection

		with(connection!!) {
			val sql = "DELETE FROM PlayerCoins WHERE uuid LIKE ?"

			val statement = prepareStatement(sql)
			statement.setString(1, uuid.toString())
			statement.executeUpdate().also { statement.close() }
		}

		database.disconnect()
	}

	fun updateCacheToDatabase() {
		Thread {
			CACHE.forEach {
				update(it, true)
			}

			CACHE.clear()
		}.start()

		database.disconnect()
	}
}