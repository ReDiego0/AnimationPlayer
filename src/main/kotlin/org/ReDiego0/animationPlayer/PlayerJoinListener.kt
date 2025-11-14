package org.ReDiego0.animationPlayer

import org.ReDiego0.animationPlayer.utils.effects.CustomTotemRessurectEffect
import org.bukkit.ChatColor
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val plugin: AnimationPlayer) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val config = plugin.config

        val delay = config.getLong("join-delay-ticks", 40L)

        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            if (!player.isOnline) {
                return@Runnable
            }

            try {
                val modelId = config.getInt("totem-model-id")
                if (modelId > 0) {
                    CustomTotemRessurectEffect.sendCustomTotemAnimation(
                        player,
                        modelId,
                        plugin.protocolManager
                    )
                }

                if (config.getBoolean("title.enabled")) {
                    val titleRaw = config.getString("title.text") ?: ""
                    val subtitleRaw = config.getString("title.subtitle") ?: ""
                    val title = formatString(player, titleRaw)
                    val subtitle = formatString(player, subtitleRaw)
                    val fadeIn = config.getInt("title.fade-in", 10)
                    val stay = config.getInt("title.stay", 60)
                    val fadeOut = config.getInt("title.fade-out", 10)

                    if (title.isNotEmpty() || subtitle.isNotEmpty()) {
                        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
                    }
                }

                if (config.getBoolean("chat-message.enabled")) {
                    val messageRaw = config.getString("chat-message.message") ?: ""
                    val message = formatString(player, messageRaw)
                    if (message.isNotEmpty() && message.isNotBlank()) {
                        player.sendMessage(message)
                    }
                }

                if (config.getBoolean("join-sound.enabled")) {
                    val soundKey = config.getString("join-sound.sound-key") ?: ""
                    if (soundKey.isNotEmpty()) {
                        val volume = config.getDouble("join-sound.volume", 1.0).toFloat()
                        val pitch = config.getDouble("join-sound.pitch", 1.0).toFloat()
                        player.playSound(player.location, soundKey, SoundCategory.PLAYERS, volume, pitch)
                    }
                }

            } catch (e: Exception) {
                plugin.logger.severe("Error al ejecutar los efectos de bienvenida para ${player.name}:")
                e.printStackTrace()
            }
        }, delay)
    }

    /**
     * Función de ayuda para traducir códigos de color (\& ) y placeholders (\%player_name\%).
     */
    private fun formatString(player: Player, text: String): String {
        val replacedText = text.replace("%player_name%", player.name)
        return ChatColor.translateAlternateColorCodes('&', replacedText)
    }
}