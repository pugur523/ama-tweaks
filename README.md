# AmaTweaks

[日本語の説明はこちら](README_ja.md)<br><br>
[![License](https://img.shields.io/github/license/pugur523/ama-tweaks.svg)](https://opensource.org/licenses/MIT)
[![Issues](https://img.shields.io/github/issues/pugur523/ama-tweaks.svg)](https://github.com/pugur523/ama-tweaks/issues)
[![Modrinth](https://img.shields.io/modrinth/dt/amatweaks?label=Modrinth%20Downloads)](https://modrinth.com/mod/amatweaks)

AmaTweaks is a client-side Minecraft Fabric mod which has some useful features made for Amateras SMP.

## Features


### tweakAutoEat

> Automatically eat food from your inventory when your food level drops below a set threshold.
The `autoEatThreshold` can be configured in the `Generic` tab of the configs.
<br><br>

### tweakAutoFireworkGlide

> Automatically glide with firework rocket in you inventory when you're flying and flying speed is less than the value `autoGlideSpeedThreshold` that can be configured in the config `Generic`.
<br><br>

### tweakAutoRestockHotbar

> Automatically restocks items from a container block (like chests, shulker-boxes, etc.) when you open it.
The `hotbarRestockList` can be customized in the `List` tab of the config.
<br><br>

### tweakCompactScoreboard

> Displays formatted scoreboard value in the sidebar.
Implementation of >=mc1.20.4 was ported from [techutils](https://github.com/Kikugie/techutils).
<br><br>

### tweakHoldBack

> Automatically keeps you moving back.
<br><br>

### tweakHoldForward

> Automatically keeps you moving forward.
<br><br>

### tweakHoldLeft

> Automatically keeps you moving left.
<br><br>

### tweakHoldRight

> Automatically keeps you moving right.
<br><br>

### tweakInteractionHistory

> Cache specified number of player interactions.
The interactions can be checked with `/history` command in the game and cleared with `/clearinteractions`.
The number of interaction to keep can be set by `interactionHistoryMaxSize` in config generic.
<br><br>

### tweakMonoChat

> Overrides chat color with white.
<br><br>

### tweakMonoGui

> Overrides text color in gui with white.
<br><br>

### tweakPersistentGammaOverride

> Fix a tweakeroo's bug that "tweakGammaOverride" will be not enabled on client restart.
<br><br>

### tweakPickBlockRedirect

> Replace the block to be picked with litematica's pick block feature. `pickRedirectMap` can be configured in the `List` tab of the config.
<br><br>

### tweakPreventBreakingAdjacentPortal

> Prevents breaking blocks that are adjacent to a nether portal.
<br><br>

### tweakPreventPlacementOnPortalSides

> Prevents block placement on sliced nether portal sides.
This feature was ported from [taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)
<br><br>

### tweakSafeStepProtection

> Prevent breaking blocks below you while you're moving forward or sideways.
This can be useful for activities like perimeter digging.
<br><br>

### tweakSelectiveBlockRendering

> [!CAUTION]
> This feature has not supported block entity render selection yet.<br>

> Renders only specified blocks. The blocks can be configured in the list tab so check it. This feature will reload the entire world (renderer) on settings changed.
List entries example: `minecraft:white_stained_glass`, `minecraft:dirt`, `minecraft:bedrock`, etc.
<br><br>

### tweakSelectiveEntityRendering

> Renders only specified entities. The entities can be configured in the list tab.
List entries example: `player`, `tnt`, `slime`, `item`.
<br> This feature was ported from [taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)
<br><br>