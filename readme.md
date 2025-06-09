简介：
基于 Jetpack Compose 和高德地图天气 API 的 Android 应用，支持实时天气查询、未来四天预报展示、本地历史记录缓存及管理。
介绍：
实时天气查询：通过城市名称查询当前实况天气，包含温度、天气、风力、更新时间。
未来四天预报：展示当天及未来三天的天气预报，支持左右滑动查看。
历史记录缓存：使用 Room 本地存储查询过的天气数据，缓存有效期可配置，避免频繁网络请求。
历史管理：切换到历史页面可查看、重查历史记录。

项目结构
app/
├── src/main/java/com/example/weatherapp/
│   ├── data/
│   │   ├── local/              # Room 实体、Dao、Database
│   │   ├── model/              # 数据模型（WeatherResponse、ForecastResponse 等）
│   │   ├── network/            # 高德天气 API 调用（WeatherService）
│   │   └── repository/         # 缓存策略与数据仓库（WeatherRepository）
│   ├── ui/
│   │   ├── main/               # 主界面 Compose 入口（MainActivity、MainContent）
│   │   ├── forecast/           # 预报展示组件（ForecastSection.kt）
│   │   ├── components/         # 可复用 UI 组件
│   │   └── theme/              # 默认主题与样式
│   ├── viewmodel/              # ViewModel
│   └── WeatherApplication.kt   # Application
└── README.md
