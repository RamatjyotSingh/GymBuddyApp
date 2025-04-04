---
layout: page
title: Post-Mortem
include_in_header: true
---

# Post-Mortem 🏖️

### Successes 🎯

Our greatest strength throughout this project was our ability to adapt and reprioritize effectively. When we identified that refactoring needed to take precedence over new feature development, we quickly adjusted our workflow. During the third iteration, we successfully shifted our focus toward increasing code coverage and addressing grader feedback. This adaptability ensured we remained focused on what mattered most at each stage of the project.

### Challenges 🥀

Despite our strengths, we faced significant challenges with team communication. Too often, code modifications were implemented without properly informing the entire team, resulting in quality issues that could have been caught with proper reviews. The lack of a structured review process and inconsistent communication about ongoing developments hindered our efficiency and code quality throughout the project.

### Lessons for the Future 🌱

If we could start over, we would make several key changes to our approach:

1. Begin concrete progress earlier in iteration 1 rather than delaying action
2. Implement the clear and frequent task announcements we eventually developed from the very beginning
3. Establish a structured review process (like regular in-meeting code reviews) from day one to prevent problematic code from being merged

Our communication issues at the start became the source of many downstream problems. Though we gradually developed better habits as the term progressed, establishing these practices earlier would have significantly improved our outcomes.

### Time Investment Analysis 🕒

**Most Time-Consuming:** Getting the architecture right consumed the majority of our time. We continually revisited early decisions that proved problematic, particularly regarding how domain-specific objects should relate to each other and how to structure layers to keep the persistence layer independent of Android packages.

**Least Time-Consuming:** Administrative tasks like assigning issues and reviewing iteration requirements were relatively quick, typically completed within a single two-hour meeting.

**Biggest Surprise:** The entire team was surprised by the extent of our difficulties with miscommunication and merge conflicts. These challenges were more persistent and impactful than we had anticipated.

### Outstanding Issues 🪲

We have one known bug: cover images for exercises don't properly respond to device aspect ratios, resulting in cropping or zooming issues on certain screens. Additionally, in the pursuit of prioritizing cleaning up technical debt, we had to defer the feature of saving completed sessions to a future iteration. This will be first on our to-do list in the next iteration!

### Revisiting Our Previous Retrospective 🔎

In our iteration 2 retrospective, we identified problems with merging branches onto `dev` close to deadlines and set a goal to avoid large architectural changes late in the iteration.

While our teamwork improved in iteration 3, we encountered remarkably similar issues. Despite being more cautious about timing our merges, we still implemented significant architectural changes near the end of the iteration to resolve dependencies between the persistence layer and Android packages. 

When attempting to merge `dev` onto `main`, we encountered unexpected problems tracing back to an older pull request that had been mistakenly merged onto `main` and subsequently reverted. Despite numerous troubleshooting attempts, the only successful solution was manually copying files to the main branch.

This experience reinforced that even with increased caution, making substantial changes late in an iteration carries inherent risk. In future iterations, we'll implement preliminary merge commits well before deadlines to validate the merging process and ensure this cannot happen again.

### Moving Forward 🌅

This project greatly reinforced that effective communication is fundamental to successful collaboration. One person's decisions invariably influence the work of other team members. As we move forward, we'll prioritize:

1. Frequent and transparent communication
2. Structured planning processes
3. Rigorous code reviews

These practices will ensure smoother collaboration and consistently higher code quality in our future work together.