package com.gbunny.kayn.skill

import com.gbunny.kayn.Kayn
import com.gbunny.kayn.wands.KaynReapingHook
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class ReapingSlash(private val plugin: Kayn) : Listener {
    @EventHandler
    fun playerDropHook(e: PlayerDropItemEvent) {

        val hook = KaynReapingHook(plugin).hook
        val player = e.player
        val world = player.world

        if (e.itemDrop.itemStack == hook) {
            e.isCancelled = true

            player.apply {
                addPotionEffect(PotionEffect(PotionEffectType.SLOW, 40, 6, false, false, false))
                velocity = location.direction.setY(0).normalize().multiply(3)
            }

            object : BukkitRunnable() {
                override fun run() {
                    val targets = player.getNearbyEntities(3.0, 2.0, 3.0)

                    for (target in targets) {
                        if (target is LivingEntity) {
                            target.damage(5.0, player)
                        }
                    }

                    var timer = 0

                    object : BukkitRunnable() {
                        override fun run() {
                            val x = cos(Math.toRadians(20.0.div(360).times(timer).times(360)))
                            val z = sin(Math.toRadians(20.0.div(360).times(timer).times(360)))

                            for (i in (0..3)) {
                                for (j in (0..9)) {
                                    val locOffSet = Vector(x, 0.0, z).multiply(i.plus(j.toDouble().div(10)))
                                    val loc = player.location.add(locOffSet).add(0.0, 1.0, 0.0)

                                    world.spawnParticle(
                                        Particle.REDSTONE,
                                        loc,
                                        1,
                                        Particle.DustOptions(Color.RED, 1.0f)
                                    )
                                }
                            }
                            timer += 1

                            if (timer == 20) {
                                cancel()
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 1L)
                }
            }.runTaskLater(plugin, 20L)
        }
    }
}