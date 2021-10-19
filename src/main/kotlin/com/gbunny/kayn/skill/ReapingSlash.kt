package com.gbunny.kayn.skill

import com.gbunny.kayn.Kayn
import com.gbunny.kayn.wands.KaynReapingHook
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class ReapingSlash(private val plugin: Kayn) : Listener {
    @EventHandler
    fun reapingSlash(e: PlayerDropItemEvent) {

        val hook = KaynReapingHook(plugin).hook
        val player = e.player
        val world = player.world

        if (e.itemDrop.itemStack == hook && !player.isSneaking) {
            e.isCancelled = true

            player.apply {
                velocity = location.direction.setY(0).normalize().multiply(3)
            }


            var timer = 0

            object : BukkitRunnable() {
                override fun run() {

                    player.apply {
                        velocity = Vector(0.0, 0.0, 0.0)
                        fallDistance = 0.0f
                    }
                    val degree = Math.toRadians(20.0.div(360).times(timer).times(360) + player.location.yaw)

                    val x = cos(degree)
                    val z = sin(degree)

                    for (i in (0..3)) {
                        for (j in (0..9)) {
                            val locOffSet = Vector(x, 0.0, z).multiply(i.plus(j.toDouble().div(10)))
                            val loc =
                                player.location.setDirection(Vector(0.0, 0.0, 0.0)).add(locOffSet).add(0.0, 1.0, 0.0)

                            world.spawnParticle(
                                Particle.REDSTONE,
                                loc,
                                1,
                                Particle.DustOptions(Color.RED, 1.0f)
                            )

                            val targets = loc.getNearbyEntities(0.5, 2.0, 0.5)

                            for (target in targets) {
                                if (target is LivingEntity) {
                                    if (target != player) {
                                        target.damage(5.0, player)
                                    }
                                }
                            }
                        }
                    }
                    timer += 1
                    Double.MAX_VALUE
                    if (timer == 18 || player.isDead) {
                        cancel()
                    }
                }
            }.runTaskTimer(plugin, 10L, 1L)
        }
    }
}