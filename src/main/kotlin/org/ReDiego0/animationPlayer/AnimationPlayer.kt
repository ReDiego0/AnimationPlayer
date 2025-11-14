package org.ReDiego0.animationPlayer

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class AnimationPlayer : JavaPlugin() {

    lateinit var protocolManager: ProtocolManager

    override fun onEnable() {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            logger.severe("¡ProtocolLib no se encontró!")
            logger.severe("Este plugin es requerido para funcionar.")
            logger.severe("Desactivando AnimationPlayer...")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        this.protocolManager = ProtocolLibrary.getProtocolManager()
        saveDefaultConfig()

        server.pluginManager.registerEvents(PlayerJoinListener(this), this)

        logger.info("AnimationPlayer v${description.version} habilitado correctamente.")
        logger.info("¡Listo para mostrar animaciones!")
    }

    override fun onDisable() {
        logger.info("AnimationPlayer deshabilitado.")
    }
}