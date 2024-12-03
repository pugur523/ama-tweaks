# AmaTweaks

[English Readme here](README.md)<br><br>
[![License](https://img.shields.io/github/license/pugur523/ama-tweaks.svg)](https://opensource.org/licenses/MIT)
[![Issues](https://img.shields.io/github/issues/pugur523/ama-tweaks.svg)](https://github.com/pugur523/ama-tweaks/issues)
[![Modrinth](https://img.shields.io/modrinth/dt/amatweaks?label=Modrinth%20Downloads)](https://modrinth.com/mod/amatweaks)


AmaTweaksはAmateras SMPのために作られたclient-sideのMinecraft Fabric modです。

## Features


### tweakAutoEat

> 設定された閾値より食料ゲージが少なくなったときに、インベントリから食料を探して自動で食べます。
閾値はconfigの`Generic`タブにある`autoEatThreshold`で設定できます。

### tweakAutoFireworkGlide

> エリトラで滑空中、`Generic`タブの`autoGlideSpeedThreshold`で設定された値よりもプレイヤーの速度が小さくなったときに自動でインベントリからロケット花火を取り出して使用します。

### tweakAutoRestockHotbar

> チェストやシュルカーボックスなどのコンテナ系ブロックを開いたとき、設定されたアイテムリスト内のアイテムをコンテナから探し出してホットバー(インベントリ)に補充します。
補充するアイテムのリストはconfigの`List`タブにある`hotbarRestockList`で設定できます。デフォルトではロケット花火と金のニンジンがリストに設定されています。

### tweakCompactScoreboard

> サイドバーに表示されるスコアボードの数値をフォーマットしてコンパクトにします。1.20.4以上のバージョンにおける実装は[techutil](https://github.com/Kikugie/techutils)からお借りしました。


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

### tweakPickBlockRedirect

> litematicaでpickを行う時に、リダイレクトが指定されていればpickするべきブロックをそれに置き換えます。リダイレクトマップはconfigの`List`タブにある`pickRedirectMap`で設定できます。(この機能は耕地ブロックや土の道などの入手不可能アイテムを土としてpickしたり、水を氷としてpickするなどの用途を意図しています。)


### tweakPreventBreakingAdjacentPortal

> ネザーポータルの枠組みにあたる位置のブロックが壊せなくなります。


### tweakPreventPlacementOnPortalSides

> スライスされたネザーポータルの側面にブロックを置けなくなります。
この機能は[taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)のアイデアをお借りしました。


### tweakSafeStepProtection

> 前、または横に動いているときに自分より下のブロックを壊せなくなります。
露天掘りのような場面での使用を想定しています。

### tweakSelectiveBlockRendering

> [!CAUTION]
> この機能はブロックエンティティのカスタマイズをまだサポートしていません。<br>
> ブロックの種類ごとに描画を行うかどうかカスタムできます。ブロックの設定はリストタブで指定できます。
この機能はリストの設定が変わるたびにワールドレンダラーを再読み込みします。
リストに追加するエントリーの記法は、ネームスペースを含めたブロックidを採用しています。(例: `minecraft:black_stained_glass`, `minecraft:grass_block`, `minecraft:bedrock`など)


### tweakSelectiveEntityRendering

> エンティティの種類ごとに描画を行うかどうかカスタムできます。基本は`tweakSelectiveBlockRendering`のエンティティ版と捉えて問題ないですが、リストの記法が異なるので注意してください。
リストの記法はネームスペースを含めないエンティティタイプ名を採用しています(例: `player`, `tnt`, `zombie`, `item`など)。
<br>この機能は[taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)のアイデアをお借りしました。