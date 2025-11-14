package org.ReDiego0.animationPlayer

import org.ReDiego0.animationPlayer.utils.effects.CustomTotemRessurectEffect
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val plugin: AnimationPlayer) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        val modelId = plugin.config.getInt("totem-model-id")

        if (modelId == 0) {
            plugin.logger.warning("El 'totem-model-id' en config.yml no está configurado correctamente o es 0. No se mostrará la animación.")
            return
        }

        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            try {
                CustomTotemRessurectEffect.sendCustomTotemAnimation(
                    player,
                    modelId,
                    plugin.protocolManager
                )
            } catch (e: Exception) {
                plugin.logger.severe("Error al intentar enviar la animación del tótem al jugador ${player.name}:")
                e.printStackTrace()
            }
        }, 30L)
    }
}