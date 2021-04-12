package de.plk.kcoins.database

import org.bukkit.configuration.file.FileConfiguration
import java.sql.Connection
import java.sql.DriverManager

/**
 * @author PLK
 * @since 11.04.2021 14:43
 * Copyright © 2021 | PLK | All rights reserved.
 */

class Database(val config: FileConfiguration) {

	var connection: Connection? = null

	fun connect() {
		Class.forName("com.mysql.jdbc.Driver").newInstance()
		connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s",
			config.getString("hostname"),
			config.getInt("port"),
			config.getString("database")
		), config.getString("username"), config.getString("password"))
	}

	fun disconnect() {
		connection?.close()
		connection = null
	}

	fun isConnected(): Boolean {
		return connection != null
	}

}