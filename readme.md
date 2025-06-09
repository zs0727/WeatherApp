简介：

基于 Jetpack Compose 和高德地图天气 API 的 Android 应用，支持实时天气查询、未来四天预报展示、本地历史记录缓存及管理。


介绍：

实时天气查询：通过城市名称查询当前实况天气，包含温度、天气、风力、更新时间。

未来四天预报：展示当天及未来三天的天气预报，支持左右滑动查看。

历史记录缓存：使用 Room 本地存储查询过的天气数据，缓存有效期可配置，避免频繁网络请求。

历史管理：切换到历史页面可查看、重查历史记录。




项目结构

app/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── weatherapp/
│       │               ├── data/
│       │               │   ├── local/              # Room数据库相关
│       │               │   ├── model/              # 数据模型类
│       │               │   ├── network/            # 网络请求相关
│       │               │   └── repository/         # 数据仓库实现
│       │               ├── ui/
│       │               │   ├── main/               # 主界面相关
│       │               │   ├── forecast/           # 天气预报组件
│       │               │   ├── components/         # 公共UI组件
│       │               │   └── theme/              # 应用主题
│       │               ├── viewmodel/              # ViewModel层
│       │               └── WeatherApplication.kt    # 应用入口
│       └── res/                                    # 资源文件目录
└── README.md
