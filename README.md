Adds the following to [FiltPick](https://www.curseforge.com/minecraft/mc-mods/filtpick):
- Filter by [Apotheosis](https://www.curseforge.com/minecraft/mc-mods/apotheosis) affix rarities
  - [Apoth Curios](https://www.curseforge.com/minecraft/mc-mods/apothic-curios) items are not filtered
- Filter by [Quality Food](https://www.curseforge.com/minecraft/mc-mods/quality-food) quality
- [Better Tridents](https://www.curseforge.com/minecraft/mc-mods/better-tridents) `Loyality` item pickup won't happen if said item is supposed to be filtered
- [Sophisticated Storage](https://www.curseforge.com/minecraft/mc-mods/sophisticated-storage) `Magnet Upgrade` will no longer pick up filtered items
- Filter earlier (and therefor skipping unneeded code from other mods)

There is an item tag which you can use to blacklist items from the two filters (`filtpick_additions:blacklist`)
- By default it contains `minecraft:golden_apple` and `minecraft:enchanted_golden_apple`

---

![showcase_1](https://i.imgur.com/HXBy2UW.png)

![showcase_2](https://i.imgur.com/INSMu2W.png)