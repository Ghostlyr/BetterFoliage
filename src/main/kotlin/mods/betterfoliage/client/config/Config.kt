package mods.betterfoliage.client.config

import cpw.mods.fml.client.event.ConfigChangedEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import mods.betterfoliage.BetterFoliageMod
import mods.betterfoliage.client.gui.BiomeListConfigEntry
import mods.octarinecore.config.*
import mods.octarinecore.metaprog.reflectField
import net.minecraft.client.Minecraft
import net.minecraft.world.biome.BiomeGenBase

// BetterFoliage-specific property delegates
private fun featureEnable() = boolean(true).lang("enabled")
private fun distanceLimit() = int(min=1, max=1000, default=1000).lang("distance")
fun biomeList(defaults: (BiomeGenBase) -> Boolean) = intList {
    BiomeGenBase.getBiomeGenArray().filter { it != null && defaults(it) }.map { it.biomeID }.toTypedArray()
}.apply { guiClass = BiomeListConfigEntry::class.java }

// Biome filter methods
private fun BiomeGenBase.filterTemp(min: Float?, max: Float?) = (min == null || min <= temperature) && (max == null || max >= temperature)
private fun BiomeGenBase.filterRain(min: Float?, max: Float?) = (min == null || min <= rainfall) && (max == null || max >= rainfall)
private fun BiomeGenBase.filterClass(vararg name: String) = name.any { it in this.javaClass.name.toLowerCase() }

// Config singleton
@SideOnly(Side.CLIENT)
object Config : DelegatingConfig(BetterFoliageMod.MOD_ID, BetterFoliageMod.DOMAIN) {

    var enabled by boolean(true)

    object blocks {
        val dirt = BlockMatcher(BetterFoliageMod.DOMAIN, "DirtDefault.cfg")
        val grass = BlockMatcher(BetterFoliageMod.DOMAIN, "GrassDefault.cfg")
        val leaves = BlockMatcher(BetterFoliageMod.DOMAIN, "LeavesDefault.cfg")
        val crops = BlockMatcher(BetterFoliageMod.DOMAIN, "CropDefault.cfg")
        val logs = BlockMatcher(BetterFoliageMod.DOMAIN, "LogDefault.cfg")
        val sand = BlockMatcher(BetterFoliageMod.DOMAIN, "SandDefault.cfg")
        val lilypad = BlockMatcher(BetterFoliageMod.DOMAIN, "LilypadDefault.cfg")
        val cactus = BlockMatcher(BetterFoliageMod.DOMAIN, "CactusDefault.cfg")
    }

    object leaves {
        val enabled by featureEnable()
        val distance by distanceLimit()
        val hOffset by double(max=0.4, default=0.2).lang("hOffset")
        val vOffset by double(max=0.4, default=0.1).lang("vOffset")
        val size by double(min=0.75, max=2.5, default=1.4).lang("size")
        val dense by boolean(false)
    }

    object shortGrass {
        val grassEnabled by boolean(true)
        val myceliumEnabled by boolean(true)
        val snowEnabled by boolean(true)
        val distance by distanceLimit()
        val hOffset by double(max=0.4, default=0.2).lang("hOffset")
        val heightMin by double(min=0.1, max=2.5, default=0.6).lang("heightMin")
        val heightMax by double(min=0.1, max=2.5, default=0.8).lang("heightMax")
        val size by double(min=0.5, max=1.5, default=1.0).lang("size")
        val useGenerated by boolean(false)
        val shaderWind by boolean(true).lang("shaderWind")
        val saturationThreshold by double(default=0.1)
    }

//    object hangingGrass {
//        var enabled by featureEnable()
//        var distance by distanceLimit()
//        var size by double(min=0.25, max=1.5, default=0.75).lang("size")
//        var separation by double(max=0.5, default=0.25)
//    }

    object connectedGrass {
        val enabled by boolean(true)
        val snowEnabled by boolean(false)
    }

    object roundLogs {
        val enabled by featureEnable()
        val distance by distanceLimit()
        val radiusSmall by double(max=0.5, default=0.25)
        val radiusLarge by double(max=0.5, default=0.44)
        val dimming by float(default = 0.7)
        val connectSolids by boolean(false)
        val lenientConnect by boolean(true)
        val connectPerpendicular by boolean(true)
        val connectGrass by boolean(true)
        val zProtection by double(min = 0.9, default = 0.99)
    }

