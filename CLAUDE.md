# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project context

Android interview challenge: a movie search app backed by the OMDB API (`https://www.omdbapi.com/`). The task (see README.md) is to refactor toward Clean Architecture, SOLID, MVVM, proper DI (Hilt preferred, Koin acceptable), and Jetpack Compose, with lifecycle-aware state management and error handling. Unit tests and edge-case handling are "nice to have".

Single Gradle module `:app`; dependency versions live in `gradle/libs.versions.toml`. minSdk 31, targetSdk/compileSdk 36, Java 11 target. Both Hilt (via KSP) and Koin are on the classpath, but only Hilt is actually wired up.

## Commands

Use `./gradlew` (Git Bash) or `.\gradlew.bat` (PowerShell).

```
./gradlew assembleDebug                 # build
./gradlew installDebug                  # install on connected device/emulator
./gradlew testDebugUnitTest             # JVM unit tests (JUnit4, MockK, Robolectric, coroutines-test)
./gradlew testDebugUnitTest --tests "com.tv.movies.domain.GetMoviesUseCaseTest"   # single test class
./gradlew connectedDebugAndroidTest     # Espresso instrumentation tests (needs device/emulator)
./gradlew lint
```

## Architecture

The live code is under `app/src/main/java/com/tv/movies/` (matches `namespace`/`applicationId` `com.tv.movies`), layered as:

- `data/` — `api/MoviesApi` (Retrofit, kotlinx-serialization converter; API key is a hardcoded default query param), `dto/` (OMDB JSON shapes: `Search`, `Response`, `Error` fields), `mapper/` (DTO → domain extension functions), `repository/MoviesRepositoryImpl` (runs on `Dispatchers.IO`, maps OMDB `Error` field and exceptions to `Resource.Error`).
- `domain/` — `model/Movie`, `repository/MoviesRepository` interface, `usecases/GetMoviesUseCase`, and `Resource<T>` (Success/Error/Loading) as the data→UI result wrapper.
- `di/` — Hilt `SingletonComponent` modules: `ApiModule` (Retrofit + API) and `RepositoryModule` (`@Binds` impl → interface).
- `ui/` — `viewmodels/MoviesViewModule` (note the "Module" spelling; it is the `@HiltViewModel`). Its `uiState` is derived from a `searchQuery` `MutableStateFlow` via `debounce(500) → distinctUntilChanged → flatMapLatest → stateIn(WhileSubscribed(5000))`; `getMovies(name)` just updates the query, and `init` seeds it with `"Matrix"`. `UiState` is a sealed class nested in the ViewModel.

**Two parallel UIs exist for the same ViewModel:**
- View-based (currently active): `MainActivity` is a `FragmentActivity` that hosts `ui/screens/MoviesFragment` (XML layouts `fragment_movies.xml` / `item_movie.xml`, `ui/adapter/MoviesAdapter` RecyclerView `ListAdapter`). The existing tests target this path.
- Compose: `ui/screens/MoviesScreen` + `ui/theme/MoviesTheme`. The Compose `MainActivity` body is commented out at the top of `MainActivity.kt`; switching back requires making it a `ComponentActivity` with `setContent`.

### Stray `com.test.movies` package

`app/src/main/java/com/test/movies/` is an incomplete duplicate of the data/domain/DI/ViewModel layers. It imports classes that don't exist (e.g. `com.test.movies.MovieSearchResponse`, `com.test.movies.data.dto.*`, `com.test.movies.domain.model.Movie`, `data.mapper.toMovies`), so it will break compilation, and its Hilt modules would conflict with the `com.tv.movies` bindings for `MoviesApi`/`MoviesRepository`. Treat `com.tv.movies` as the source of truth; confirm with the user before deleting or completing `com.test.movies`.

## Tests

- `app/src/test/.../domain/GetMoviesUseCaseTest.kt` — MockK + `runTest` pattern for domain tests.
- `app/src/test/.../MoviesFragmentTest.kt` — Robolectric test that `MainActivity` hosts `MoviesFragment` (`isIncludeAndroidResources = true` is set for this).
- `app/src/androidTest/.../MoviesFragmentTest.kt` — Espresso checks on view IDs `searchEditText`, `stateTextView`, `moviesRecyclerView`. These will break if the Fragment UI is replaced with Compose.

## Maintenance
When a change makes anything in this file inaccurate (renamed classes, moved packages, new commands), update this file in the same change.
