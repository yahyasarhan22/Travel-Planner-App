# 1221858_1220137_CourseProject
### Travel Planner App — ENCS5150 Android Course Project
**Birzeit University · Advanced Computer Systems Engineering Laboratory · First Semester 2026**

---

## Team Members

| Name | Student ID | Role |
|------|-----------|------|
| Yahya Sarhan | 1221858 | Login & Registration · Reservations · Favorites · Profile · Contact Us |
| Yousef Qawwas | 1220137 | Splash & Intro · API · Trips · Special Section · Admin Panel · Animations |

---

## App Overview

A **Travel Planner** Android application built in Java that helps users discover, reserve, and manage travel destinations. Users can browse trips fetched from a REST API, save favorites, make reservations, and manage their profiles — all through a modern, user-friendly interface.

- **App Type:** Travel Planner App
- **Theme:** Left Theme (`#EEEEEE` · `#6FCF97` · `#2FA084` · `#1F6F5F`)
- **Target Device:** Pixel 3a XL · API Level 28
- **Minimum SDK:** API 28

---

## Features

### User Side
- **Splash Screen** — Animated logo display on launch
- **Introduction Screen** — App overview with REST API connection button
- **Login & Registration** — Secure auth with input validation, Remember Me, and encrypted passwords
- **Home** — Navigation Drawer with app overview and theme description
- **Trips** — Browse all trips via RecyclerView with search and filter support
- **Trip Detail** — Full trip info displayed in a Fragment
- **Reservations** — Make, view, and track all trip reservations
- **Favorites** — Save trips, remove them, and reserve directly
- **Special Section** — Curated popular destinations and recommended trips
- **Profile Management** — View and update personal info and profile picture
- **Contact Us** — Call, locate on Maps, or email the support team

### Admin Side
- Separate admin Navigation Drawer
- Add / Edit / Delete trips
- View and delete users
- View all reservations
- Add new admins

> **Default admin credentials:**
> Email: `admin@admin.com` · Password: `Admin123!`

---

## Tech Stack

| Concept | Implementation |
|---------|---------------|
| UI | Android Layouts (Static & Dynamic), Navigation Drawer, RecyclerView, Fragments |
| Navigation | Intents, Fragment Transactions |
| Data | SQLite via `DatabaseHelper.java` |
| Networking | Retrofit / Volley — RESTful API |
| Storage | SharedPreferences (Remember Me, session) |
| Security | AES-encrypted password storage |
| Animations | Fade, Slide Up, Bounce (`res/anim/`) |
| Notifications | Toast messages |
| Validation | Custom `InputValidator.java` |

---

## Project Structure

```
app/src/main/
├── java/com/travel/app/
│   ├── activities/
│   │   ├── SplashActivity.java
│   │   ├── IntroductionActivity.java
│   │   ├── LoginActivity.java
│   │   ├── RegisterActivity.java
│   │   ├── MainActivity.java
│   │   ├── AdminActivity.java
│   │   └── ContactUsActivity.java
│   ├── fragments/
│   │   ├── HomeFragment.java
│   │   ├── TripsFragment.java
│   │   ├── TripDetailFragment.java
│   │   ├── SpecialFragment.java
│   │   ├── ReservationsFragment.java
│   │   ├── FavoritesFragment.java
│   │   └── ProfileFragment.java
│   ├── adapters/
│   │   ├── TripAdapter.java
│   │   ├── FavoriteAdapter.java
│   │   ├── ReservationAdapter.java
│   │   ├── AdminTripAdapter.java
│   │   └── AdminUserAdapter.java
│   ├── models/
│   │   ├── Trip.java
│   │   ├── User.java
│   │   └── Reservation.java
│   ├── database/
│   │   └── DatabaseHelper.java
│   ├── network/
│   │   ├── ApiService.java
│   │   └── ApiClient.java
│   └── helpers/
│       ├── SessionManager.java
│       ├── PasswordEncryptor.java
│       └── InputValidator.java
└── res/
    ├── layout/         # All XML screen layouts
    ├── menu/           # Navigation drawer menus
    ├── anim/           # fade_in, slide_up, bounce
    ├── drawable/       # Icons, logo, button backgrounds
    └── values/         # colors.xml, strings.xml, themes.xml, dimens.xml
```

---

## Database Schema

| Table | Key Columns |
|-------|------------|
| `users` | `id`, `email`, `first_name`, `last_name`, `password` (encrypted), `gender`, `major`, `phone`, `profile_pic`, `is_admin` |
| `trips` | `id`, `destination`, `country`, `duration_days`, `price`, `rating`, `description`, `image_url` |
| `reservations` | `id`, `user_id`, `trip_id`, `quantity`, `type`, `date`, `status` |
| `favorites` | `id`, `user_id`, `trip_id` |

---

## API Format

The app fetches trip data from a RESTful API on first launch. Expected response format:

```json
[
  {
    "id": 101,
    "destination": "Istanbul",
    "country": "Turkey",
    "duration_days": 5,
    "price": 750,
    "rating": 4.7,
    "description": "Explore historical landmarks",
    "image": "https://example.com/istanbul.jpg"
  }
]
```

---

## How to Run

1. Clone this repository
2. Open in **Android Studio**
3. Set the emulator to **Pixel 3a XL, API 28** (Graphics: Software)
4. Click **Run** or build the APK via `Build → Build APK(s)`

---

## Submission

- `Project.zip` — exported from `File → Export → Export to Zip File`
- `app-debug.apk` — found at `app/build/outputs/apk/debug/app-debug.apk`

---

## Course Info

- **Course:** ENCS5150 — Advanced Computer Systems Engineering Laboratory
- **Institution:** Birzeit University
- **Semester:** First Semester 2026
