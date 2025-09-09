# Karoo 3 – AutoStart Ride (Extension)

Eine minimalistische **Karoo‑3 Extension**, die **automatisch die Ride‑Aufzeichnung startet** (Freeride, auch **ohne Ziel/Route**).
Nach Installation/Boot startet ein Foreground‑Service, bringt die Map/Ride‑Seite nach vorn und simuliert den Start‑Button.

> ⚠️ Hinweis: Die exakten Klassen-/Paketnamen der **karoo-ext**‑API können sich je nach Version unterscheiden.
> Prüfe die Imports in `srv/AutoStartRideService.kt` und `ext/AutoStartRideExtension.kt` gegen deine verwendete Version.

## Features
- Auto‑Start der Aufzeichnung nach Boot/Install
- Erkennt Zustand: `NotRecording` → startet, `Paused` → Resume
- Kurzer In‑Ride‑Alert („Aufzeichnung läuft“)

## Projektstruktur
```
Karoo3-AutoStart-Ride/
  app/
    src/main/java/de/alex/autostartride/
      boot/BootReceiver.kt
      srv/AutoStartRideService.kt
      srv/Notifications.kt
      ext/AutoStartRideExtension.kt
    src/main/res/xml/extension_info.xml
    src/main/res/values/strings.xml
    src/main/AndroidManifest.xml
  build.gradle.kts
  settings.gradle.kts
  gradle.properties
  app/build.gradle.kts
```

## Voraussetzungen
- Android Studio Jellyfish o. neuer
- JDK 17
- Karoo 3 (Sideload erlaubt)
- **karoo-ext** Library (über GitHub Packages)

### GitHub Packages Zugriff
Setze **einmalig** deine Zugangsdaten, z. B. in `~/.gradle/gradle.properties`:
```
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_PAT   # mit read:packages
```
Alternativ können die Umgebungsvariablen `GPR_USER` und `GPR_TOKEN` gesetzt werden.

## Build
```bash
./gradlew :app:assembleDebug
```
Das Ergebnis liegt unter `app/build/outputs/apk/debug/app-debug.apk`.

## Sideload auf dem Karoo 3
1. APK auf das Gerät kopieren (per USB/ADB oder via Hammerhead‑Companion).
2. Auf dem Karoo installieren (Sideload zulassen).
3. Gerät neu starten (oder kurz Flugmodus an/aus), dann startet die App den Service automatisch.
4. Die Ride‑App geht in den Vordergrund und **Recording beginnt**.

## Anpassungen
- **Explizites Start‑Kommando**: Falls eine neuere karoo‑ext‑Version `StartRide` o.ä. anbietet, ersetze den Hardware‑Button‑Dispatch.
- **Nur bei Bewegung starten**: Ride‑State + Speed‑Sensor/GPSSpeed prüfen, bevor du den Start auslöst.
- **Failsafe**: Timeout einbauen (falls UI nicht im Vordergrund landet, erneut `ShowMapPage` dispatchen).

## Lizenz
MIT

## GitHub Actions (CI)
Push dieses Projekt auf GitHub und aktiviere unter **Settings → Secrets** zwei Secrets:
- `GPR_USER` = Dein GitHub-Username
- `GPR_TOKEN` = Personal Access Token mit `read:packages`

Danach baut die Action automatisch ein **Debug-APK** und legt es als **Artifact** an.