    object cactus {
        val enabled by featureEnable()
        val distance by distanceLimit()
        val size by double(min=0.5, max=1.5, default=0.8).lang("size")
        val sizeVariation by double(max=0.5, default=0.1)
        val hOffset by double(max=0.5, default=0.1).lang("hOffset")
    }

    object lilypad {
        val enabled by featureEnable()
        val distance by distanceLimit()
        val hOffset by double(max=0.25, default=0.1).lang("hOffset")
        val flowerChance by int(max=64, default=16, min=0)
    }

    object reed {
        val enabled by featureEnable()
        val distance by distanceLimit()
        val hOffset by double(max=0.4, default=0.2).lang("hOffset")
        val heightMin by double(min=1.5, max=3.5, default=1.7).lang("heightMin")
        val heightMax by double(min=1.5, max=3.5, default=2.2).lang("heightMax")
        val population by int(max=64, default=32).lang("population")
        val biomes by biomeList { it.filterTemp(0.4f, null) && it.filterRain(0.4f, null) }
        val shaderWind by boolean(true).lang("shaderWind")
    }

    object algae {
        val enabled by featureEnable()
        val distance by distanceLimit()
        val hOffset by double(max=0.25, default=0.1).lang("hOffset")
        val size by double(min=0.5, max=1.5, default=1.0).lang("size")
        val heightMin by double(min=0.1, max=1.5, default=0.5).lang("heightMin")
        val heightMax by double(min=0.1, max=1.5, default=1.0).lang("heightMax")
        val population by int(max=64, default=48).lang("population")
        val biomes by biomeList { it.filterClass("river", "ocean") }
        val shaderWind by boolean(true).lang("shaderWind")
    }

    object coral {
        val enabled by featureEnable()
        val distance by distanceLimit()
        val shallowWater by boolean(false)
        val hOffset by double(max=0.4, default=0.2).lang("hOffset")
        val vOffset by double(max=0.4, default=0.1).lang("vOffset")
        val size by double(min=0.5, max=1.5, default=0.7).lang("size")
        val crustSize by double(min=0.5, max=1.5, default=1.4)
        val chance by int(max=64, default=32)
        val population by int(max=64, default=48).lang("population")
        val biomes by biomeList { it.filterClass("river", "ocean", "beach") }
    }

    object netherrack {
        val enabled by featureEnable()
        val distance by distanceLimit()
        val hOffset by double(max=0.4, default=0.2).lang("hOffset")
        val heightMin by double(min=0.1, max=1.5, default=0.6).lang("heightMin")
        val heightMax by double(min=0.1, max=1.5, default=0.8).lang("heightMax")
        val size by double(min=0.5, max=1.5, default=1.0).lang("size")
    }

    object fallingLeaves {
        val enabled by featureEnable()
        val speed by double(min=0.01, max=0.15, default=0.05)
        val windStrength by double(min=0.1, max=2.0, default=0.5)
        val stormStrength by double(min=0.1, max=2.0, default=0.8)
        val size by double(min=0.25, max=1.5, default=0.75).lang("size")
        val chance by double(min=0.001, max=1.0, default=0.05)
        val perturb by double(min=0.01, max=1.0, default=0.25)
        val lifetime by double(min=1.0, max=15.0, default=5.0)
        val opacityHack by boolean(true)
    }

    object risingSoul {
        val enabled by featureEnable()
        val chance by double(min=0.001, max=1.0, default=0.02)
        val perturb by double(min=0.01, max=0.25, default=0.05)
        val headSize by double(min=0.25, max=1.5, default=1.0)
        val trailSize by double(min=0.25, max=1.5, default=0.75)
        val opacity by float(min=0.05, max=1.0, default=0.5)
        val sizeDecay by double(min=0.5, max=1.0, default=0.97)
        val opacityDecay by float(min=0.5, max=1.0, default=0.97)
        val lifetime by double(min=1.0, max=15.0, default=4.0)
        val trailLength by int(min=2, max=128, default=48)
        val trailDensity by int(min=1, max=16, default=3)
    }

    override fun onChange(event: ConfigChangedEvent.OnConfigChangedEvent) {
        super.onChange(event)
        if (hasChanged(blocks, shortGrass["saturationThreshold"]))
            Minecraft.getMinecraft().refreshResources()
        else
            Minecraft.getMinecraft().renderGlobal.loadRenderers()
    }
}
