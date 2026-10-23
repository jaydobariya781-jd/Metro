# Ahmedabad Metro Transit Application

Official Android Mobile Application for the Ahmedabad Metro Rail Project (GMRC).

## Overview
The **Ahmedabad Metro** app provides commuters and transit administrators with a modern, high-performance transit ticketing and fleet operations management system.

---

## Key Features

### Passenger Experience (User-Side)
- **Interactive Metro Map**: Route browsing across East-West (Blue Line) and North-South (Red Line) corridors.
- **Smart QR Ticket Booking**: Station selection, passenger count, instant fare estimation, and dynamic QR generation.
- **Digital Daily Pass**: Unlimited one-day travel pass with secure OTP verification.
- **Payment Gateway Integration**: Multi-method checkout supporting UPI (Google Pay, PhonePe, Paytm), Metro Smart Card, and Cash.
- **Live Ticket Manager**: Offline-capable ticket storage with Active and History tab views.
- **Saved Stations & Favorites**: Quick access to frequent stations (Home, Work, College).
- **Service Alerts & Notifications**: Real-time broadcast alerts for maintenance, timetable changes, and offers.

### Administrative Console (Admin-Side)
- **Live Operations Dashboard**: System-wide revenue, active commuters, daily ridership KPIs, and peak hours.
- **Station Management**: Platform counts, interchange status, first/last train timetables, and operational toggling.
- **Route & Fare Matrix**: Distance calculation, transfer stations, and inter-corridor route management.
- **Ridership & Financial Reports**: Exportable analytics summaries (PDF / CSV format).
- **Audit Logging**: Comprehensive security tracking for all administrative overrides and turnstile verifications.
- **User Role Management**: Search, filter, and modify passenger and administrator accounts.

---

## Technical Stack & Architecture
- **Language**: Kotlin 1.9+
- **Platform**: Android Native (minSdk 24, targetSdk 35)
- **UI Architecture**: Activities & Fragments with Material Design Components
- **Persistence**: SharedPreferences with thread-safe singleton managers
- **Barcode / QR Engine**: ZXing Android Embedded for 512x512 turnstile QR codes
- **Build System**: Gradle 8.7

---

## Team Contributions
- **Jay Dobariya**: Project setup, user authentication flow, splash, onboarding, settings, notifications, validation utilities.
- **Brij Nagvadiya**: Metro booking engine, QR generation, fare calculation, ticket persistence, route search.
- **Alok Kumar**: Admin console dashboard, station management, route repository, revenue analytics, audit logger.

---

## License
Ahmedabad Metro Rail Project. All rights reserved.