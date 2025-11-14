package org.ReDiego0.animationPlayer.utils.effects

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import org.bukkit.EntityEffect
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.lang.reflect.InvocationTargetException

inline fun <reified T : ItemMeta> ItemStack.meta(crossinline block: T.() -> Unit): ItemStack {
    this.itemMeta = (this.itemMeta as? T)?.apply(block)
    return this
}

object CustomTotemRessurectEffect {
    /**
     * Sends a custom totem animation with the texture of a totem with a [customModelDataId]
     *
     * @param player            the player that will receive the totem animation
     * @param customModelDataId the model ID of the custom totem
     * @param protocolManager   el manager de ProtocolLib
     */
    fun sendCustomTotemAnimation(player: Player, customModelDataId: Int, protocolManager: ProtocolManager) =
        sendCustomTotemAnimation(
            player,
            ItemStack(Material.TOTEM_OF_UNDYING).meta<ItemMeta> {
                setCustomModelData(customModelDataId)
            },
            protocolManager
        )

    /**
     * Sends a custom totem animation with the texture of a totem with a [customModelDataId]
     *
     * @param player    the player that will receive the totem animation
     * @param itemStack the ItemStack that will be set in the player's offhand. Needs to be a [Material.TOTEM_OF_UNDYING].
     * @param protocolManager   el manager de ProtocolLib
     */
    fun sendCustomTotemAnimation(player: Player, itemStack: ItemStack, protocolManager: ProtocolManager) {

        if (itemStack.type != Material.TOTEM_OF_UNDYING)
            throw IllegalArgumentException("ItemStack $itemStack isn't a Totem of Undying!")

        val packet = protocolManager.createPacket(PacketType.Play.Server.SET_SLOT)
        packet.integers.write(0, 0)
        packet.integers.write(1, 0)
        packet.integers.write(2, 45)
        packet.itemModifier.write(0, itemStack)

        try {
            protocolManager.sendServerPacket(player, packet)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("No se pudo enviar el paquete SET_SLOT al jugador ${player.name}:", e)
        }

        player.playEffect(EntityEffect.TOTEM_RESURRECT)
        player.updateInventory()
    }
}