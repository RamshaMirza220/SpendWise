# 💰 SpendWise – Personal Finance & Budget Tracker

A modern Android application built with **Kotlin** and **Jetpack Compose** for tracking personal expenses, managing monthly budgets, and visualizing spending patterns through real-time analytics.

---

## 📱 Screenshots

> Dashboard | Add Expense | Analytics

---

## ✨ Features

- 📊 **Real-time budget tracker** with monthly spending overview
- ➕ **Add expenses** with 8 categories (Food, Transport, Shopping, Health, etc.)
- 📈 **Analytics dashboard** with interactive pie chart (MPAndroidChart)
- 🏗️ **Room DB persistence** — all data stored locally, works offline
- 🎨 **Material 3 UI** with Jetpack Compose
- 🔄 **StateFlow** reactive updates across all screens

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Repository Pattern |
| Database | Room DB |
| DI | Hilt (Dagger) |
| Reactive State | StateFlow + Coroutines |
| Charts | MPAndroidChart |
| Navigation | Jetpack Navigation Compose |

---

## 🏗️ Architecture

```
com.ramsha.spendwise
├── data
│   ├── local (Room DB, DAO, Entities)
│   └── repository (ExpenseRepository)
├── di (Hilt AppModule)
├── domain
│   └── model (Expense, ExpenseCategory)
├── ui
│   ├── screens (Dashboard, AddExpense, Analytics)
│   └── theme (Material 3 Theme)
└── viewmodel (ExpenseViewModel + StateFlow)
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 26+

### Run the App
```bash
git clone https://github.com/RamshaMirza220/SpendWise.git
cd SpendWise
# Open in Android Studio and run on emulator or device
```

---

## 👩‍💻 Author

**Ramsha Mirza** — Flutter & Android Developer  
[GitHub](https://github.com/RamshaMirza220) • [LinkedIn](https://linkedin.com/in/ramsha-mirza)
