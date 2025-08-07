# WeeklyNotes

A simple Android application for managing weekly notes and tasks, optimized for e-ink displays like the one on the Mudita Kompakt. 

This app was made for two reasons
- **Complete lack of weekly notes/planner apps**: I'll dive into this below.
- **Inline Note Editing**: Click on notes to edit text directly

## Weekly notes concept

This app was made to replicate the weekly note-taking experience of a Hobonichi Techo Weekly planner. The Hobonichi Techo Weekly is the only paper planner (as far as I know) which offer a full blank notetaking page per week, unencumbered by days. It's a 'catch all' page, you can write down quick notes, tasks, much like a bullet journal. Most other paper planners force you to plan things per day (and even per hour), not even offering a note area. Other planners have a note area per day, mostly catering to the journaling people. Before my Hobonichi weekly, I tried Michael Hyatt's Full Focus Planner but mostly used the week page to plan the week. It all depends on your way of life. For a lot of people, planning per week makes a lot of sense, and tasks don't need to be strictly bound to a day. The Hobonichi Techo Weekly is popular for that very reason.

As I enjoyed using the Hobonichi Weekly so much, I wondered if I could find an app on the phone to replicate the experience. Prerably it needs to be a very minimalist app and it needs to run well on my Mudita Kompakt e-ink phone. But with apps you fall into a similar issue: you either have simple notetaking apps with no future dates, or simple planner apps with no real note capability. And then you have the hybrid bloated affairs like Notion, which are just a hellscape if you just want to quickly write something. Like paper planners, the simple 'weekly' planner apps force you to plan per day. They just show the entire week at a time. Tasks either have a date, or no date. There is no 'somewhere in a week'. They don't offer a 'blank slate' note taking capability either. You could circumvent the issue by planning all future tasks for a week on the Monday of that week, but that doesn't feel right.

## Vibe coded

I'm not a developer. I'm a functional/business analyst. With the current AI craze, there is a lot to do about 'vibe coding', i.e. let a toaster write code for you. It obviously has limitations, but I was wondering if I could vibe code a minimalist app just for the weekly note taking. Could I outsource the developing part to a toaster and just do the fun bit: the functional requirements and mock-ups? It turns out you can.

I did not code this app myself. Not at all. It is entirely written by a toaster. The app is probably very bloated and the code is likely a mess. Don't ask me to build much further on it, because it will probably break if I add too luch other stuff. Already I notice that it takes a few seconds to start on my Mudita Kompakt, indicating the app is way too heavy for what it does. But it's an experiment.


## Features

- **Weekly View**: Navigate between weeks with arrow buttons
- **Infinite notes per week**: Add as many notes as you want per week
- **Status Management**: Each 'note' is a box with a status, so you can also turn it into a todo, just liek you'd do with a bullet journal. Just click on the status box on the left to the note to cycle through the statuses: blank (to do), V (done), X (canceled), > (moved: to another week, another planner, etc.), i (info/note)
- **Auto-save**: All changes are automatically saved
- **File-based Storage**: Notes are saved to JSON files for easy transfer between devices
- **E-ink Optimized**: White background with thick black interface elements
- **Auto-delete**: Notes are automatically removed when all text is deleted
- **Minimalist**: No frills, no distractions, no formatting text, no reminders or anything of that. It's barely more functional than a paper planner, except that you can edit or delete notes.
- **Week-based Files**: Each week's notes are stored in separate files

#The rest of this readme is written by a toaster

## Project Structure

```
app/src/main/java/s/nils/weeklynotes/
├── MainActivity.kt              # Main activity entry point
├── data/
│   ├── Note.kt                 # Note data model with status enum
│   ├── Week.kt                 # Week data model with date calculations
│   └── NotesStorage.kt         # File-based storage service
└── ui/
    ├── WeeklyNotesScreen.kt    # Main UI components
    └── WeeklyNotesViewModel.kt # State management and business logic
```

## Getting Started

1. Open the project in Android Studio
2. Sync Gradle dependencies
3. Build and run the application on an Android device or emulator
4. The app will open to the current week with an empty notes area
5. Tap the + button to add your first note

## Requirements

- Android API level 24+ (Android 7.0)
- Kotlin 2.0.21+
- Jetpack Compose BOM 2024.09.00

## Dependencies

- **Jetpack Compose**: Modern declarative UI toolkit
- **Material 3**: Latest Material Design components
- **Lifecycle ViewModel**: State management and lifecycle awareness
- **Gson**: JSON serialization for file storage
- **Kotlin Coroutines**: Asynchronous programming support

## File Transfer

To transfer notes between devices:

1. **Export**: Use the export function to create a JSON file
2. **Transfer**: Copy the JSON file to the new device
3. **Import**: Use the import function to load the notes

The exported file contains all weeks and notes in a human-readable JSON format. 
