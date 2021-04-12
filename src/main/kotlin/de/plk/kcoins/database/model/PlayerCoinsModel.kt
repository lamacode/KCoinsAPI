package de.plk.kcoins.database.model

import java.util.*

/**
 * @author PLK
 * @since 11.04.2021 14:37
 * Copyright © 2021 | PLK | All rights reserved.
 */

data class PlayerCoinsModel(
	val uuid: UUID,
 	var coins: Long
)
