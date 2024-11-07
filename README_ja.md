# AmaTweaks

[English Readme here](README.md)<br><br>
[![License](https://img.shields.io/github/license/pugur523/ama-tweaks.svg)](https://opensource.org/licenses/MIT)
[![Issues](https://img.shields.io/github/issues/pugur523/ama-tweaks.svg)](https://github.com/pugur523/ama-tweaks/issues)
[![Modrinth](https://img.shields.io/modrinth/dt/amatweaks?label=Modrinth%20Downloads)](https://modrinth.com/mod/amatweaks)


AmaTweaksは、Amateras SMPのために作られたいくつかの便利な機能を提供するclient-sideのMinecraft Fabric modです。

## Features


### tweakAutoEat

> 設定された閾値より食料ゲージが少なくなったときに、インベントリから食料を探して自動で食べます。
閾値はconfigの`Generic`タブにある`autoEatThreshold`で設定できます。

### tweakAutoFireworkGlide

> エリトラで滑空中、`Generic`タブの`autoGlideSpeedThreshold`で設定された値よりもプレイヤーの速度が小さくなったときに自動でインベントリからロケット花火を取り出して使用します。

### tweakAutoRestockHotbar

> チェストやシュルカーボックスなどのコンテナ系ブロックを開けたとき、設定されたアイテムリスト内のアイテムをコンテナから探し出してホットバーに補充します。
補充するアイテムのリストはconfigの`List`タブにある`hotbarRestockList`で設定できます。デフォルトではロケット花火、金のニンジンがリストに設定されています。


### tweakHoldBack

> 後退キーを自動で押し続けます。


### tweakHoldForward

> 前進キーを自動で押し続けます。


### tweakHoldLeft

> 左に進むキーを自動で押し続けます。


### tweakHoldRight

> 右に進むキーを自動で押し続けます。


### tweakPersistentGammaOverride

> tweakerooのtweakGammaOverrideが起動時に有効にならない問題を修正します。


### tweakPreventBreakingAdjacentPortal

> ネザーポータルの枠組みにあたる位置のブロックが壊せなくなります。


### tweakPreventPlacementOnPortalSides

> スライスされたネザーポータルの側面にブロックを置けなくなります。
この機能は[taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)のアイデアをお借りしました。


### tweakSafeStepProtection

> 前進中、または横に動いているときに自分より下のブロックを壊せなくなります。
これは露天掘りなどの場面で便利かもしれません。