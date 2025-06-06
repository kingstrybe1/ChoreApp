📋 Chore App – README
🧹 Overview
Chore App is an Android application designed to help users manage and assign household chores. Users can create, view, edit, and delete chores, specifying who assigned them and who is responsible for completing them. The app uses a local SQLite database to store chore data persistently.
---------------------------------------------------------------------------------------------------------

🚀 Features
✅ Add new chores with:

Name

Assigned By

Assigned To

📝 Edit existing chores

❌ Delete chores

📅 View when each chore was assigned (with formatted timestamp)

🔄 Data stored using local SQLite database

🧾 Chores listed in reverse chronological order

🧪 Basic logging for debugging chore details

--------------------------------------------------------------------------------------------------------

🛠️ Technologies Used
Kotlin (Android)

SQLite (Local database)

RecyclerView for chore list display

AlertDialog for chore input popups

Custom Adapter to bind chore data to views

---------------------------------------------------------------------------------------------------------

🧑‍💻 Code Structure
ChoreListActivity.kt: Main activity displaying the list of chores.

ChoreListAdapter.kt: RecyclerView adapter for displaying and managing chore items.

ChoreDatabaseHandler.kt: Handles all database operations (CRUD).

Chore.kt: Data model representing a Chore.

popup.xml: Layout for the chore input dialog.

list_row.xml: Layout for each individual chore in the list.

---------------------------------------------------------------------------------------------------------

📸 UI Preview (Optional)
Add screenshots of the popup dialog, list display, etc.

---------------------------------------------------------------------------------------------------------

📦 Setup Instructions
Clone the repository:

bash
Copy
Edit
git clone https://github.com/kingstrybe1/chore-app.git
Open the project in Android Studio.

Build and run the app on an emulator or Android device.

---------------------------------------------------------------------------------------------------------

📌 Notes
The app currently uses local storage only (no remote/cloud sync).

Basic error checking is included for empty inputs.

Future improvements may include notifications, user profiles, or Firebase integration.

