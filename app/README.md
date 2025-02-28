# FindMyCar

FindMyCar ist eine Android-App, die es ermöglicht, den aktuellen Standort als Parkplatz zu speichern und später wiederzufinden. Die App nutzt OpenStreetMap zur Kartenanzeige und speichert die Parkplätze lokal in einer SQLite-Datenbank. Zudem kann man die gespeicherten Parkplätze auf einer Karte anzeigen und die Navigation über Google Maps starten.

---

## **Funktionen**
- **Standort speichern**: Speichert den aktuellen Standort als Parkplatz mit einer Vibrationsbestätigung.
- **Parkplatzverlauf anzeigen**: Zeigt eine Liste der gespeicherten Parkplätze mit Datum und Koordinaten.
- **Navigation starten**: Öffnet den gespeicherten Parkplatz direkt in Google Maps.
- **Vibration bei Speicherung**: Haptisches Feedback als Bestätigung.
- **Marker auf der Karte**: Der gespeicherte Parkplatz wird mit einem Pin-Icon auf der Karte markiert.

---

### Schwierigkeiten
ich hatte schwierigkeiten und änderungen bei der vorstellung von der Weiterleitung auf Google Maps und beim PopUp

## **Technologien & Bibliotheken**
- **Kotlin** – Hauptprogrammiersprache der App
- **Jetpack Compose** – UI-Design
- **OpenStreetMap (OSMDroid)** – Kartenanzeige
- **Google Maps Intent** – Zur Navigation zum Parkplatz
- **SQLite (Room)** – Speicherung der Parkplätze
- **Location Services (FusedLocationProviderClient)** – Ermittlung des aktuellen Standorts
- **Material Design 3** – Modernes UI-Design

---

## **Einrichtung**
Man muss nachdem man die App downloaded hat muss man freigeben dass der Standort verwenden darf.

### **Projekt klonen**
```sh
git clone https://github.com/hereqi/FindMyCar_LBb.git
cd FindMyCar_LB_B

### **Linter**
Die Lint Dateien sind in folgendem Verzeichnis findbar:
```
./app/build/reports/lint-results-debug.html
```

Um das Projekt zu linten, kann folgender Befehl ausgeführt werden:
```
./gradlew lint
```