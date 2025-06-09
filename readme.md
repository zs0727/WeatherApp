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


技术难点与解决方案 

异步网络与 UI 线程切换

问题：Android 禁止网络操作在主线程，否则抛出 NetworkOnMainThreadException 并 ANR。

解决：使用 Kotlin 协程与 withContext(Dispatchers.IO)，在 ViewModel 中 viewModelScope.launch，确保异步安全。

缓存策略与数据一致性

问题：缓存过期判断不准确，或同一条数据重复插入。

解决：

使用城市+时间戳复合主键与 OnConflictStrategy.REPLACE 实现去重覆盖；

增加 CACHE_VALIDITY_SECONDS 参数，只有当本地数据超过阈值时才发网络请求。

跨时区时间解析与显示

问题：高德返回 reporttime 为 UTC+8，系统时区不同导致显示时间偏差。

解决：解析时明确设置 SimpleDateFormat.timeZone=TimeZone.getTimeZone("Asia/Shanghai")，再用系统时区格式化输出。



学习总结

在本项目中，我重点分析了网络请求和缓存过程中的三个核心问题：

主线程阻塞：原始同步调用造成界面卡顿，优化后使用协程+Dispatcher-IO，结合 Retrofit/OkHttp 超时参数，稳定高效。

JSON 解析容错：高德接口字段可能变化，加入 optString 与非空判断，数据类字段转为可空加默认值。

缓存时效性控制：设计灵活的缓存有效期策略，避免数据过旧或重复查询，同时通过复合主键确保本地存储规范。

