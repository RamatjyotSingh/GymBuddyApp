# GymBuddy - Architecture Documentation

## Table of Contents

1. Overview
2. Architectural Pattern
3. Component Architecture
4. Layer Details
   - Presentation Layer
   - Business Logic Layer
   - Data Access Layer
   - Domain Objects
5. Key Workflows
6. Design Patterns
7. Dependency Management

## Overview

GymBuddy is an Android application that follows a three-tier architecture pattern with clear separation of concerns. The application allows users to browse exercises, create custom workouts, track workout sessions, and view workout history.

## Architectural Pattern

The application follows a **Three-Tier Architecture** with the following layers:

```
┌─────────────────────┐
│  Presentation Layer │  UI components, Activities, Fragments, Adapters
├─────────────────────┤
│ Business Logic Layer│  Managers, Application Services, Business Rules
├─────────────────────┤
│   Data Access Layer │  Database Interfaces, Implementations
└─────────────────────┘
        │   │   │
        ▼   ▼   ▼
┌─────────────────────┐
│    Domain Objects   │  Core business entities shared across layers
└─────────────────────┘
```

This architecture provides:
- **Clear separation of concerns**: Each layer has a specific responsibility
- **Maintainability**: Changes to one layer have minimal impact on others
- **Testability**: Layers can be tested independently with mocks/stubs
- **Flexibility**: Database implementations can be switched without affecting business logic

## Component Architecture

### High-Level Components

```
┌──────────────────────────────────────────────────────────────────┐
│                       Presentation Layer                          │
│                                                                  │
│  ┌─────────────┐  ┌───────────┐  ┌─────────┐  ┌──────────────┐  │
│  │  Activities │  │ Fragments │  │ Adapters│  │ UI Utilities │  │
│  └─────────────┘  └───────────┘  └─────────┘  └──────────────┘  │
└──────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌──────────────────────────────────────────────────────────────────┐
│                     Business Logic Layer                          │
│                                                                  │
│  ┌─────────────────┐  ┌──────────┐  ┌────────────┐  ┌─────────┐ │
│  │ ApplicationService│ │ Managers │  │ Validators │  │ Utilities│ │
│  └─────────────────┘  └──────────┘  └────────────┘  └─────────┘ │
└──────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌──────────────────────────────────────────────────────────────────┐
│                       Data Access Layer                           │
│                                                                  │
│  ┌──────────────────┐  ┌─────────────────┐  ┌────────────────┐  │
│  │ Database Interfaces│ │ DB Implementations│ │ Database Factory│  │
│  └──────────────────┘  └─────────────────┘  └────────────────┘  │
└──────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌──────────────────────────────────────────────────────────────────┐
│                        Domain Objects                             │
│                                                                  │
│  ┌─────────┐  ┌───┐  ┌───────────────┐  ┌────────────┐  ┌──────┐│
│  │ Exercise │  │Tag│  │ WorkoutProfile │  │ WorkoutItem │  │ Session││
│  └─────────┘  └───┘  └───────────────┘  └────────────┘  └──────┘│
└──────────────────────────────────────────────────────────────────┘
```

## Layer Details

### Presentation Layer

The Presentation Layer contains all UI components and is responsible for displaying data to the user and capturing user input.

#### Activities

Activities represent the main screens of the application:

- **MainActivity**: Home screen with workout list
- **WorkoutBuilderActivity**: Create and edit workout profiles
- **WorkoutPlayerActivity**: Execute workout sessions
- **ExerciseDetailActivity**: Display exercise details
- **ExerciseListActivity**: Browse and search exercises
- **WorkoutLogActivity**: View history of completed workouts
- **WorkoutLogDetailActivity**: View details of a specific workout log
- **StartWorkoutListActivity**: Intermediate screen before starting a workout

#### Fragments

Modular UI components that can be reused across activities:

- **AddExerciseDialogFragment**: Dialog for configuring exercise parameters

#### Adapters

Connect domain objects to UI components (RecyclerViews):

- **WorkoutProfileAdapter**: Display workout profiles
- **WorkoutItemAdapter**: Display exercises within a workout
- **ExerciseAdapter**: Display exercise list
- **WorkoutLogAdapter**: Display workout history

#### UI Utilities

Helper classes for the UI layer:

- **ErrorHandler**: Centralized error handling
- **NavigationHelper**: Handle screen navigation
- **ToastErrorDisplay**: Display errors as toasts
- **AssetLoader**: Load images from assets
- **FileHandler**: Handle file operations

#### Data Flow in Presentation Layer

```
User Action → Activity/Fragment → Manager Call → Update UI with Result
```

Example from `ExerciseDetailActivity`:

```java
// Get the exercise details using the manager
ExerciseManager exerciseManager = ApplicationService.getInstance().getExerciseManager();
Exercise exercise = exerciseManager.getExerciseByID(exerciseID);

// Update the views with exercise info
setExercise(exercise);
```

### Business Logic Layer

The Business Logic Layer contains the application's core logic, validation rules, and orchestrates operations between the UI and data access layers.

#### ApplicationService

Central service locator that provides access to all managers:

```java
// Singleton pattern
public class ApplicationService {
    private static ApplicationService instance;
    
    public static ApplicationService getInstance() {
        if (instance == null) {
            instance = new ApplicationService();
        }
        return instance;
    }
    
    // Provides access to managers
    public ExerciseManager getExerciseManager() { ... }
    public WorkoutManager getWorkoutManager() { ... }
    public WorkoutSessionManager getWorkoutSessionManager() { ... }
}
```

