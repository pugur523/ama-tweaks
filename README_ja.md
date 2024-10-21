# AmaTweaks

[English Readme here](README.md)

AmaTweaksは、Amateras SMPのために作られたいくつかの便利な機能を提供するclient sideのminecraft fabric modです。

## Features
`tweakSafeStepProtection`
> 前進中、または横に動いているときに自分より下のブロックを壊せなくなります。
これは露天掘りなどの場面で便利かもしれません。


`tweakHoldForward`
> 前進キーを押し続けます。


`tweakAutoEat`
> 設定された閾値より食料ゲージが少なくなったときに、インベントリから食料を探して自動で食べます。
閾値はconfigのgenericにある`autoEatThreshold`で設定できます。


`tweakAutoRestockHotbar`
> コンテナを開けたとき、設定されたアイテムリスト内のアイテムをコンテナから探し出してホットバーに補充します。
アイテムリストはconfigのlistにある`hotbarRestockList`で設定できます。


`tweakPreventPlacementOnPortalSides`
> スライスされたネザーポータルの側面にブロックを置けなくなります。
この機能は[taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)のアイデアをお借りしています。