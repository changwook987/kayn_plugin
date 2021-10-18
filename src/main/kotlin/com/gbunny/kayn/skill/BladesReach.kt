package com.gbunny.kayn.skill

import com.gbunny.kayn.Kayn
import com.gbunny.kayn.wands.KaynReapingHook
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.scheduler.BukkitRunnable

class BladesReach(
    private val plugin: Kayn
) : Listener {
    @EventHandler
    fun bladesReach(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
            if (item == KaynReapingHook(plugin).hook) {
                e.isCancelled = true

                val loc = player.eyeLocation
                val locOffSet = player.eyeLocation.direction.setY(0).normalize().multiply(0.2)

                val armorStand = player.world.spawnEntity(loc, EntityType.ARMOR_STAND, false) as ArmorStand

                armorStand.apply {
                    isMarker = true
                    isVisible = false
                }

                var timer = 0

                object : BukkitRunnable() {
                    override fun run() {
                        armorStand.apply {

                            teleport(
                                location.add(locOffSet),
                                PlayerTeleportEvent.TeleportCause.PLUGIN
                            )

                            world.spawnParticle(
                                Particle.REDSTONE,
                                location,
                                20,
                                Particle.DustOptions(Color.RED, 2.0f)
                            )

                            for (target in getNearbyEntities(1.0, 2.0, 1.0)) {
                                if (target is LivingEntity) {
                                    if (target != player) {
                                        target.damage(5.0, player)
                                    }
                                }
                            }
                        }


                        timer += 1

                        if (timer == 50) {
                            armorStand.remove()
                            cancel()
                        }
                    }
                }.runTaskTimer(plugin, 0L, 1L)
            }
        }
    }
}