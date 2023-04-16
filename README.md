# To-Do-List

To Do List is an Android application that allows you to track day-to-day tasks and set reminders that trigger notifications when tasks are pending.

![To-Do-List Demo](https://user-images.githubusercontent.com/60143996/211173929-818a7e44-5736-464d-b67a-32fce3d39864.mp4)

![To-Do-List Screenshots](https://user-images.githubusercontent.com/60143996/211175438-6dba23df-58f3-4fd9-bcfe-5da7f0813ea2.png)


## Screen Shots
<p float="left">
  <img src="https://user-images.githubusercontent.com/60143996/232342122-f721da78-4bb4-4e68-9500-853d11ddad69.png" width="512" />
  <img src="https://user-images.githubusercontent.com/60143996/232342123-c0057929-cb2a-4b81-831a-8ba7b98f8c3c.png" width="512" /> 
</p>
<p float="left">
  <img src="https://user-images.githubusercontent.com/60143996/232342125-429ea19a-407c-42f5-b1f2-48a479e1ecc5.png" width="512" />
  <img src="https://user-images.githubusercontent.com/60143996/232342126-dd3e1fe2-b80f-4245-8aed-8d1d1e8146ba.png" width="512" />
</p>

## Features

- Add and delete tasks
- Mark tasks as complete or incomplete
- Set reminders for pending tasks
- Receive notifications for pending tasks

## Architecture and Components

This application is built using MVVM (Model-View-ViewModel) architecture with the following major components:

- **Room Database**: Used for local data storage and retrieval
- **Navigation Component**: Used for navigation between fragments
- **RecyclerView and LiveData Binding**: Used for displaying the list of tasks
- **Alarm and Notification Manager**: Used for setting reminders and sending notifications

**Note**: To receive push notifications, the Android version must be equal to or greater than 12.

## Requirements

- Android Studio Arctic Fox or later
- Android SDK version 31 or later
- An Android device or emulator running Android 10 or later

## Getting Started

1. Clone this repository:
2. git clone https://github.com/<your-username>/to-do-list.git
3. Open the project in Android Studio:
   cd to-do-list
   open -a "Android Studio"
4. Build and run the app:

- Connect your Android device or start an emulator
- Click the Run button in Android Studio or run the following command in the terminal:

  ```
  ./gradlew installDebug
  ```

## Contributing

Contributions are always welcome! Here are some ways you can contribute:

- Report issues and bugs
- Suggest new features and enhancements
- Fix issues and bugs
- Implement new features and enhancements

To contribute, please fork this repository, make your changes, and submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Android Open Source Project](https://source.android.com/)
- [Google Developer Documentation](https://developer.android.com/docs)


