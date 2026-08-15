# Chahida App - Project Analysis

## Architecture
The application follows the **MVVM (Model-View-ViewModel)** architectural pattern. It is built using **Jetpack Compose** for a modern, declarative UI.

### Dependency System
- **Core**: Kotlin, Coroutines, AndroidX.
- **UI**: Jetpack Compose (Material 3), Compose Navigation.
- **Networking**: Retrofit 2, OkHttp, GSON Converter.
- **Image Loading**: Coil.
- **Persistence**: SharedPreferences (Local Cart & Order history).
- **Effects**: Haze (Glassmorphism), Compose Animations.

## Folder Structure
- `data/`: Data models and API definitions.
  - `api/`: Retrofit client and API service.
  - `model/`: Data classes for Products, Orders, FAQ, etc.
- `components/`: Reusable UI components (Product Cards, Banner, Shimmer).
- `screens/`: Feature-specific screens and ViewModels.
  - `home/`: Home screen with categories, deals, and FAQ.
  - `cart/`: Cart management and checkout initiation.
  - `products/`: Full product listing with filtering.
  - `orders/`: Order placement and history.
  - `details/`: Product detail view.
- `ui/theme/`: Custom theme definitions (Gold/Dark theme).

## Screen Breakdown
- **SplashActivity**: Displays the brand logo with a fade-in animation.
- **HomeScreen**:
  - Stylish brand header ("Chahida").
  - Auto-scrolling Hero Banner.
  - Category list with filtering logic.
  - Flash Deals product grid.
  - Modern FAQ section with expansion animation.
- **CartScreen**:
  - List of selected items with piece count.
  - Real-time subtotal and total calculation.
  - Curved summary section with "Make Payment" action.
- **ProductsScreen**:
  - All-products grid with search and category filtering.
  - Skeleton loading states.
- **CheckoutScreen**:
  - Order summary.
  - Customer info form with Division/District dropdowns.
- **MyOrdersScreen**:
  - Detailed order history showing product photos and status.

## Design System
- **Primary Color**: `#E1A200` (Gold)
- **Secondary Color**: `#171512` (Dark Black)
- **Background**: `#FFFFFF` / `#0F172B`
- **Price Accent**: `#EC003F` (Red)
- **Style**: Glassmorphism (using Haze), Premium rounded corners (32dp+), Shimmer loading effects.
