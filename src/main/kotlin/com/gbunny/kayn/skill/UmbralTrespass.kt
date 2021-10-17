package com.gbunny.kayn.skill

import com.gbunny.kayn.Kayn
import com.gbunny.kayn.wands.KaynReapingHook
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.scheduler.BukkitRunnable

class UmbralTrespass(
    private val plugin: Kayn
) : Listener {
    @EventHandler
    fun onClickSkill(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand
        val action = e.action

        val hook = KaynReapingHook(plugin).hook

        if (item == hook) {

            if (action == Action.RIGHT_CLICK_BLOCK
                || action == Action.RIGHT_CLICK_AIR
            ) {
                val target = player.getTargetEntity(10, false) ?: return

                if (target !is LivingEntity) return

                var timer = 0
                val currentGameMode = player.gameMode

                player.gameMode = GameMode.SPECTATOR

                object : BukkitRunnable() {
                    override fun run() {

                        plugin.logger.info("apply")
                        target.apply {
                            isGlowing = true
                        }
                        player.apply {
                            val loc = Location(
                                world,
                                target.location.x,
                                target.eyeLocation.y + 5,
                                target.location.z,
                                target.location.yaw,
                                45.0f
                            )

                            teleport(
                                loc,
                                PlayerTeleportEvent.TeleportCause.PLUGIN
                            )

                            world.spawnParticle(
                                Particle.BARRIER,
                                target.eyeLocation.add(0.0, 2.0, 0.0),
                                1
                            )


                        }

                        timer += 1

                        if (timer >= 100 || target.isDead) {

                            target.damage(5.0, player)

                            player.apply {
                                gameMode = currentGameMode

                                isFlying = false

                                teleport(target.location, PlayerTeleportEvent.TeleportCause.PLUGIN)

                                velocity = target.location.direction.setY(0).normalize().multiply(3)
                            }

                            target.apply {
                                isGlowing = false
                            }

                            plugin.logger.info("cancel")

                            cancel()
                        }
                    }
                }.runTaskTimer(plugin, 0L, 1L)
            }
        }
    }
}