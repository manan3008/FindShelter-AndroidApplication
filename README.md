# FindShelter Mobile Application

## Overview

The FindShelter application is designed to help users find accommodation in shelters. This mobile application extends its functionality by incorporating a database to store shelter details, enabling users to send email requests for accommodation, identifying the user's current location, and displaying both the user's location and the shelter's location on a Google Map. This project is aimed at enhancing user experience and contributing socially innovative features to support those in need of shelter.

## Objective

- Design and develop an enhanced version of the FindShelter application.
- Incorporate a local database for storing and retrieving shelter details.
- Enable email communication with shelters directly from the application.
- Utilize location services to find the user's current location.
- Integrate Google Maps to display relevant location markers.

## Features

![image](https://github.com/manan3008/FindShelter-AndroidApplication/assets/68306893/231e8938-6eb6-488a-9fdc-fab5045d3c6c)

### Database Integration

- Utilize Room and LiveData for local database integration.
- Populate the database with a provided list of shelters on the first startup.
- Enable saving edits of shelter details (phone and email) to the database.

### Email Communication

- Implement functionality to send an email to shelters requesting accommodation.
- Ensure email functionality is conditional on the availability of shelter's name and email.
- Use string resources for constructing the email content.

### Location Services

- Implement location services to identify the user's current location.
- Ensure accurate location tracking to enhance the map's functionality.

### Google Map Integration

- Introduce a new activity displaying a Google Map with markers for the shelter's location and the user's current location.
- Ensure the map centers on the shelter's location and includes markers with descriptive labels.
- Set an appropriate zoom level to enhance visibility.

## Development and Design

This application is developed for mobile platforms, focusing on practicality, user-friendliness, and social innovation. The design considerations include a client-server architecture to support future expansions, such as real-time updates, integration with social services, and community-driven features.

### Future Design Considerations

- Enhance the application to become a client/server system.
- Integrate with social media and other APIs for broader reach and functionality.
- Implement features to coordinate with other organizations and services.
- Consider user privacy and data protection in all features.

## Running the Application

To run the application, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE that supports mobile development (e.g., Android Studio).
3. Configure the project to include Google Maps API and email services.
4. Build and run the application on a compatible mobile device or emulator.

