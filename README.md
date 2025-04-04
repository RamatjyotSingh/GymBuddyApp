# Gym Buddy

Gym Buddy is an all-in-one workout assistant application that lets you create and start personalized workouts, as well as keep track of previous workouts.

As of our latest release, users are able to:
- Create workout profiles by selecting from a predefined set of exercises.
- Start workouts based on created workout profiles.
- View previously completed workouts.

## Building and running the project

As of our latest release, Gym Buddy is compatible with Java 21 and Android Studio Meerkat 2024.3.1 Patch 1, with Android Gradle Plugin version 8.9.1. The application is known to be working with the built-in Android emulator within Android Studio, using the Pixel Tablet device with API 36.

To build the program, install the indicated version of Android Studio, and make sure that the Android Gradle Plugin is up-to-date with version 8.9.1. In Android Studio, with the project folder open, click the "Sync Project with Gradle Files" button in the top right, close to the settings icon. This should successfully build the project.

To run the application, simply select the "app" configuration in the run configuration drop down menu and click the play button!

Note that you will need to setup the virtual Pixel Tablet device within the Device Manager in Android Studio prior to running the application.

## Building and running the website

The website for our project can be run locally using Jekyll, but is also being hosted online [here](https://typrgs.github.io/comp3350-project-presentation/), if you don't want to go through the trouble of running it locally.

To run the website locally, you will have to install Jekyll. First, install Ruby version 2.7.0 or higher. The easiest way to do this on Windows is to use the [Ruby installer](https://rubyinstaller.org/). More installation information for all supported operating systems can be found [here](https://jekyllrb.com/docs/installation/). After installing Ruby, the next step is to install RubyGems. This can easily be installed through the Windows terminal by running the following command:

```
gem update --system
```

You can also installing RubyGems manually by following the steps [here](https://rubygems.org/pages/download).

Finally, install Jekyll by running the following in the terminal:

```
gem install jekyll bundler
```

Now you should have Jekyll installed on your system.

The last thing we need is the `github-pages` gem, as this website was built with Github Pages integration. Install this gem by running the following in the terminal:

```
gem install github-pages
```

The website can now be run locally. Navigate to the website directory and run the following in the terminal:

```
bundle exec jekyll serve
```

This will build the website, and host it locally on port 4000. Keep this terminal session open, so you can properly close it when you are finished viewing the website. In a web browser, search for `localhost:4000` or `127.0.0.1:4000`, and that should bring you to our website.

When you're done viewing it, close the Jekyll server by hitting the `CTRL+C` key combination and type `Y` to terminate the session.

This website uses a Jekyll theme that was created by Emil Baehr, and modified by us. The Github repository for this theme can found [here](https://github.com/emilbaehr/automatic-app-landing-page).

## Vision

Our project vision statement is written in [VISION.md](doc/VISION.md).

## Retrospective

The notes from our most recent retrospective can be found in [RETROSPECTIVE.md](doc/RETROSPECTIVE.md).  
This file includes a reflection on our previous issues, our goals for the this iteration, and a chart showing our project velocity over the last two iterations.

## Architecture

Our architecture is outlined in detail in [ARCHITECTURE.md](doc/ARCHITECTURE.md).

## Future releases

Several planned features for this project went incomplete given the short amount of time we had to work on it. Here are some other features we had planned, but ran out of time to add:

- Setting personal challenges
- Achievements
- Creating custom exercises
- Recommended exercises
- Motivational notifications and tips
- Music player integration

Our team decided that for this iteration, it would be best to commit to working on cleaning up our codebase and ensuring that all of our existing features are as functional as possible, rather than pushing through with half-baked features that take away from the user experience.

In particular, we spent a large amount of time working with the persistence and logic layers of our project, trying to make the code as best as possible in terms of quality.