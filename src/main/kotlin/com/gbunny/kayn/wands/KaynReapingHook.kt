package com.gbunny.kayn.wands

import com.gbunny.kayn.Kayn
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

class KaynReapingHook(plugin: Kayn) : Listener {
    val hook = ItemStack(Material.IRON_HOE, 1)

    init {
        hook.apply {
            itemMeta = itemMeta.apply {
                displayName(text().content("Kayn's Reaping Hook").color(NamedTextColor.RED).build())

                lore(
                    listOf(
                        text().content("상대를 향해 사용시 궁극기 사용").build(),
                        text().content("휘두를 시 돌진후 회전베기").build()
                    )
                )
                isUnbreakable = true
                addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
                addItemFlags(ItemFlag.HIDE_UNBREAKABLE)

                addEnchant(
                    Enchantment.DAMAGE_ALL,
                    3,
                    true
                )
            }
        }
    }

    val recipe = ShapedRecipe(NamespacedKey(plugin, "kayn_reaping_hook"), hook).apply {
        shape(
            "  D",
            " SI",
            "S  "
        )
        setIngredient('D', ItemStack(Material.DIAMOND))
        setIngredient('S', ItemStack(Material.STICK))
        setIngredient('I', ItemStack(Material.IRON_INGOT))
    }

    @EventHandler
    fun playerInteract(e: PlayerInteractEvent) {
        if (e.player.inventory.itemInMainHand == hook)
            if (e.action == Action.RIGHT_CLICK_BLOCK) {

                if (e.clickedBlock?.type == Material.GRASS_BLOCK
                    || e.clickedBlock?.type == Material.DIRT
                    || e.clickedBlock?.type == Material.COARSE_DIRT
                    || e.clickedBlock?.type == Material.ROOTED_DIRT
                ) {
                    e.isCancelled = true
                }
            }
    }
}

