package com.gbunny.kayn

import com.gbunny.kayn.skill.UmbralTrespass
import com.gbunny.kayn.wands.KaynReapingHook
import org.bukkit.plugin.java.JavaPlugin

class Kayn : JavaPlugin() {
    override fun onEnable() {

        skillLoad()
        addRecipes()

        logger.info("plugin on Enable")
    }

    private fun skillLoad() {
        server.pluginManager.apply {
            registerEvents(
                UmbralTrespass(this@Kayn), this@Kayn
            )
            registerEvents(
                KaynReapingHook(this@Kayn), this@Kayn
            )
        }
    }

    private fun addRecipes() {
        server.apply {
            val hook = KaynReapingHook(this@Kayn)
            addRecipe(hook.recipe)
        }
    }
}
