This is a new [**React Native**](https://reactnative.dev) project, bootstrapped using [`@react-native-community/cli`](https://github.com/react-native-community/cli).

# Getting Started

>**Note**: Make sure you have completed the [React Native - Environment Setup](https://reactnative.dev/docs/environment-setup) instructions till "Creating a new application" step, before proceeding.

## Step 1: Start the Metro Server

First, you will need to start **Metro**, the JavaScript _bundler_ that ships _with_ React Native.

To start Metro, run the following command from the _root_ of your React Native project:

```bash
# using npm
npm start

# OR using Yarn
yarn start
```

## Step 2: Start your Application

Let Metro Bundler run in its _own_ terminal. Open a _new_ terminal from the _root_ of your React Native project. Run the following command to start your _Android_ or _iOS_ app:

### For Android

```bash
# using npm
npm run android

# OR using Yarn
yarn android
```

### For iOS

```bash
# using npm
npm run ios

# OR using Yarn
yarn ios
```

If everything is set up _correctly_, you should see your new app running in your _Android Emulator_ or _iOS Simulator_ shortly provided you have set up your emulator/simulator correctly.

This is one way to run your app — you can also run it directly from within Android Studio and Xcode respectively.

## Step 3: Modifying your App

Now that you have successfully run the app, let's modify it.

1. Open `App.tsx` in your text editor of choice and edit some lines.
2. For **Android**: Press the <kbd>R</kbd> key twice or select **"Reload"** from the **Developer Menu** (<kbd>Ctrl</kbd> + <kbd>M</kbd> (on Window and Linux) or <kbd>Cmd ⌘</kbd> + <kbd>M</kbd> (on macOS)) to see your changes!

   For **iOS**: Hit <kbd>Cmd ⌘</kbd> + <kbd>R</kbd> in your iOS Simulator to reload the app and see your changes!

## Congratulations! :tada:

You've successfully run and modified your React Native App. :partying_face:

### Now what?

- If you want to add this new React Native code to an existing application, check out the [Integration guide](https://reactnative.dev/docs/integration-with-existing-apps).
- If you're curious to learn more about React Native, check out the [Introduction to React Native](https://reactnative.dev/docs/getting-started).

# Troubleshooting

If you can't get this to work, see the [Troubleshooting](https://reactnative.dev/docs/troubleshooting) page.

# Learn More

To learn more about React Native, take a look at the following resources:

- [React Native Website](https://reactnative.dev) - learn more about React Native.
- [Getting Started](https://reactnative.dev/docs/environment-setup) - an **overview** of React Native and how setup your environment.
- [Learn the Basics](https://reactnative.dev/docs/getting-started) - a **guided tour** of the React Native **basics**.
- [Blog](https://reactnative.dev/blog) - read the latest official React Native **Blog** posts.
- [`@facebook/react-native`](https://github.com/facebook/react-native) - the Open Source; GitHub **repository** for React Native.

---

# **Application Structure**

```bash
ARWheelApp/
├── __tests__/
├── .bundles/
├── android/
├── ios/
├── node_modules/
├── assets/
│   ├── images/                        ← ภาพทั่วไปและ UI ที่ไม่เกี่ยวกับ AR โดยตรง
│   │   └── icons/                       ← icon แอป
│   ├── models/                        ← glb/usdz/3d models
│   │   └── default/                     ← โมเดลล้อแม็กพื้นฐาน
│   └── markers/                       ← ภาพ marker ที่กล้อง AR ใช้สำหรับระบุพิกัด (สำหรับ marker-based AR)
├── src/
│   ├── api/                           ← ฟังก์ชันเชื่อมต่อ backend, Firebase, etc.
│   │   ├── firebase.ts                  ← ตั้งค่า Firebase App
│   │   ├── apiClient.ts                 ← Axios instance สำหรับเรียก backend
│   │   ├── endpoints.ts                 ← รวม URL endpoint ของ backend
│   │   └── uploadHelper.ts              ← ฟังก์ชันอัปโหลดไฟล์ (ภาพ/โมเดล)
│   ├── components/                    ← UI components เช่น ปุ่ม, แถบล่าง, การ์ดล้อแม็ก
│   │   ├── WheelCard.tsx                ← การ์ดแสดงล้อแต่ละแบบ
│   │   ├── BottomTabBar.tsx             ← Custom tab bar UI
│   │   ├── ARPermissionModal.tsx        ← Modal ขอสิทธิ์กล้อง
│   │   ├── ModelSelector.tsx            ← UI dropdown/select model
│   │   ├── Button.tsx                   ← ปุ่ม reusable
│   │   ├── Loading.tsx                  ← Loading spinner ทั่วไป
│   │   └── ErrorMessage.tsx             ← แสดง error แบบ reusable
│   ├── constants/                     ← ค่าคงที่ เช่น สี, ขนาด, enum ต่าง ๆ
│   │   ├── colors.ts                    ← สีหลักของแอป
│   │   ├── fonts.ts                     ← ฟอนต์และขนาด
│   │   ├── routes.ts                    ← ชื่อ route / screen name
│   │   ├── enums.ts                     ← เช่น ประเภทล้อ, ประเภทผู้ใช้
│   │   └── theme.ts                     ← Light / Dark theme config (ถ้ามี)
│   ├── contexts/                      ← React Context (Auth, AR, Theme)
│   │   ├── AuthContext.tsx              ← เก็บข้อมูลผู้ใช้ที่ล็อกอิน
│   │   ├── ARContext.tsx                ← เก็บสถานะการ track AR, กล้อง
│   │   └── ThemeContext.tsx             ← เปลี่ยนธีม Dark/Light
│   ├── hooks/                         ← Custom hooks เช่น useCameraPermission, useARTracking
│   │   ├── useCameraPermission.ts       ← เช็คและขอสิทธิ์กล้อง
│   │   ├── useLightingDetection.ts      ← ตรวจจับแสงใน AR
│   │   ├── useARTracking.ts             ← เช็ค AR พร้อมหรือไม่
│   │   ├── useModelCache.ts             ← จัดการโหลด+แคช 3D models
│   │   ├── useOrientation.ts            ← ตรวจจับการหมุนเครื่อง
│   │   └── useLanguage.ts               ← จัดการเปลี่ยนภาษา
│   ├── i18n/                          ← การจัดการ i18n (internationalization)
│   │   ├── index.ts
│   │   └── locales/
│   │       ├── en.json
│   │       └── th.json
│   ├── models/                        ← TypeScript interface / schema ของข้อมูลล้อ
│   │   ├── WheelModel.ts                ← interface สำหรับล้อแม็ก
│   │   ├── User.ts                      ← interface ผู้ใช้งาน
│   │   ├── Store.ts                     ← interface ร้านค้า
│   │   ├── ScanResult.ts                ← ข้อมูลผลลัพธ์จากการสแกนล้อ
│   │   └── AIResult.ts                  ← โครงสร้างผลลัพธ์จาก AI
│   ├── navigation/                    ← React Navigation stack/tab/screen config
│   │   ├── AppNavigator.tsx             ← รวม Stack/Tab Navigator
│   │   ├── RootNavigation.ts            ← ฟังก์ชันควบคุม navigation จากภายนอก
│   │   ├── navigationTypes.ts           ← type ของ route param
│   │   └── NavigationTheme.ts           ← ปรับสีของ navigator ตามธีม
│   ├── screens/                       ← หน้าจอหลักของแอป
│   │   ├── HomeScreen.tsx               ← หน้าแรกแสดงล้อแม็ก
│   │   ├── ARScreen.tsx                 ← กล้อง AR marker-based / markerless
│   │   ├── ProfileScreen.tsx            ← หน้าโปรไฟล์ผู้ใช้
│   │   ├── ModelPickerScreen.tsx        ← เลือกล้อแม็กจากรายการ
│   │   ├── SplashScreen.tsx             ← หน้า loading แรก
│   │   ├── OnboardingScreen.tsx         ← หน้าแนะนำการใช้งานครั้งแรก
│   │   ├── ModelDetailScreen.tsx        ← รายละเอียดล้อแม็ก
│   │   └── ScanResultScreen.tsx         ← แสดงผลจากการสแกนและวิเคราะห์
│   ├── services/
│   │   ├── authService.ts               ← Login/Register/Firebase Auth
│   │   ├── modelService.ts              ← CRUD ล้อแม็ก
│   │   ├── trackingService.ts           ← วิเคราะห์มุมจากภาพ
│   │   ├── lightingService.ts           ← ตรวจจับแสง (ใช้ร่วมกับ AR)
│   │   ├── cacheService.ts              ← จัดการ cache local mode
│   │   └── languageService.ts           ← เปลี่ยนภาษาและจัดการค่าภาษา
│   ├── state/
│   │   ├── useAuthStore.ts              ← จัดการสถานะผู้ใช้
│   │   ├── useARStore.ts                ← เก็บสถานะกล้อง / model ที่เลือก
│   │   ├── useModelStore.ts             ← จัดการ state ของ model ที่โหลด
│   │   └── store.ts                     ← รวม store ทั้งหมด - Redux, Zustand หรือ Recoil
│   ├── utils/                         ← ฟังก์ชันทั่วไป เช่น format, validate, log
│   │   ├── formatter.ts                 ← format ข้อความ, วันที่, ขนาด ฯลฯ
│   │   ├── validator.ts                 ← เช็คความถูกต้องของ input
│   │   ├── logger.ts                    ← log แบบ custom
│   │   ├── fileHelper.ts                ← จัดการแปลง/โหลดไฟล์
│   │   ├── permissionHelper.ts          ← ช่วยจัดการ permission
│   │   ├── platform.ts                  ← ตรวจว่าเป็น iOS / Android
│   │   └── arHelper.ts                  ← ช่วยคำนวณตำแหน่ง AR
│   └── App.tsx
├── .eslintrc.js
├── .gitignore
├── .prettierrc.js
├── .watchmanrconfig
├── app.json
├── App.tsx
├── babel.config.js
├── Gemfile
├── index.js
├── jest.config.js
├── metro.config.js
├── package-lock.json
├── package.json
├── README.md
└── tsconfig.json
```