#### Managers

Implement business rules and orchestrate operations:

- **ExerciseManager**: Handle exercise-related operations
- **WorkoutManager**: Handle workout profile operations
- **WorkoutSessionManager**: Handle workout session (history) operations

Example from `ExerciseManager`:

```java
public Exercise getExerciseByID(int id) {
    try {
        Exercise exercise = exerciseDB.getExerciseByID(id);
        if (exercise == null) {
            throw new ExerciseAccessException("Exercise not found with ID: " + id);
        }
        return exercise;
    } catch (DBException e) {
        throw new ExerciseAccessException("Failed to retrieve exercise", e);
    }
}
```

#### Validators & Utilities

- **InputValidator**: Validate user input
- **StringFormatter**: Format strings for display
- **ConfigLoader**: Load configuration data
- **WorkoutPlaybackController**: Control workout execution flow

#### Business Exceptions

Custom exceptions for different error scenarios:

- **BusinessException**: Base exception class
- **DataAccessException**: Database access errors
- **ExerciseAccessException**: Exercise-specific errors
- **InvalidInputException**: Input validation errors

### Data Access Layer

The Data Access Layer handles data persistence and retrieval, abstracting the database details from the business layer.

#### Database Interfaces

Define data operations without implementation details:

- **IExerciseDB**: Exercise database operations
- **IWorkoutDB**: Workout profile operations
- **IWorkoutSessionDB**: Workout session operations

#### Database Implementations

Concrete implementations of database interfaces:

- **ExerciseHSQLDB**: HSQL implementation for exercises
- **WorkoutHSQLDB**: HSQL implementation for workouts
- **ExerciseStub**: Test stub for exercises

#### Database Factory

Creates appropriate database implementations:

```java
public class DatabaseFactory {
    public static IExerciseDB getExerciseDB() {
        // Return HSQL or stub implementation based on configuration
    }
    
    public static IWorkoutDB getWorkoutDB() {
        // Return appropriate implementation
    }
}
```

### Domain Objects

Core business entities shared across all layers:

- **Exercise**: Represents a physical exercise with name, instructions, properties
- **Tag**: Categories or labels for exercises (e.g., "Upper Body", "Beginner")
- **WorkoutProfile**: A collection of workout items forming a complete workout
- **WorkoutItem**: An exercise with specific parameters (sets, reps, weight)
- **WorkoutSession**: A recorded instance of completing a workout
- **SessionItem**: A record of completing a specific exercise in a session

## Key Workflows

### 1. Viewing Exercise Details

```
ExerciseListActivity
    ↓ onViewMoreClicked(Exercise)
    ↓ starts
ExerciseDetailActivity
    ↓ onCreate()
    ↓ gets exercise ID from intent
    ↓ calls
ExerciseManager.getExerciseByID(id)
    ↓ calls
IExerciseDB.getExerciseByID(id)
    ↓ returns Exercise
    ↓ back to
ExerciseDetailActivity
    ↓ setExercise(exercise)
    ↓ displays details
```

### 2. Building a Workout

```
WorkoutBuilderActivity
    ↓ onClickFAB()
    ↓ launches
ExerciseListActivity
    ↓ user selects exercise
    ↓ onExerciseClicked(Exercise)
    ↓ returns to
WorkoutBuilderActivity
    ↓ handles exercise selection
    ↓ shows
AddExerciseDialogFragment
    ↓ user configures exercise parameters
    ↓ onClickBtnAddWorkoutItem()
    ↓ returns workout item to
WorkoutBuilderActivity
    ↓ adds to adapter
    ↓ user clicks Save
    ↓ onClickSave()
    ↓ generates WorkoutProfile
    ↓ calls
WorkoutManager.saveWorkout(profile)
    ↓ calls
IWorkoutDB.insertWorkoutProfile(profile)
    ↓ stores in database
```

### 3. Starting and Completing a Workout

```
MainActivity
    ↓ user selects workout
    ↓ launches
StartWorkoutListActivity
    ↓ displays workout details
    ↓ user clicks Start Workout
    ↓ launches
WorkoutPlayerActivity
    ↓ creates
WorkoutPlaybackController
    ↓ manages workout flow
    ↓ user completes workout
    ↓ onFinishedWorkout(WorkoutSession)
    ↓ session saved to database
```

## Design Patterns

1. **Service Locator Pattern**
   - `ApplicationService` provides centralized access to managers
   - Simplifies access to services without direct dependencies

2. **Repository Pattern**
   - Database interfaces abstract data access
   - Allows for multiple implementations (production, testing)

3. **Factory Pattern**
   - `DatabaseFactory` creates appropriate database implementations
   - Centralizes creation logic

4. **Adapter Pattern**
   - RecyclerView adapters convert domain objects to UI representations

5. **Strategy Pattern**
   - Error handling with different display strategies

6. **MVC/MVP Pattern**
   - Activities/Fragments as Controllers/Presenters
   - XML layouts as Views
   - Domain objects as Models

## Dependency Management

The application uses a centralized dependency management approach:

1. **Top-down dependencies**: Higher layers depend on lower layers, not vice versa
2. **Inversion of Control**: Lower layers define interfaces that higher layers implement
3. **Service Locator**: ApplicationService provides central access to managers

Dependencies flow:
```
Presentation → Business Logic → Data Access
      ↓             ↓               ↓
      └─────────────┴───────────────┘
                    ↓
               Domain Objects
```

