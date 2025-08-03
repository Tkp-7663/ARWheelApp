
# **Application Structure**

```bash
ARWheelApp/
├── __tests__/
├── .bundles/
├── android/
├── ios/
├── node_modules/
├── assets/                            ← ไฟล์ static เช่น รูป ไอคอน โมเดล AR
│   ├── images/
│   ├── models/                          ← glb/usdz/3d models
│   └── markers/                         ← รูป marker สำหรับ marker-based AR
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
├── tsconfig.json
└── yarn.lock
```
