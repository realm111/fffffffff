# TPAHere Auto (Fabric 1.21.11, client-side)

Kazdemu graczowi, ktory napisze cokolwiek na czacie, wysyla `/tpahere <nick>`.
Menu pod klawiszem **K** (zmienisz w Opcje > Sterowanie).

## Funkcje
- wlacz / wylacz, cooldown na gracza, odstep miedzy komendami (anty-spam)
- wlasna komenda z `{player}` (np. `tpahere {player}`)
- config: `config/tpahere_auto.json`

## Build
Wrzuc na GitHub - Actions zbuduje jar (zakladka Actions > Artifacts).
Lokalnie: `gradle build` (Java 21, Gradle 9.2+), jar w `build/libs/`.

## Instalacja
Fabric Loader 0.18+ + Fabric API 1.21.11, jar do folderu `mods`.
