# TaskFlow — zadanie 5

Projekt z laboratorium nawigacji w Jetpack Compose.

## Uruchomienie

Otwórz katalog repozytorium w Android Studio i wykonaj synchronizację Gradle.
Projekt wymaga JDK 17, Android SDK 35 i dostępu do internetu do pobrania zależności.
Minimalna wersja urządzenia to Android 8.0 (API 26).

## Zaimplementowane funkcje

- Dodawanie zadań z odrzucaniem pustego tytułu i tytułu złożonego z białych znaków.
- Lista, szczegóły, zmiana statusu ukończenia i usuwanie zadania.
- Statystyki z licznikami i procentem ukończenia.
- Stan aplikacji udostępniany przez StateFlow.
- Typowane trasy @Serializable i odczyt taskId przez toRoute.
- Ekrany przyjmują dane i lambdy, cała nawigacja znajduje się w TaskFlowApp.
- Zakładki Zadania / Statystyki z saveState, restoreState i launchSingleTop.
- Zachowanie przewinięcia listy i ukrywanie paska dolnego w formularzu i szczegółach.
- Deep link taskflow://tasks/{taskId} oraz obsługa brakującego zadania.

Zadania są przechowywane w pamięci ViewModelu i pozostają dostępne przy obrocie
ekranu. Po zakończeniu procesu aplikacji lista jest pusta. Instrukcja nie wymaga bazy danych.

Repozytorium było puste, dlatego model i ekrany zostały przygotowane na podstawie
instrukcji laboratorium. Zachowano nazwy tras, plików i wymagane znaczniki testTag.

## Testy

Testy jednostkowe w TasksViewModelTest obejmują walidację, dodawanie, liczniki,
przełączanie ukończenia, usuwanie, nieznane identyfikatory i unikalność ID.

Testy instrumentacyjne w TaskFlowTest obejmują izolowany formularz, pełny przepływ
dodawania i szczegółów z TestNavHostController, zmianę statusu i usuwanie,
zakładki, deep link oraz zachowanie przewinięcia listy.

Windows:

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug assembleDebugAndroidTest
.\gradlew.bat connectedDebugAndroidTest
```

Linux / macOS:

```sh
./gradlew testDebugUnitTest assembleDebug assembleDebugAndroidTest
./gradlew connectedDebugAndroidTest
```

Druga komenda wymaga uruchomionego emulatora lub telefonu z debugowaniem USB.
Workflow Android checks buduje aplikację i uruchamia testy na emulatorze API 35.

## Deep link

Po dodaniu zadania odczytaj jego ID z debuggera lub testu i użyj:

```sh
adb shell am start -W -a android.intent.action.VIEW -d "taskflow://tasks/TASK_ID" pl.edu.taskflow
```

ID przekazane w linku musi odpowiadać zadaniu w aktualnie działającym procesie.
Nieznany identyfikator wyświetla komunikat o braku zadania.
