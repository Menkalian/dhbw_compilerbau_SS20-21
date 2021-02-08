# Projektaufgabe Compilerbau Sommersemester 2020/21

[![pipeline status](https://gitlab.com/kiliankra/dhbw_compilerbau_SS20-21/badges/main/pipeline.svg)](https://github.com/Menkalian/dhbw_compilerbau_SS20-21)
[![coverage report](https://gitlab.com/kiliankra/dhbw_compilerbau_SS20-21/badges/main/coverage.svg)](https://github.com/Menkalian/dhbw_compilerbau_SS20-21)

## Kurzbeschreibung der Aufgabe

Diese Aufgabe ist für maximal 5 Personen gedacht. Im Falle einer Dreiergruppe müssen nur der Top-Down-Parser und die beiden Visitoren implementiert werden. Im Falle einer Vierergruppe kann der generische Lexer entfallen.

Analog zum Drachenbuch (Seiten 209 bis 216) soll ein Verfahren implementiert werden, das aus einem regulären Ausdruck direkt einen deterministischen endlichen Automaten (DEA) erzeugt, der exakt die Worte akzeptiert, welche den regulären Ausdruck matchen.

### Bewertung

|       Was        |   Anteil   |
|------------------|------------|
| Umsetzung        |  50 Punkte |
| Kompilierbarkeit |   5 Punkte |
| Schnittstellen   |  20 Punkte |
| Konventionen     |  25 Punkte |
| **Summe**        | 100 Punkte |

### Abgabefrist

Eine **Zip-Datei** (**Quelldateien** und **Unit-Tests**) bis zum **05.03.2021** über **Moodle**

## Projektstruktur

Dieses Projekt nutzt Gradle als Buildtool und für automatisiertes Testen. Das Projekt ist auf [Github](https://github.com/Menkalian/dhbw_compilerbau_SS20-21) einsehbar. Dort sind auch die Ergebnisse des automatisierten Build/Test-Verfahrens zu sehen (und auch diese Markdown-Datei vernünftig formatiert).

Um das Projekt nur zu kompilieren, kann der Befehl `./gradlew build` genutzt werden. Die `.class`-Dateien sind dann im Verzeichnis `${projDir}/build/classes/java/main/${package}`.

Um die Unit-Tests auszuführen kann `./gradlew test` oder `./gradlew check` verwendet werden. Bei letzteren wird auch noch ein Coverage-Report angelegt. Dieser ist [hier](build/reports/jacoco/test/html/index.html) einsehbar.

### Kurzzusammenfassung:

```shell
$./gradlew build  # Baut das Projekt
$./gradlew test   # Führt die Unit-Tests aus
$./gradlew check  # Führt die Unit-Tests aus und erstellt einen Coverage Report
````